///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;
import es.domocracy.domocracyapp.comm.HubConnection;
import es.domocracy.domocracyapp.comm.Message;
import es.domocracy.domocracyapp.comm.MessageDispatcher;
import es.domocracy.domocracyapp.comm.MessageListener;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceType;
import es.domocracy.domocracyapp.ui.Interface;

public class RoomManager {
	// -----------------------------------------------------------------------------------
	// RoomList members
	private List<Room> mRoomList;
	private Room mNewDeviceRoom;

	private HubConnection mCurrentConnection;
	private MessageListener mMessageListener;

	private Interface mInterface;

	// -----------------------------------------------------------------------------------
	// RoomList basic interface
	public RoomManager(HubConnection _hubConnection, Interface _interface) {
		mCurrentConnection = _hubConnection;

		mInterface = _interface;

		mRoomList = new ArrayList<Room>();
		mNewDeviceRoom = new Room((byte) 0x00, "New Device",
				new ArrayList<Device>());

		loadRooms();
		initMessageListener();
	}

	// -----------------------------------------------------------------------------------
	public void setDevices(int _room) {
		mInterface.updateDeviceList(mRoomList.get(_room).devices());
	}

	// -----------------------------------------------------------------------------------
	public void addRoom(Room _room) {
		mRoomList.add(_room);
		mInterface.updateRoomList(mRoomList);
	}

	// -----------------------------------------------------------------------------------------------------------------
	public List<Room> rooms() {
		return mRoomList;
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Private interface
	private void loadRooms() {

	}

	// ------------------------------------------------------------------------------------------------------------------
	private void initMessageListener() {
		// Defining messages supported by rooms.
		final byte[] supportedTypes = { Message.Type.NewDevice.value,
				Message.Type.RoomListInfo.value, Message.Type.InfoDevice.value };

		// Init message listener
		mMessageListener = new MessageListener(supportedTypes) {

			@Override
			public void onMessage(final Message _message) {
				Log.d("DMC", "UserRooms received a message of type: " + _message.type());
				if (_message.type() == Message.Type.InfoDevice.value) {
					if (0 == mNewDeviceRoom.devices().size()) {
						mRoomList.add(0, mNewDeviceRoom);
					}
					
					mNewDeviceRoom.addDevice(Device.getDevice(	_message.payload()[1],
																new String(Arrays.copyOfRange(_message.payload(),1 ,_message.payload().length)),
																new DeviceType(), null, mCurrentConnection));
					
					mInterface.updateRoomList(mRoomList);
				}
			}
		};

		MessageDispatcher.registerListener(mMessageListener);
	}
	// -----------------------------------------------------------------------------------
}
