package es.domocracy.domocracyapp.comm;

import java.util.HashMap;

public class MessageDispatcher {
	// -----------------------------------------------------------------------------------------------------------------

	static private MessageDispatcher mDispatcherInstance = null;;
	static private HashMap<Byte, Event> mEvents;

	static private HubConnection mHubConnection = null;
		
	// -----------------------------------------------------------------------------------------------------------------
	// Public interface
	static public void init(HubConnection _connection) {
		if(mDispatcherInstance == null){
			mDispatcherInstance = new MessageDispatcher();			
		}
		
		mHubConnection = _connection;			// 666 Check if there's another possibility
	}

	//-----------------------------------------------------------------------------------------------------------------
	static public MessageDispatcher get(){
		return mDispatcherInstance;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	static public void registerListener(MessageListener _listener) {
		if(mDispatcherInstance == null){
			mDispatcherInstance = new MessageDispatcher();
		}
		
		for (byte type : _listener.messageTypes()) {
			if (!mEvents.containsKey(type)) { // If it's the first time that
												// this kind of message is
												// registered, create a new
												// event type
				mEvents.put(type, new Event());
			}

			mEvents.get(type).registerListener(_listener); // register Listener
															// into the event
															// type
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Private Interface
	private void callEvent(Message _message) {
		// Check if there is any listener register to the message's type.
		if(_message == null)
			assert(false);	// Uninitialized message
		if (mEvents.containsKey(_message.type())) {
			// If there is, call listeners.
			mEvents.get(_message.type()).callListeners(_message);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	private MessageDispatcher() {
		mEvents = new HashMap<Byte, Event>();
		
		// Start a thread for hearing
		Thread hearingThread = new Thread() {
			@Override
			public void run() {
				for(;;){
					if(mHubConnection == null || !mHubConnection.isConnected())
						continue;
					// Get string from input buffer 
					Message msg = mHubConnection.readBuffer();	// TODO: 666 Doesn't check if there is more than 1 message together
					
					// Create Message from raw
					if(msg != null){
						callEvent(msg);
					}
				}
			}

		};

		hearingThread.start();
	}

	// -----------------------------------------------------------------------------------------------------------------

}
