package es.domocracy.domocracyapp.comm.compatibility;

import java.util.List;

import android.util.Log;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueParsingError;

public class HueDriver {
	//-----------------------------------------------------------------------------------------------------------------
	// Class members
	static private PHHueSDK mHueSDK = null;   
	static private HueDriver mInstance = null;
	//-----------------------------------------------------------------------------------------------------------------
	// static interface	
	//-----------------------------------------------------------------------------------------------------------------
	static public HueDriver get(){
		if(mInstance == null){
			mInstance = new HueDriver();
			init();
		}
		
		return mInstance;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	private static void init(){
		mHueSDK = PHHueSDK.create();
		mHueSDK.setAppName("Domocracy");
		mHueSDK.setDeviceName(android.os.Build.MODEL);
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public interface	
	//-----------------------------------------------------------------------------------------------------------------
	public void searchBridge(){
		if(mHueSDK == null){
			init();
		}
		
		mHueSDK.getNotificationManager().registerSDKListener(mHueListener);
		PHBridgeSearchManager sm = (PHBridgeSearchManager) mHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
		sm.search(true, true); 
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	
	
	//-----------------------------------------------------------------------------------------------------------------
	// Private interface	
	//-----------------------------------------------------------------------------------------------------------------
	private PHSDKListener mHueListener = new PHSDKListener() {
		
		@Override
		public void onParsingErrors(List<PHHueParsingError> arg0) {
			Log.d("DMC-HUE", "Parsed Error");
		}
		
		@Override
		public void onError(int arg0, String arg1) {
			Log.d("DMC-HUE", "Error");
		}
		
		@Override
		public void onConnectionResumed(PHBridge arg0) {
			Log.d("DMC-HUE", "Connection Resumed");
		}
		
		@Override
		public void onConnectionLost(PHAccessPoint arg0) {
			Log.d("DMC-HUE", "Connection Lost");
		}
		
		@Override
		public void onCacheUpdated(List<Integer> arg0, PHBridge arg1) {
			Log.d("DMC-HUE", "Cache Updated");
		}
		
		@Override
		public void onBridgeConnected(PHBridge arg0) {
			Log.d("DMC-HUE", "Bridge Connected");
		}
		
		@Override
		public void onAuthenticationRequired(PHAccessPoint arg0) {
			Log.d("DMC-HUE", "Authentication Required");
		}
		
		@Override
		public void onAccessPointsFound(List<PHAccessPoint> arg0) {
			Log.d("DMC-HUE", "Access Points Found");
		}
	};
	
	
	//-----------------------------------------------------------------------------------------------------------------
	
	
}
