///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.comm;

import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

public class Hub {
	// -----------------------------------------------------------------------------------
	// Hub factory
	enum eConnectionTypes {
		eWifi, eBluetooth
	};
	
	

	// -----------------------------------------------------------------------------------
	// Hub information
	private String mName;
	private UUID mUuid;
	private eConnectionTypes mConnType;

	// Connection info
	// WIFI
	private InetAddress mAddr;
	private int mPort;

	// Bluetooth
	private String mDevName;

	// Auxiliary info
	private Date mLastConnection;

	// -----------------------------------------------------------------------------------
	// Hub basic interface.
	public String name() {
		return mName;

	}

	// -----------------------------------------------------------------------------------
	public UUID uuid() {
		return mUuid;

	}

	// -----------------------------------------------------------------------------------
	public eConnectionTypes connType() {
		return mConnType;
	}

	// -----------------------------------------------------------------------------------
	public InetAddress addr() {
		return mAddr;

	}

	// -----------------------------------------------------------------------------------
	public String devName(){
		return mDevName;
	}
	
	// -----------------------------------------------------------------------------------
	public int port() {
		return mPort;
	}

	// -----------------------------------------------------------------------------------
	public Date lastConnection() {
		return mLastConnection;

	}

	// -----------------------------------------------------------------------------------
	// Wifi hub constructor
	public Hub(String _name, UUID _uuid, InetAddress _addr, int _port) {
		mName = _name;
		mUuid = _uuid;
		
		mConnType = eConnectionTypes.eWifi;
		
		mAddr = _addr;
		mPort = _port;

	}
	
	// -----------------------------------------------------------------------------------
	// Bluetooth hub constructor
	public Hub(String _name, UUID _uuid, String _devName) {
		mName = _name;
		mUuid = _uuid;
		
		mConnType = eConnectionTypes.eBluetooth;
		
		mDevName = _devName;
	}

	// -----------------------------------------------------------------------------------
	public void updateLastConnection(Date _date) {
		mLastConnection = _date;

	}

	// -----------------------------------------------------------------------------------
	public String serializeHub() {
		return mUuid.toString() + " " + mName + " "
				+ mLastConnection.toString();

	}

}
