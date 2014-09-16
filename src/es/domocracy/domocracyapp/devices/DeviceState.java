package es.domocracy.domocracyapp.devices;

public class DeviceState {
	// At the moment this class only holds a byte that is used for dimmer and switch devices.
	//-----------------------------------------------------------------------------------------------------------------
	// DeviceState's members

	private byte mState;
	
	//-----------------------------------------------------------------------------------------------------------------
	// DeviceState's public interface
	public DeviceState(){
		mState = 0;
	}
	
	//-----------------------------------------------------------------------------------------------------------------

	public byte state(){
		return mState;
	}
	
	//-----------------------------------------------------------------------------------------------------------------

	public void setState(byte _state){
		mState = _state;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// DeviceState's private interface
	
	
	
}
