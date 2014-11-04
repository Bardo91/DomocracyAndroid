package es.domocracy.domocracyapp.comm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class HubConnectionWifi extends HubConnection {
	// -----------------------------------------------------------------------------------
	// HubConnectionWifi
	protected final int TIMEOUT = 2000;
	public boolean connectToHub(final Hub _hub) {
		assert(_hub.connType() == Hub.eConnectionTypes.eWifi);
		
		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					if (_hub.port() != -1) {
						mHubSocket = new Socket();
						mHubSocket.connect(new InetSocketAddress(_hub.addr(),
								_hub.port()), TIMEOUT);

						mInStream = mHubSocket.getInputStream();
						mOutStream = mHubSocket.getOutputStream();

						mHub = _hub;
						mIsConnected = true;
						
						initReading();
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();

		Long t0 = System.currentTimeMillis();
		while (!mIsConnected) {
			Long t1 = System.currentTimeMillis();
			if (t1 - t0 >= TIMEOUT) {
				Log.d("DMC", "Could not connect to device");
				break;
			}
		}

		return mIsConnected;
	}
}
