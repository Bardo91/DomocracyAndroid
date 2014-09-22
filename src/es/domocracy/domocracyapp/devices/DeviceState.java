package es.domocracy.domocracyapp.devices;

public class DeviceState {
	// At the moment this class only holds a byte that is used for dimmer and switch devices.
	//-----------------------------------------------------------------------------------------------------------------
	// DeviceState's members
	private static final byte ON_THRESHOLD = (byte) 0xC8;
	
	
	private byte mState;
	
	//-----------------------------------------------------------------------------------------------------------------
	// DeviceState's public interface
	public DeviceState(){
		mState = 0;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public boolean isOn(){
		return mState >= ON_THRESHOLD ? true : false;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public boolean isOff(){
		return mState < ON_THRESHOLD ? true : false;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void setOn(){
		mState = (byte) 0xFF;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void setOff(){
		mState = (byte) 0x00;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public byte getDimmValue(){
		return mState;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void setDimmValue(byte _val){
		mState = _val;
	}
	
	
}
