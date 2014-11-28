package es.domocracy.domocracyapp.comm;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;

public class HubConnectionHue implements HubConnection{
	//-----------------------------------------------------------------------------------------------------------------
	//Static interface	
	//-----------------------------------------------------------------------------------------------------------------
	static private PHHueSDK mHueSDK = null;   
	
	public static void init(){
		mHueSDK = PHHueSDK.create();
		mHueSDK.setAppName("Domocracy");
		mHueSDK.setDeviceName(android.os.Build.MODEL);
		
		Log.d("DMC-HUE", "Hue's SDK initialized");
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Class members	
	//-----------------------------------------------------------------------------------------------------------------
	private PHBridge mBridge = null;
	private List<PHLight> mLightList = null;
	
	
	//-----------------------------------------------------------------------------------------------------------------
	// Public interface	
	//-----------------------------------------------------------------------------------------------------------------
	public boolean isConnected(){
		return mBridge != null;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	@Override
	public boolean connectToHub(Hub _hub, Context _context) {
		searchBridgeLan();
		// 666 TODO: timeout?
		return true;
	}

	//-----------------------------------------------------------------------------------------------------------------
	@Override
	public boolean closeConnection(Context _context) {
		if (mHueSDK.isHeartbeatEnabled(mBridge)) {
        	mHueSDK.disableHeartbeat(mBridge);
        }
        mHueSDK.disconnect(mBridge);
        Log.d("DMC-HUE", "Unloaded Hue's SDK");
        return true;
	}

	//-----------------------------------------------------------------------------------------------------------------
	@Override
	public boolean sendMsg(Message _msg) {
		// 666 TODO: decode msg.
		Random random = new Random();
		changeLight(1, random.nextInt(360), 100);
		return true;
	}

	//-----------------------------------------------------------------------------------------------------------------
	@Override
	public Message readBuffer() {
		// TODO... read messages from cache or so on.
		return null;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	// Private interface	
	//-----------------------------------------------------------------------------------------------------------------
	private void searchBridgeLan(){
		mHueSDK.getNotificationManager().registerSDKListener(mHueListener);
		PHBridgeSearchManager sm = (PHBridgeSearchManager) mHueSDK.getSDKService(PHHueSDK.SEARCH_BRIDGE);
		sm.search(true, true); 	
		Log.d("DMC-HUE", "Started Brigde Discovering Mode");
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void changeLight(int _light, int _hue, int _brightness){		
		PHLightState state = new PHLightState();
		state.setHue(_hue);
		state.setBrightness(_brightness);
		
		mBridge.updateLightState(mLightList.get(_light), state);	// 666: no callback used
		Log.d("DMC-HUE", "Changing light");
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	private PHSDKListener mHueListener = new PHSDKListener() {
		
		@Override
		public void onParsingErrors(List<PHHueParsingError> _errors) {
			Log.d("DMC-HUE", "Parsed Error");
		}
		
		@Override
		public void onError(int _error, String _errorStr) {
			Log.d("DMC-HUE", "Error");
		}
		
		@Override
		public void onConnectionResumed(PHBridge _bridge) {
			Log.d("DMC-HUE", "Connection Resumed");
		}
		
		@Override
		public void onConnectionLost(PHAccessPoint _accessPoint) {
			Log.d("DMC-HUE", "Connection Lost");
		}
		
		@Override
		public void onCacheUpdated(List<Integer> _cacheNotificationsList, PHBridge _bridge) {
			Log.d("DMC-HUE", "Cache Updated");
			if (_cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED)) { // 666 TODO: Today only light are been used
				System.out.println("Lights Cache Updated ");
				PHBridgeResourcesCache cache =  _bridge.getResourceCache();
				mLightList = cache.getAllLights();
			}
		}
		
		@Override
		public void onBridgeConnected(PHBridge _bridge) {
			Log.d("DMC-HUE", "Bridge Connected");
			mHueSDK.setSelectedBridge(_bridge);
            mHueSDK.enableHeartbeat(_bridge, PHHueSDK.HB_INTERVAL);
            
            mBridge = _bridge;
		}
		
		@Override
		public void onAuthenticationRequired(PHAccessPoint _accessPoint) {
			Log.d("DMC-HUE", "Authentication Required");
			// Waiting 4 authentication. 666 TODO some display
			mHueSDK.startPushlinkAuthentication(_accessPoint);
		}
		
		@Override
		public void onAccessPointsFound(List<PHAccessPoint> _accessPoints) {
			Log.d("DMC-HUE", "Access Points Found");
			// Connecting to the first access point. 666 TODO: check last connection.  
			PHAccessPoint ap = _accessPoints.get(0);
			ap.setUsername("newdeveloper");	// 666 TODO: random user name or account user name.
		 	mHueSDK.connect(ap);
		}
	};
	
	//-----------------------------------------------------------------------------------------------------------------
	
	
}
