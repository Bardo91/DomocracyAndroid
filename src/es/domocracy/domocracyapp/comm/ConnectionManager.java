///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.comm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;

public class ConnectionManager {

	// ----------------------------------------------------------------------------------------------------------------
	// ConnectionLoader members
	private HubConnection mBluetoothConnection;
	private HubConnection mWifiConnection;
	private HueConnection mHueConnection;
	private List<Hub> mHubList;
	private ServiceNSD mServiceDNS;

	public enum eConnectionTypes {
		eWifi, eBluetooth, eHue
	};

	// ----------------------------------------------------------------------------------------------------------------
	// ConnectionLoader public interface
	public ConnectionManager(Context _context) {
		mHubList = new ArrayList<Hub>();
		initDrivers(_context);

		mHueConnection = new HueConnection();
	}

	// ----------------------------------------------------------------------------------------------------------------
	public HubConnection connect(Context _context, eConnectionTypes _type) {

		
		switch (_type) {
		case eWifi:
			connectWifi(_context);
		case eBluetooth:
			connectBluetooth(_context);
		case eHue:
			connectHue(_context);
		default:
			assert(false);	//	666 TODO: not supported connection				
		}
		
		return mCurrentConnection;
	}

	// ----------------------------------------------------------------------------------------------------------------
	public HubConnection currentConnection() {
		return mCurrentConnection;
	}

	// ----------------------------------------------------------------------------------------------------------------
	public HueConnection hueConnection() {
		return mHueConnection;
	}

	// ----------------------------------------------------------------------------------------------------------------
	public void initDrivers(Context _context) {
		// initNSD(_context);
		mServiceDNS = new ServiceNSD(_context);

		// Init bluetooth
		HubConnectionBluetooth.initBluetooth(_context);

		// Init Hue driver
		HueConnection.init();
	}

	// ----------------------------------------------------------------------------------------------------------------
	public void unloadDrivers(Context _context) {
		HubConnectionBluetooth.unloadBluetooth(_context);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Private Interface of Connection Manager
	// ----------------------------------------------------------------------------------------------------------------
	private void connectWifi(Context _context) {
		if (mWifiConnection == null)
			mWifiConnection = new HubConnectionWifi();
		
		// Call Service DNS to look for a new hub and addit to the top of the
		// list
		mHubList.add(0, mServiceDNS.getConnectionInfo());

		// ------------------------------- 666 to test with pc
		// InetAddress addr = null;
		// try {
		// addr = InetAddress.getByName("192.168.1.26");
		// } catch (Exception e) {
		// }

		// mHubList.add(new Hub("Rinoceronte", UUID.randomUUID(), addr , 5028));
		// ------------------------------- 666 to test with pc


	}

	// ----------------------------------------------------------------------------------------------------------------
	private void connectBluetooth(Context _context) {
		if(mBluetoothConnection == null)
			mBluetoothConnection = new HubConnectionBluetooth();
		
		// Connect to the newest Bluetooth hub
		mHubList.add(new Hub("Casa", UUID.randomUUID(), "HC-06"));
		// mCurrentConnection = new HubConnectionBluetooth();
		mBluetoothConnection.connectToHub(mHubList.get(0), _context);

	}
	
	// ---------------------------------------------------------------------------------------------------------------
	private void connectHue(Context _context) {

	}

}
