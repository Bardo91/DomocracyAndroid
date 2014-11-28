package es.domocracy.domocracyapp.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HubConnectionBluetooth implements HubConnection {
	// -----------------------------------------------------------------------------------
	// Static bluetooth adapter initialization
	// -----------------------------------------------------------------------------------------------------------------
	final private String HubName = "HC-06";
	static private BluetoothAdapter mBtAdapter;

	static public void initBluetooth(Context _context) {
		// Get default bluetooth device.
		if (mBtAdapter == null) {
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			assert (mBtAdapter != null);
		}

		// If bluetooth is not enabled,
		if (!mBtAdapter.isEnabled()) {
			mBtAdapter.enable();
		}

		Log.d("DMC_BT", "Bluetooth Enabled");
	}

	// -----------------------------------------------------------------------------------------------------------------
	static public void unloadBluetooth(Context _context) {
		if (mBtAdapter.isEnabled()) {
			mBtAdapter.disable();
		}
		Log.d("DMC_BT", "Bluetooth Disabled");
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Bluetooth Broadcast receiver.
	private final BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d("DMC_BT", "Received something");
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				Log.d("DMC_BT", "Founded device");
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				try {
					if (device.getName().equals(HubName)) {
						Log.d("DMC_BT", "Found " + HubName);
						mBtAdapter.cancelDiscovery();
						connect2Device(device);
					}
				} catch (NullPointerException _e) {
					Log.d("DMC-BT", "Device is null");
					_e.printStackTrace();
				}
			}
		}
	};

	// -----------------------------------------------------------------------------------------------------------------
	private void registerBtReceiver(Context _context) {
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		_context.registerReceiver(mBtReceiver, filter);
		Log.d("DMC_BT", "Registered listener");
	}

	// -----------------------------------------------------------------------------------------------------------------
	private void unregisterBtReceiver(Context _context) {
		_context.unregisterReceiver(mBtReceiver);
		Log.d("DMC_BT", "Unregister listener");
	}

	// -----------------------------------------------------------------------------------
	// HubConnectionbluetooth
	// -----------------------------------------------------------------------------------------------------------------
	public static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private BluetoothSocket mHubSocket;
	private Thread connectionThread;

	protected Hub mHub;
	protected InputStream mInStream;
	protected OutputStream mOutStream;
	private final int SLEEP_TIME = 200;

	private int mBufferLenght = 0;
	private byte[] mPersistentBuffer = new byte[2048];

	// -----------------------------------------------------------------------------------------------------------------
	public boolean connectToHub(final Hub _hub, Context _context) {
		registerBtReceiver(_context);
		mBtAdapter.startDiscovery();
		Log.d("DMC_BT", "Started discovery");
		return isConnected();
	}

	// -----------------------------------------------------------------------------------------------------------------
	public boolean closeConnection(Context _context) {
		unregisterBtReceiver(_context);
		if (isConnected()) {
			try {
				mHubSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Log.d("DMC_BT", "Connection closed");

		return (!isConnected());
	}

	// -----------------------------------------------------------------------------------------------------------------
	public boolean isConnected() {
		return (mHubSocket != null && mHubSocket.isConnected()) ? true : false;
	}

	// -----------------------------------------------------------------------------------------------------------------
	public void connect2Device(BluetoothDevice device) {
		final BluetoothDevice mmDevice = device;

		BluetoothSocket tmp = null;

		try {
			tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
		} catch (IOException e) {
		}
		mHubSocket = tmp;

		Log.d("DMC_BT", "Waiting connection with hub");
		connectionThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					mHubSocket.connect();
				} catch (IOException ConnectEx) {
					Log.d("DMC_BT", "Error connecting to hub");
					try {
						mHubSocket.close();
					} catch (IOException closeEx) {
					}
					return;
				}
				try {
					mInStream = mHubSocket.getInputStream();
					mOutStream = mHubSocket.getOutputStream();
					
					initReading();
					Log.d("DMC_BT", "Connected to hub");
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		connectionThread.start(); // Start the previously defined thread.

	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean sendMsg(Message _msg) {
		if (isConnected()) {
			try {
				mOutStream.write(_msg.rawMessage());
				Log.d("DMC", "Sended msg: " + new String(_msg.rawMessage()));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public Message readBuffer() {
		if (0 < mBufferLenght) {
			// Create message
			assert (0 != mPersistentBuffer[1]);
			Log.d("DMC", "Received a message of type: " + mPersistentBuffer[1]);
			byte[] rawMsg = Arrays.copyOf(mPersistentBuffer,
					mPersistentBuffer[0]);
			Message msg = Message.decode(rawMsg);

			// Empty buffer
			byte msgSize = mPersistentBuffer[0];
			System.arraycopy(mPersistentBuffer, msgSize, mPersistentBuffer, 0,
					mBufferLenght);
			mBufferLenght -= msgSize;

			if (msg.isValid())
				return msg;
		}
		return null;
	}

	// -----------------------------------------------------------------------------------
	protected void initReading() {
		Thread readingThread = new Thread() {
			@Override
			public void run() {
				for (;;) {
					try {
						sleep(SLEEP_TIME);
					} catch (InterruptedException sleep_exception) {
					}

					byte[] buffer = new byte[1024];

					int nBytes = 0;
					try {
						if (0 < mInStream.available()) {
							nBytes = mInStream.read(buffer);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (nBytes > 0) {
						System.arraycopy(buffer, 0, mPersistentBuffer,
								mBufferLenght, nBytes);
						mBufferLenght += nBytes;
					}
				}
			}
		};
		readingThread.start();
	}

}