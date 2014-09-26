package es.domocracy.domocracyapp.ui;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.rooms.Room;

public class Interface {
	//-----------------------------------------------------------------------------------------------------------------
		// Class members
		private SlideMenu mRoomList;
		private MainScreen mMainScreen;
		private ActionBar mTopMenu;
		
		//-----------------------------------------------------------------------------------------------------------------
		// Public interface
		public Interface(Activity _activity){
			mRoomList = new SlideMenu(_activity);
			mMainScreen = new MainScreen(_activity);
			
			initActionBar(_activity);
			
		}
		
		//-----------------------------------------------------------------------------------------------------------------
		public void updateDeviceList(List<Device> _deviceList){
			mMainScreen.setDevices(_deviceList);
			
		}
		
		//-----------------------------------------------------------------------------------------------------------------
		public void updateRoomList(List<Room> _roomList){
			mRoomList.setRooms(_roomList);
		}
		
		//-----------------------------------------------------------------------------------------------------------------
		
		
		//-----------------------------------------------------------------------------------------------------------------
		// Private Interface	
		private void initActionBar(Activity _activity){
			mTopMenu = _activity.getActionBar();
			
			mTopMenu.setDisplayHomeAsUpEnabled(true);
			mTopMenu.setHomeButtonEnabled(true);
		}
		
		//-----------------------------------------------------------------------------------------------------------------
}
