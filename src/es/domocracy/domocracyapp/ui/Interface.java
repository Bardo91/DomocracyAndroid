package es.domocracy.domocracyapp.ui;

import java.util.List;

import android.app.Activity;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.rooms.Room;

public class Interface {
	//-----------------------------------------------------------------------------------------------------------------
	// Class members
	private SlideMenu mSlideMenu;
	private MainScreen mMainScreen;
	private TopMenu mTopMenu;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public interface
	public Interface(Activity _activity){
		mMainScreen = new MainScreen(_activity);
		mSlideMenu = new SlideMenu(_activity, mMainScreen);
		mTopMenu = new TopMenu(_activity);
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public MainScreen mainScreen(){
		return mMainScreen;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public SlideMenu slideMenu(){
		return mSlideMenu;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public TopMenu topMenu(){
		return mTopMenu;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void updateDeviceList(List<Device> _deviceList){
		mMainScreen.setDevices(_deviceList);
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void updateRoomList(List<Room> _roomList){
		mSlideMenu.setRooms(_roomList);
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Private Interface
	
	//-----------------------------------------------------------------------------------------------------------------
}
