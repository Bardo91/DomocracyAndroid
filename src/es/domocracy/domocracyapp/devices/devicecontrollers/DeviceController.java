package es.domocracy.domocracyapp.devices.devicecontrollers;

import android.content.Context;
import android.view.View;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceState;

public abstract class DeviceController{
	// -----------------------------------------------------------------------------------
	// DeviceControl members

	protected Device mOwner;
	protected View mControllerView;
	

	// -----------------------------------------------------------------------------------
	//DeviceControl basic interface
	public DeviceController(Device _owner){
		mOwner = _owner;
		
	}
	
	public abstract View getView(Context _context);
	public abstract void updateControllerState(DeviceState _state);				// Function to change the representation of the device state inside de controller
	
	// -----------------------------------------------------------------------------------
	// DeviceControl private interface

	protected abstract void sendInstruction();									// Function to modify the state of the owner (The device).
	
}
