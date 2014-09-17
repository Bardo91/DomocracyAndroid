///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.rooms;

import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import es.domocracy.domocracyapp.R;
import es.domocracy.domocracyapp.devices.Device;

public class Room {
	// -----------------------------------------------------------------------------------
	// Room members
	private UUID mUUID;
	private String mName;
	private List<Device> mDevices;
	private View mRoomView;

	// -----------------------------------------------------------------------------------
	// Room basic interface
	public Room(UUID _uuid, String _name, List<Device> _devices) {
		mUUID = _uuid;
		mName = _name;
		mDevices = _devices;
	}

	// -----------------------------------------------------------------------------------
	public String name() {
		return mName;
	}

	// -----------------------------------------------------------------------------------
	public UUID UUID() {
		return mUUID;
	}

	// -----------------------------------------------------------------------------------
	public void addDevice(Device _device){
		if(!mDevices.contains(_device)){
			mDevices.add(_device);
		}
		
	}
	
	// -----------------------------------------------------------------------------------
	public List<Device> devices() {
		return mDevices;
	}

	// -----------------------------------------------------------------------------------
	public View contructView(Context _context) {
		if (mRoomView == null) {
			LayoutInflater inflater = LayoutInflater.from(_context);
			mRoomView = inflater.inflate(R.layout.room_layout, null);
		}

		return mRoomView;
	}

	// -----------------------------------------------------------------------------------
	// Room private interface

	// -----------------------------------------------------------------------------------
}
