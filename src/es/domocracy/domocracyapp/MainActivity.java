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
import es.domocracy.domocracyapp.comm.ConnectionManager;
import es.domocracy.domocracyapp.comm.MessageDispatcher;
import es.domocracy.domocracyapp.rooms.RoomManager;
import es.domocracy.domocracyapp.ui.Interface;

public class MainActivity extends ActionBarActivity {

	// -----------------------------------------------------------------------------------
	// Main's activity members
	private ConnectionManager mConnectionManager;

	private RoomManager mRooms;
	private Interface mInterface;
// 	-----------------------------------------------------------------------------------
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
		mConnectionManager.closeConnections(this);
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		mConnectionManager.unloadDrivers(this);
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
			// 666 TODO: deprecated methods
			//mHubConnection.sendMsg(new Message(	(byte) 0x02, 
			//										Message.Type.Look4Devices.value, 
			//										new byte[0]));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	// -----------------------------------------------------------------------------------
	// Private Interface
	private void initConnection(){
		mConnectionManager = new ConnectionManager(this);
		// Init MessageDispatcher
		MessageDispatcher.init(mConnectionManager);
		
	}

	// -----------------------------------------------------------------------------------
	void initInterface(){
		mInterface = new Interface(this);
		
		mRooms = new RoomManager(mConnectionManager, mInterface);
		
	}
	
}
