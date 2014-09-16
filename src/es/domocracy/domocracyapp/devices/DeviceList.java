package es.domocracy.domocracyapp.devices;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import es.domocracy.domocracyapp.R;

public class DeviceList extends BaseAdapter {
	// -----------------------------------------------------------------------------------
	// DeviceList's members
	private ListView mDeviceListView;
	private List<Device> mDeviceList;
	private Typeface mTypeface;

	// -----------------------------------------------------------------------------------
	// DeviceList basic interface
	public DeviceList(Activity _activity) {
		mTypeface = Typeface.createFromAsset(_activity.getAssets(),
				"multicolore.otf");

		mDeviceList = new ArrayList<Device>();

		initUI(_activity);
	}

	//-----------------------------------------------------------------------------------
	@Override
	public int getCount() {
		return mDeviceList.size();
	}

	//-----------------------------------------------------------------------------------
	@Override
	public Object getItem(int _position) {
		return mDeviceList.get(_position);
	}

	//-----------------------------------------------------------------------------------
	@Override
	public long getItemId(int _position) {
		return _position;
	}

	//-----------------------------------------------------------------------------------
	@Override
	public View getView(int _position, View _convertedView, ViewGroup _parent) {
		DeviceViewHolder deviceView = null;
		
		if(_convertedView == null){
			// If view is empty, create and fill it
			_convertedView = mDeviceList.get(_position).contructView(_parent.getContext());
			
			deviceView = new DeviceViewHolder();
			
			deviceView.mName = (TextView) _convertedView.findViewById(R.id.device_name);
			
			_convertedView.setTag(deviceView);			
		}else
			// If not, get the tag
			deviceView = (DeviceViewHolder) _convertedView.getTag();
		
		// Fill it with the provided info.
		deviceView.mName.setTypeface(mTypeface);
		deviceView.mName.setText(mDeviceList.get(_position).name());
		
		//if(mDevideList.get(_position).isExpanded())
		//	deviceView.mDescription.setVisibility(View.VISIBLE);
		//else
		//	deviceView.mDescription.setVisibility(View.GONE);
		
		
		return _convertedView;
	}
	
	// -----------------------------------------------------------------------------------
	// DeviceList private interface
	public void setDevices(List<Device> _list) {
		mDeviceList = _list;
		notifyDataSetChanged();
	}

	private class DeviceViewHolder {
		TextView mName;
	}

	// -----------------------------------------------------------------------------------
	private void initUI(Activity _activity) {
		mDeviceListView = (ListView) _activity
				.findViewById(R.id.device_list_view);

		mDeviceListView.setAdapter(this);

	}
}
