package es.domocracy.domocracyapp.ui;

import java.util.List;

import android.app.Activity;
import es.domocracy.domocracyapp.devices.Device;

public class MainScreen {
	//-----------------------------------------------------------------------------------------------------------------
	// Class members
	private RoomHeader mHeader;
	private RoomContent mDevices;
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public interface
	public MainScreen(Activity _activity){
		initDeviceList(_activity);
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void setDevices(List<Device> _list){
		mDevices.setDevices(_list);
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Private Interface	
	private void initDeviceList(Activity _activity){
		mDevices = new RoomContent(_activity);
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
}
