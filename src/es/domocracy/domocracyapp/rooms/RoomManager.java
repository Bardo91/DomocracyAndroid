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

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import es.domocracy.domocracyapp.comm.HubConnection;
import es.domocracy.domocracyapp.comm.Message;
import es.domocracy.domocracyapp.comm.MessageDispatcher;
import es.domocracy.domocracyapp.comm.MessageListener;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceList;
import es.domocracy.domocracyapp.devices.DeviceType;

public class RoomManager {
	// -----------------------------------------------------------------------------------
	// RoomList members
	private List<Room> mRoomList;

	private DeviceList mDeviceList;

	private HubConnection mCurrentConnection;
	private Activity mActivity;
	private MessageListener mMessageListener;

	// -----------------------------------------------------------------------------------
	// RoomList basic interface
	public RoomManager(Activity _activity, HubConnection _hubConnection) {
		mActivity = _activity;
		
		mCurrentConnection = _hubConnection;
		mDeviceList = new DeviceList(_activity);

		mRoomList = new ArrayList<Room>();

		initMessageListener();
	}

	// Cosas que he quitado y hay que hacer de otra forma:
	//		--> FillRoomList(), ahora se llenara ella sola accediendo al archivo que sea necesario
	
	// -----------------------------------------------------------------------------------
	public void setDevices(int _room) {
		mDeviceList.setDevices(mRoomList.get(_room).devices());
	}

	// -----------------------------------------------------------------------------------
	public void addRoom(Room _room) {
		mRoomList.add(_room);
	}

	// -----------------------------------------------------------------------------------------------------------------
	private void initMessageListener() {
		// Defining messages supported by rooms.
		final byte[] supportedTypes = { Message.Type.NewDevice.value,
										Message.Type.RoomListInfo.value, 
										Message.Type.InfoDevice.value };

		// Init message listener
		mMessageListener = new MessageListener(supportedTypes) {

			@Override
			public void onMessage(final Message _message) {
				Log.d("DMC", "UserRooms received a message of type: " + _message.type());
				if (_message.type() == Message.Type.InfoDevice.value) {
					mRoomList.get(0).addDevice(Device.getDevice(	_message.payload()[1],
																	new String(Arrays.copyOfRange(_message.payload(), 1, _message.payload().length)),
																	new DeviceType(), 
																	null,
																	mCurrentConnection));
					
					Log.d("DMC", "Added new device to room");
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mDeviceList.notifyDataSetChanged();
							Toast.makeText(mActivity, "Added new Device" + new String(Arrays.copyOfRange(_message.payload(), 1, _message.payload().length)), Toast.LENGTH_SHORT).show();
							Log.d("DMC", "Update UI");
						}
					});
				}
			}
		};

		MessageDispatcher.registerListener(mMessageListener);
	}

	// -----------------------------------------------------------------------------------
	private void initActionBar(Activity _activity) {		// 666 TODO: hay que moverlo a la clase q contenga el action bar, que no tienen nombre aún 
		_activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		_activity.getActionBar().setHomeButtonEnabled(true);

	}

	// -----------------------------------------------------------------------------------
}
