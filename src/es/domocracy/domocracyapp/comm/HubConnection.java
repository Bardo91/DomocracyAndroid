///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import android.util.Log;

public class HubConnection {
	// -----------------------------------------------------------------------------------
	// HubConnection members
	private final int TIMEOUT = 2000; // 1000 ms.

	private Hub mHub;
	private Socket mHubSocket;
	private InputStream mInStream;
	private OutputStream mOutStream;
	private boolean mIsConnected = false;

	// -----------------------------------------------------------------------------------
	// HubConnection public Interface
	public HubConnection() {

	}

	// -----------------------------------------------------------------------------------
	public boolean sendMsg(Message _msg) {
		if (mHubSocket.isConnected()) {
			try {
				mOutStream.write(_msg.rawMessage());
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	// -----------------------------------------------------------------------------------
	public Message readBuffer(){
		if(mHubSocket.isConnected()){
			byte[] buffer = new byte[1024];
			
			int nBytes = 0;
			try {
				if(0 < mInStream.available()){
					nBytes = mInStream.read(buffer);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(nBytes > 0){
				Log.d("DMC-DEBUG", "Received a message of type: " + buffer[1]);
				return Message.decode(Arrays.copyOf(buffer, nBytes));	
			}
		}
		return null;
	}
	
	
	// -----------------------------------------------------------------------------------
	public boolean connectToHub(final Hub _hub) {
		Thread t = new Thread() {

			@Override
			public void run() {
				try {
					mHubSocket = new Socket();
					mHubSocket.connect(new InetSocketAddress(_hub.addr(), _hub.port()), TIMEOUT);
					
					mInStream = mHubSocket.getInputStream();
					mOutStream = mHubSocket.getOutputStream();

					mHub = _hub;
					mIsConnected = true;
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
				return false;
			}
		}

		return true;
	}

	// -----------------------------------------------------------------------------------
	public boolean isConnected(){
		return mIsConnected;
	}
	// -----------------------------------------------------------------------------------
	public boolean closeConnection() {
		if (mHubSocket != null && mHubSocket.isConnected()) {
			try {
				for (;;) {
					mHubSocket.close();
					if (mHubSocket.isClosed())
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// -----------------------------------------------------------------------------------
	public Hub hub() {
		return mHub;
	}

	// -----------------------------------------------------------------------------------
	// HubConnection private interface

	// -----------------------------------------------------------------------------------
}
