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
import es.domocracy.domocracyapp.comm.compatibility.HueConnection;

public class ConnectionManager {

	// -----------------------------------------------------------------------------------
	// ConnectionLoader members
	private HubConnection mCurrentConnection;
	private HueConnection mHueDriver;
	private List<Hub> mHubList;
	private ServiceNSD mServiceDNS;

	
	// -----------------------------------------------------------------------------------
	// ConnectionLoader public interface
	public ConnectionManager(Context _context) {
		mHubList = new ArrayList<Hub>();	
		initDrivers(_context);
	}
	
	// -----------------------------------------------------------------------------------
	public HubConnection connect(Context _context){
		
		if(mCurrentConnection == null)
			return null;
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
		//mCurrentConnection = new HubConnectionBluetooth();
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
	public void initDrivers(Context _context){
		// initNSD(_context);
		mServiceDNS = new ServiceNSD(_context);
		
		// Init bluetooth
		HubConnectionBluetooth.initBluetooth(_context);
		mCurrentConnection = new HubConnectionBluetooth();
	}
	
	// -----------------------------------------------------------------------------------
	public void unloadDrivers(Context _context){
		HubConnectionBluetooth.unloadBluetooth(_context);
	}
}
