package es.domocracy.domocracyapp.comm;

import android.bluetooth.BluetoothAdapter;

public class HubConnectionBluetooth extends HubConnection {
	// -----------------------------------------------------------------------------------
	// Static bluetooth adapter initialization
	static private BluetoothAdapter mAdapter;
	
	static private void initBluetooth(){
		// Get default bluetooth device.
		if (mAdapter == null){
			mAdapter = BluetoothAdapter.getDefaultAdapter();
			assert(mAdapter != null);
		}
		
		// If bluetooth is not enabled, tell the system to show start bluetooth menu
		if(!mAdapter.isEnabled()){
			mAdapter.enable();
		}
		
	}
	
	// -----------------------------------------------------------------------------------
	// HubConnection
	protected final int TIMEOUT = 2000;
	
	
	public boolean connectToHub(final Hub _hub) {
		initBluetooth();

		
		return mIsConnected;
	}
}