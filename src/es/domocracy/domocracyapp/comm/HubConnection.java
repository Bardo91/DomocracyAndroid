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
import java.util.Arrays;

import android.content.Context;
import android.util.Log;

public abstract class HubConnection {
	// -----------------------------------------------------------------------------------
	// HubConnection members
	protected Hub mHub;
	protected InputStream mInStream;
	protected OutputStream mOutStream;

	private int mBufferLenght = 0;
	private byte[] mPersistentBuffer = new byte[2048];

	
	// -----------------------------------------------------------------------------------
	//	Abstract interface
	abstract public boolean connectToHub(final Hub _hub);
	abstract public boolean closeConnection(Context _Context);
	abstract public boolean isConnected();
	
	// -----------------------------------------------------------------------------------
	// HubConnection public Interface
	// -----------------------------------------------------------------------------------
	public boolean sendMsg(Message _msg) {
		if (isConnected()) {
			try {
				mOutStream.write(_msg.rawMessage());
				Log.d("DMC", "Sended msg: " + new String(_msg.rawMessage()));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// -----------------------------------------------------------------------------------
	public Message readBuffer() {
		if (0 < mBufferLenght) {
			// Create message
			assert(0 != mPersistentBuffer[1]);
			Log.d("DMC", "Received a message of type: " + mPersistentBuffer[1]);
			byte[] rawMsg = Arrays.copyOf(mPersistentBuffer,mPersistentBuffer[0]);
			Message msg = Message.decode(rawMsg);

			// Empty buffer
			byte msgSize = mPersistentBuffer[0];
			System.arraycopy(mPersistentBuffer, msgSize, mPersistentBuffer, 0, mBufferLenght);
			mBufferLenght -= msgSize;

			if (msg.isValid())
				return msg;
		}
		return null;
	}

	// -----------------------------------------------------------------------------------
	public Hub hub() {
		return mHub;
	}

	// -----------------------------------------------------------------------------------
	// HubConnection private interface
	protected HubConnection() {

	}
	
	// -----------------------------------------------------------------------------------
	protected void initReading() {
		Thread readingThread = new Thread() {
			@Override
			public void run() {
				for (;;) {
					byte[] buffer = new byte[1024];

					int nBytes = 0;
					try {
						if (0 < mInStream.available()) {
							nBytes = mInStream.read(buffer);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (nBytes > 0) {
						System.arraycopy(buffer, 0, mPersistentBuffer, mBufferLenght, nBytes);
						mBufferLenght += nBytes;
					}
				}
			}
		};
		readingThread.start();
	}
	// -----------------------------------------------------------------------------------
}
