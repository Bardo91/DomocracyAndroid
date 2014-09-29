package es.domocracy.domocracyapp.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import es.domocracy.domocracyapp.R;
import es.domocracy.domocracyapp.rooms.Room;

public class SlideMenu extends BaseAdapter {
	// -----------------------------------------------------------------------------------------------------------------
	// Class members
	private Activity mActivity;
	private ListView mRoomListView;
	private DrawerLayout mRoomDrawer;
	private ActionBarDrawerToggle mActionBarToggle;

	private List<Room> mRoomList;

	private MainScreen mMainScreen;

	private Typeface mTypeface;

	// -----------------------------------------------------------------------------------------------------------------
	// Public interface
	public SlideMenu(Activity _activity, MainScreen _mainScreen) {
		mActivity = _activity;
		mMainScreen = _mainScreen;

		mTypeface = Typeface.createFromAsset(_activity.getAssets(),
				"multicolore.otf");

		mRoomList = new ArrayList<Room>();

		initDrawer(_activity);
		initRoomList(_activity);
		initDrawerHeader(_activity);

		mRoomListView.setAdapter(this);
	}

	// -----------------------------------------------------------------------------------------------------------------
	public void setRooms(List<Room> _list) {
		mRoomList = _list;
		requestUiUpdate();
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	public boolean isClickedSlideMenu(MenuItem _item){
		return mActionBarToggle.onOptionsItemSelected(_item);
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	// Base adapter implementation
	@Override
	public int getCount() {
		return mRoomList.size();
	}

	// ----------------------------------------------------------------------------------------------------------------
	@Override
	public Object getItem(int _position) {
		return mRoomList.get(_position);
	}

	// ----------------------------------------------------------------------------------------------------------------
	@Override
	public long getItemId(int _position) {
		return _position;
	}

	// ----------------------------------------------------------------------------------------------------------------
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

	// ----------------------------------------------------------------------------------------------------------------
	// RoomList private interface

	private class RoomViewHolder {
		TextView mName;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Private Interface
	private void initDrawer(Activity _activity) {
		// Get the instances of the UI views
		mRoomDrawer = (DrawerLayout) _activity.findViewById(R.id.drawer_layout);
		mActionBarToggle = new ActionBarDrawerToggle(	_activity, 
														mRoomDrawer,
														R.drawable.ic_launcher, 
														R.string.drawer_open,
														R.string.drawer_close); // 666 TODO: implement onClose & onOpen
																				// to change title when closed if needed

		mRoomDrawer.setDrawerListener(mActionBarToggle);
	}
	
	// ----------------------------------------------------------------------------------------------------------------
	private void initRoomList(Activity _activity){
		mRoomListView = (ListView) _activity.findViewById(R.id.left_drawer);

		mRoomListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> _parent, View _view,
					int _position, long _id) {
				mMainScreen.setDevices(mRoomList.get(_position - 1).devices());
				mRoomDrawer.closeDrawer(mRoomListView);
			}
		});
	}
	
	// ----------------------------------------------------------------------------------------------------------------
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

	// ----------------------------------------------------------------------------------------------------------------
	private void requestUiUpdate() {
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}
}
