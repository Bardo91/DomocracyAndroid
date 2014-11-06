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

public class ConnectionLoader {

	// -----------------------------------------------------------------------------------
	// ConnectionLoader members
	static private HubConnection mCurrentConnection;
	private List<Hub> mHubList;
	private ServiceNSD mServiceDNS;
	
	// -----------------------------------------------------------------------------------
	// ConnectionLoader public interface
	public ConnectionLoader(Context _context) {
		mHubList = new ArrayList<Hub>();
		//mServiceDNS = new ServiceNSD(_context);		
	}
	
	// -----------------------------------------------------------------------------------
	public HubConnection connect(Context _context){
		// Call Service DNS to look for a new hub and addit to the top of the list
		//mHubList.add(0, mServiceDNS.getConnectionInfo());
		
		//------------------------------- 666 to test with pc
		//InetAddress addr = null;
		//try {
		//	addr = InetAddress.getByName("192.168.1.26");
		//} catch (Exception e) {
		//}
		
		//mHubList.add(new Hub("Rinoceronte", UUID.randomUUID(), addr , 5028));
		//------------------------------- 666 to test with pc
		
		// Connect to the newest Bluetooth hub
		mHubList.add(new Hub("Casa", UUID.randomUUID(), "HC-06"));
		mCurrentConnection = new HubConnectionBluetooth();
		mCurrentConnection.connectToHub(mHubList.get(0), _context);
		
		return mCurrentConnection;
	}
	
	// -----------------------------------------------------------------------------------
	public HubConnection currentConnection(){
		return mCurrentConnection;
	}
	
	// -----------------------------------------------------------------------------------	
	public void disconnect(Context _context){
		mCurrentConnection.closeConnection(_context);
	}
	
	// -----------------------------------------------------------------------------------
	
}
