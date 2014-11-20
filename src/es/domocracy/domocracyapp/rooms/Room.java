///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.rooms;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import es.domocracy.domocracyapp.R;
import es.domocracy.domocracyapp.devices.Device;

public class Room {
	// -----------------------------------------------------------------------------------
	// Room members
	private byte mId;
	private String mName;
	private List<Device> mDevices;
	private View mRoomView;

	// -----------------------------------------------------------------------------------
	// Room basic interface
	public Room(byte _id, String _name, List<Device> _devices) {
		mId = _id;
		mName = _name;
		mDevices = _devices;
	}

	// -----------------------------------------------------------------------------------
	public String name() {
		return mName;
	}

	// -----------------------------------------------------------------------------------
	public byte id() {
		return mId;
	}

	// -----------------------------------------------------------------------------------
	public void addDevice(Device _device){
		if(!mDevices.contains(_device)){
			mDevices.add(_device);
		}
		
	}
	
	// -----------------------------------------------------------------------------------
	public void eraseDevice(int _position){
		assert(mDevices.size() < _position);	//	Bad object position
		mDevices.remove(_position);
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
