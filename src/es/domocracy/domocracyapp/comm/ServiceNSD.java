///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.comm;

import java.net.InetAddress;
import java.util.UUID;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class ServiceNSD {
	final public static String DOMOCRACY_HUB_SERVICE = "domocracy";
	final public static String DOMOCRACY_HUB_SERVICE_TYPE = "_dmc._tcp";

	private NsdManager mNsdManager = null;
	private DiscoveryListener mDiscoveryListener = null;
	private ResolveListener mResolveListener = null;

	private NsdServiceInfo mServiceInfo = null;
	private int mPort = -1;
	private InetAddress mHost = null;
	
	private final int TIMEOUT = 10000;	// 1000 ms.

	// -----------------------------------------------------------------------------------
	// -------- Public Interface
	public ServiceNSD(Context _context) {
		initializeDiscoveryListener();
		initializeResolveListener();

		mNsdManager = (NsdManager) _context
				.getSystemService(Context.NSD_SERVICE);

	}

	// -----------------------------------------------------------------------------------
	public Hub getConnectionInfo(){
		startServiceDiscovery();
		
		Long t0 = System.currentTimeMillis();
		while(mPort == -1){
			Long t1 = System.currentTimeMillis();
			if(t1 - t0 >= TIMEOUT){
				break;
			}
		}
		
		stopServiceDiscovery();
		
		return new Hub("Rinoceronte", UUID.randomUUID(), mHost, mPort);
	}
	
	
	// -----------------------------------------------------------------------------------
	// -------- Private Interface
	// -----------------------------------------------------------------------------------
	// -----------------------------------------------------------------------------------
	private void startServiceDiscovery() {
		mNsdManager.discoverServices(DOMOCRACY_HUB_SERVICE_TYPE,
				NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

	}

	// -----------------------------------------------------------------------------------
	private void stopServiceDiscovery() {
		mNsdManager.stopServiceDiscovery(mDiscoveryListener);

	}
	
	
	/************************************************************************************
	 * Custom Service Resolve Listener
	 * 
	 * **********************************************************************************/
	private void initializeResolveListener() {
		mResolveListener = new ResolveListener() {
			// -----------------------------------------------------------------------------------
			@Override
			public void onResolveFailed(NsdServiceInfo _serviceInfo,
					int _errorCode) {
				Log.d("DMC", "Resolved failed, error code: " + _errorCode);

			}

			// -----------------------------------------------------------------------------------
			@Override
			public void onServiceResolved(NsdServiceInfo _serviceInfo) {
				Log.d("DMC", "Resolved succeeded: " + _serviceInfo);

				mServiceInfo = _serviceInfo;
				mPort = mServiceInfo.getPort();
				mHost = mServiceInfo.getHost();
				
				Log.d("DMC", "Detected hub with addr:" + mHost + " and port: " + mPort);
			}
		};
	}

	/************************************************************************************
	 * Custom NSD Discovery Listener
	 * 
	 * **********************************************************************************/
	// -----------------------------------------------------------------------------------
	private void initializeDiscoveryListener() {
		mDiscoveryListener = new DiscoveryListener() {

			// -----------------------------------------------------------------------------------
			@Override
			public void onDiscoveryStarted(String _regType) {
				Log.d("DMC", "Started NSD");
			}

			// -----------------------------------------------------------------------------------
			@Override
			public void onDiscoveryStopped(String _serviceType) {
				Log.d("DMC", "Stopped NSD");

			}

			// -----------------------------------------------------------------------------------
			@Override
			public void onServiceFound(NsdServiceInfo _service) {
				// Found Service
				Log.d("DMC", "Service discovery success with name: " + _service.getServiceName() + " and type: " + _service.getServiceType());
				if (_service.getServiceName().contains(DOMOCRACY_HUB_SERVICE)) {
					// Found Domocracy's service.
					mNsdManager.resolveService(_service, mResolveListener);
				}

			}

			// -----------------------------------------------------------------------------------
			@Override
			public void onServiceLost(NsdServiceInfo _service) {
				Log.d("DMC",
						"The service is not longer available on the network");
			}

			// -----------------------------------------------------------------------------------
			@Override
			public void onStartDiscoveryFailed(String _serviceType,
					int _errorCode) {
				Log.d("DMC", "Discovery failed: Error code: " + _errorCode);
				mNsdManager.stopServiceDiscovery(this);

			}

			// -----------------------------------------------------------------------------------
			@Override
			public void onStopDiscoveryFailed(String _serviceType,
					int _errorCode) {
				Log.d("DMC", "Discovery failed: Error code: " + _errorCode);
				mNsdManager.stopServiceDiscovery(this);

			}
		};
	}

	// -----------------------------------------------------------------------------------

}
