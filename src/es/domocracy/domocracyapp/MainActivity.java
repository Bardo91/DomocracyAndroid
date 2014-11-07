///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import es.domocracy.domocracyapp.comm.ConnectionLoader;
import es.domocracy.domocracyapp.comm.HubConnection;
import es.domocracy.domocracyapp.comm.Message;
import es.domocracy.domocracyapp.comm.MessageDispatcher;
import es.domocracy.domocracyapp.database.InfoCollector;
import es.domocracy.domocracyapp.rooms.RoomManager;

public class MainActivity extends ActionBarActivity {

	// -----------------------------------------------------------------------------------
	// Main's activity members
	private ConnectionLoader mConnectionLoader;
	private HubConnection mHubConnection;

	private RoomManager mRooms;

	// -----------------------------------------------------------------------------------
	// MainActivity interface
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set UI pattern by XML
		setContentView(R.layout.activity_main);

		initConnection();	// Prepare connections
		initInterface();	// Load and fill the UI content
		
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onStop() {
		super.onStop();
		mHubConnection.closeConnection(this);
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onStart() {
		super.onStart();
		mConnectionLoader.connect(this);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		mConnectionLoader.unloadDrivers(this);
	}
	
	// -----------------------------------------------------------------------------------
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if (id == R.id.add_device_settings) {
			// 666 TODO: where to gather functions that are related to the system.
			mHubConnection.sendMsg(new Message(	(byte) 0x02, 
													Message.Type.Look4Devices.value, 
													new byte[0]));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	// -----------------------------------------------------------------------------------
	// Private Interface
	void initConnection(){
		mConnectionLoader = new ConnectionLoader(this);
		mConnectionLoader.initDrivers(this);
		
		mHubConnection = mConnectionLoader.currentConnection();

		// Init MessageDispatcher
		MessageDispatcher.init(mHubConnection);

	}

	// -----------------------------------------------------------------------------------
	void initInterface(){
		mRooms = new RoomManager(this, mHubConnection);
		
		mRooms.fillRoomList(InfoCollector.getRooms(mHubConnection));
		mRooms.setDevices(0); // 666 TODO Set default room or last room, or so
	}
	
}
