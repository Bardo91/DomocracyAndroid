package es.domocracy.domocracyapp.comm;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HubConnectionBluetooth extends HubConnection {
	// -----------------------------------------------------------------------------------
	// Static bluetooth adapter initialization
	static private BluetoothAdapter mBtAdapter;
	
	static private void initBluetooth(){
		// Get default bluetooth device.
		if (mBtAdapter == null){
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			assert(mBtAdapter != null);
		}
		
		// If bluetooth is not enabled, tell the system to show start bluetooth menu
		if(!mBtAdapter.isEnabled()){
			mBtAdapter.enable();
		}
		
	}

	// Bluetooth Broadcast receiver.
	private final BroadcastReceiver mBtReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        	if (device.getName().equals("HC-06")) { // 666 TODO: connect to different bts
	        		Log.d("DMC_BT", "Found HC-06");
	        		mBtAdapter.cancelDiscovery();
	        		connectAsServer(device);
	        	}
	        }
	    }
	};
	
	
	public void registerBtReceiver(Context _context){
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		_context.registerReceiver(mBtReceiver, filter); // Don't forget to unregister during onDestroy

	}
	
	public void unregisterBtReceiver(Context _context){
		_context.unregisterReceiver(mBtReceiver);
		
	}
	
	static void startDiscovery(){
		mBtAdapter.startDiscovery();
	}
	
	// -----------------------------------------------------------------------------------
	// HubConnection
	public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private final int TIMEOUT = 2000;
	private BluetoothSocket mHubSocket;
	
	// -----------------------------------------------------------------------------------
	public boolean connectToHub(final Hub _hub) {
		initBluetooth();
		
		return isConnected();
	}

	// -----------------------------------------------------------------------------------
	public boolean closeConnection(Context _context) {
		unregisterBtReceiver(_context);
		if(isConnected()){
			
		}
		
		return (!isConnected());
	}

	// -----------------------------------------------------------------------------------
	public boolean isConnected() {
		return (mHubSocket != null && mHubSocket.isConnected()) ? true : false;
	}

	
	
	
	
	public void connectAsServer(BluetoothDevice device) {
		  final BluetoothDevice mmDevice = device;
		 
		  BluetoothSocket tmp = null;
		  
		  try {
		   tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID); // Start a Service to create a socket that will allow the application to create the connection with the device wich identification us provided by MY_UUID. MY_UUID can be defined everywhere,in this case in the same BluetoothManager class, outside this function.
		  } catch (IOException e) {
		  }
		  mmSocket = tmp; // Finaly when pass the socket to the proper variable
		 
		  Log.d("BLUETOOTHMANAGER", "Waiting connection with HC-06");
		  connectionThread = new Thread(new Runnable() { // Create a new thread, beccuse "connect()" function that is used here is blocking, and if we implement it in the current UI thread, the application will be stopped until the connection is done.
		 
		   @Override
		   public void run() {
		    try {
		     mmSocket.connect(); // Connect to the device. This is the blocking operation
		    } catch (IOException ConnectEx) {
		     Log.d("BLUETOOTHMANAGER", "Error connecting to HC-06");
		     try {
		      mmSocket.close(); // If connection fails, close the socket
		     } catch (IOException closeEx) {
		     }
		     return;
		    }
		    try {
		     iStream = mmSocket.getInputStream(); // If the connection is made, instanciate input and output streams into declarated variables to read and write
		     oStream = mmSocket.getOutputStream();
		 
		    } catch (IOException e) {
		     e.printStackTrace();
		    }
		 
		   }
		  });
		  connectionThread.start(); // Start the previously defined thread.
		 
		 }
	
	
}