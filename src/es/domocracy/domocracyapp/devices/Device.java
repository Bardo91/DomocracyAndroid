package es.domocracy.domocracyapp.devices;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import es.domocracy.domocracyapp.R;
import es.domocracy.domocracyapp.comm.HubConnection;
import es.domocracy.domocracyapp.comm.Message;
import es.domocracy.domocracyapp.devices.devicecontrollers.DeviceController;

public class Device {
	// -----------------------------------------------------------------------------------
	// Factory

	static private HashMap<Byte, Device> existingDevices =  new HashMap<Byte, Device>();

	// -----------------------------------------------------------------------------------
	static public Device getDevice(byte _uuid, String _name,
			DeviceType _type, DeviceState _state, HubConnection _hubConnection) {
		// If dev exist, get it.
		if (existingDevices.containsKey(_uuid)) {
			return existingDevices.get(_uuid);
		}
		// If not, create a new device and store it
		Device dev = new Device(_uuid, _name, _type, _state, _hubConnection);
		existingDevices.put(_uuid, dev);
		return dev;
	}

	// -----------------------------------------------------------------------------------
	// Device's members
	private byte mUUID;
	private String mName;
	private DeviceState mState;
	private List<DeviceController> mControllers;
	private View mDeviceView;
	private HubConnection mCurrentConnection;

	// -----------------------------------------------------------------------------------
	// Device basic interface
	
	public byte UUID() {
		return mUUID;
	}

	// -----------------------------------------------------------------------------------
	public String name() {
		return mName;
	}

	// -----------------------------------------------------------------------------------
	public DeviceState state() {
		return mState;
	}

	// -----------------------------------------------------------------------------------
	public View contructView(Context _context) {
		if (mDeviceView == null) {
			LayoutInflater inflater = LayoutInflater.from(_context);
			mDeviceView = inflater.inflate(R.layout.device_layout, null);
			
			for (DeviceController controller : mControllers) {
				((LinearLayout) mDeviceView.findViewById(R.id.device_layout))
						.addView(controller.getView(_context));
			}
		}
		return mDeviceView;
	}
	
	// -----------------------------------------------------------------------------------
	public void sendInstruction(Message _msg){
		mCurrentConnection.sendMsg(_msg);
		
	}
	
	// -----------------------------------------------------------------------------------
	// Device private interface
	private Device(byte _uuid, String _name, DeviceType _type, DeviceState _state, HubConnection _hubConnection) {
		mUUID = _uuid;
		mName = _name;
		mState = _state;
		mControllers = _type.buildControllers(this);
		mCurrentConnection = _hubConnection;
		mState = new DeviceState();

	}

	
}
