package es.domocracy.domocracyapp.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import es.domocracy.domocracyapp.comm.ConnectionManager.eConnectionTypes;
import android.content.Context;
import android.util.Log;

public class HubConnectionWifi implements HubConnection {
	// ----------------------------------------------------------------------------------------------------------------
	// Class members
	// ----------------------------------------------------------------------------------------------------------------
	protected Hub mHub;
	protected InputStream mInStream;
	protected OutputStream mOutStream;
	private final int SLEEP_TIME = 200;

	private int mBufferLenght = 0;
	private byte[] mPersistentBuffer = new byte[2048];
	
	// ----------------------------------------------------------------------------------------------------------------
	// HubConnectionWifi
	// ----------------------------------------------------------------------------------------------------------------
	private Socket mHubSocket;	
	
	// ----------------------------------------------------------------------------------------------------------------
	public boolean isConnected(){
		return (mHubSocket != null && mHubSocket.isConnected())? true : false;
		
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	public boolean closeConnection(Context _Context) {
		if (isConnected()) {
			try {
				for (;;) {
					mHubSocket.close();
					if (mHubSocket.isClosed())
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	protected final int TIMEOUT = 2000;
	public boolean connectToHub(final Hub _hub, Context _context) {
		assert(_hub.connType() == eConnectionTypes.eWifi);
		
		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					if (_hub.port() != -1) {
						mHubSocket = new Socket();
						mHubSocket.connect(new InetSocketAddress(_hub.addr(),
								_hub.port()), TIMEOUT);

						mInStream = mHubSocket.getInputStream();
						mOutStream = mHubSocket.getOutputStream();

						mHub = _hub;
						
						initReading();
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();

		Long t0 = System.currentTimeMillis();
		while (!isConnected()) {
			Long t1 = System.currentTimeMillis();
			if (t1 - t0 >= TIMEOUT) {
				Log.d("DMC", "Could not connect to device");
				break;
			}
		}

		return isConnected();
	}

	// ----------------------------------------------------------------------------------------------------------------
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

	// ----------------------------------------------------------------------------------------------------------------
	@Override
	public Message readBuffer() {
		if (0 < mBufferLenght) {
			// Create message
			assert(0 != mPersistentBuffer[1]);
			Log.d("DMC", "Received a message of type: " + mPersistentBuffer[1]);
			byte[] rawMsg = Arrays.copyOf(mPersistentBuffer,mPersistentBuffer[0]);
			Message msg = Message.decode(rawMsg);

			// Empty buffer
			byte msgSize = mPersistentBuffer[0];
			System.arraycopy(mPersistentBuffer, msgSize, mPersistentBuffer, 0, mBufferLenght);
			mBufferLenght -= msgSize;

			if (msg.isValid())
				return msg;
		}
		return null;
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	protected void initReading() {
		Thread readingThread = new Thread() {
			@Override
			public void run() {
				for (;;) {
					try { sleep(SLEEP_TIME); } 
					catch (InterruptedException sleep_exception) {}
					
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
						System.arraycopy(buffer, 0, mPersistentBuffer, mBufferLenght, nBytes);
						mBufferLenght += nBytes;
					}
				}
			}
		};
		readingThread.start();
	}
}
