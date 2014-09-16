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
	// Hub information
	private String mName;
	private Date mLastConnection;
	private UUID mUuid;
	private InetAddress mAddr;
	private int mPort;

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
	public InetAddress addr() {
		return mAddr;

	}

	// -----------------------------------------------------------------------------------
	public int port(){
		return mPort;
	}
	
	// -----------------------------------------------------------------------------------
	public Date lastConnection() {
		return mLastConnection;

	}

	// -----------------------------------------------------------------------------------
	public Hub(String _name, UUID _uuid, InetAddress _addr, int _port) {
		mName = _name;
		mUuid = _uuid;
		mAddr = _addr;
		mPort = _port;

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
