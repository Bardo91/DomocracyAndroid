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
import android.graphics.Typeface;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import es.domocracy.domocracyapp.R;
import es.domocracy.domocracyapp.comm.HubConnection;
import es.domocracy.domocracyapp.comm.Message;
import es.domocracy.domocracyapp.comm.MessageDispatcher;
import es.domocracy.domocracyapp.comm.MessageListener;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceList;
import es.domocracy.domocracyapp.devices.DeviceType;

public class UserRooms extends BaseAdapter {
	// -----------------------------------------------------------------------------------
	// RoomList members
	private ListView mRoomListView;
	private DrawerLayout mRoomDrawer;
	private List<Room> mRoomList;

	private Typeface mTypeface;

	private DeviceList mDeviceList;

	private HubConnection mCurrentConnection;
	private Activity mActivity;
	private MessageListener mMessageListener;

	// -----------------------------------------------------------------------------------
	// RoomList basic interface
	public UserRooms(Activity _activity, DeviceList _deviceList,
			HubConnection _hubConnection) {
		mActivity = _activity;
		mTypeface = Typeface.createFromAsset(_activity.getAssets(),
				"multicolore.otf");

		mCurrentConnection = _hubConnection;
		mDeviceList = _deviceList;

		mRoomList = new ArrayList<Room>();
		initUI(_activity);
		initMessageListener();
	}

	// -----------------------------------------------------------------------------------
	public void fillRoomList(List<Room> _rooms) {
		mRoomList = _rooms;
		notifyDataSetChanged();
	}

	// -----------------------------------------------------------------------------------
	public void setDevices(int _room) {
		mDeviceList.setDevices(devices(_room));
	}

	// -----------------------------------------------------------------------------------
	public void addRoom(Room _room) {
		mRoomList.add(_room);
		notifyDataSetChanged();
	}

	// -----------------------------------------------------------------------------------
	public List<Device> devices(int _room) {
		return mRoomList.get(_room).devices();
	}

	// -----------------------------------------------------------------------------------
	@Override
	public int getCount() {
		return mRoomList.size();
	}

	// -----------------------------------------------------------------------------------
	@Override
	public Object getItem(int _position) {
		return mRoomList.get(_position);
	}

	// -----------------------------------------------------------------------------------
	@Override
	public long getItemId(int _position) {
		return _position;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public View getView(int _position, View _convertedView, ViewGroup _parent) {
		RoomViewHolder roomView = null;

		if (_convertedView == null) {
			// If view is empty, create and fill it
			_convertedView = mRoomList.get(_position).contructView(
					_parent.getContext());

			roomView = new RoomViewHolder();

			roomView.mName = (TextView) _convertedView
					.findViewById(R.id.room_name);

			_convertedView.setTag(roomView);
		}

		else
			// If not, get the tag
			roomView = (RoomViewHolder) _convertedView.getTag();

		// Fill it with the provided info.
		roomView.mName.setTypeface(mTypeface);
		roomView.mName.setText(mRoomList.get(_position).name());

		return _convertedView;
	}

	// -----------------------------------------------------------------------------------
	// RoomList private interface

	private class RoomViewHolder {
		TextView mName;
	}

	// -----------------------------------------------------------------------------------
	private void initUI(Activity _activity) {
		initDrawer(_activity);
		initActionBar(_activity);

	}

	//-----------------------------------------------------------------------------------------------------------------
	private void initMessageListener() {
		// Defining messages supported by rooms.
		final byte[] supportedTypes = { Message.Type.NewDevice.value,
										Message.Type.RoomListInfo.value,
										Message.Type.InfoDevice.value};
		
		// Init message listener
		mMessageListener = new MessageListener(supportedTypes) {

			@Override
			public void onMessage(final Message _message) {
				// 666 TODO: do whatelse with messages.
				Log.d("DMC-DEBUG", "UserRooms received a message of type: " + _message.type());
				
				if(_message.type() == Message.Type.InfoDevice.value){
					mRoomList.get(0).addDevice(Device.getDevice(	_message.payload()[0], 
																	new String(Arrays.copyOfRange(_message.payload(), 1, _message.payload().length)), 
																	new DeviceType(), 
																	null, 
																	mCurrentConnection));
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mDeviceList.notifyDataSetChanged();
							Toast.makeText(	mActivity, 
											"Added new Device" + new String(Arrays.copyOfRange(_message.payload(), 1, _message.payload().length)), 
											Toast.LENGTH_SHORT).show();
						}
					});
				}	
			}
		};
		
		MessageDispatcher.registerListener(mMessageListener);	
	}

	//-----------------------------------------------------------------------------------------------------------------
	private void initDrawer(Activity _activity) {
		// Get the instances of the UI views
		mRoomDrawer = (DrawerLayout) _activity.findViewById(R.id.drawer_layout);

		mRoomListView = (ListView) _activity.findViewById(R.id.left_drawer);

		mRoomListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> _parent, View _view,
					int _position, long _id) {
				setDevices(_position - 1);
				mRoomDrawer.closeDrawer(mRoomListView);
			}
		});

		initDrawerHeader(_activity);

		mRoomListView.setAdapter(this);

	}

	// -----------------------------------------------------------------------------------
	private void initDrawerHeader(Activity _activity) {
		mRoomListView.addHeaderView(_activity.getLayoutInflater().inflate(
				R.layout.navigation_header, null));

		TextView headRooms = (TextView) _activity.findViewById(R.id.head_rooms);
		headRooms.setTypeface(mTypeface);
		headRooms.setText("Rooms");

		Button addRoomBt = (Button) _activity
				.findViewById(R.id.button_add_room);

		addRoomBt.setTypeface(Typeface.createFromAsset(_activity.getAssets(),
				"multicolore.otf"));

		addRoomBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

	}

	// -----------------------------------------------------------------------------------
	private void initActionBar(Activity _activity) {
		_activity.getActionBar().setDisplayHomeAsUpEnabled(true);
		_activity.getActionBar().setHomeButtonEnabled(true);

	}

	// -----------------------------------------------------------------------------------
}
