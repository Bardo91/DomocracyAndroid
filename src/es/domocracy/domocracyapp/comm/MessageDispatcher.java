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
		assert(_message == null); // Uninitialized message
		if (mEvents.containsKey(_message.type())) {
			// If there is, call listeners.
			mEvents.get(_message.type()).callListeners(_message);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	private MessageDispatcher() {
		mEvents = new HashMap<Byte, Event>();
		
		// Start a thread for hearing
		Thread listeningThread = new Thread() {
			final long sleepTime = 5;			// Time to spend asleep when there's no connection
			
			@Override
			public void run() {
				for(;;){
					if(mHubConnection == null || !mHubConnection.isConnected()){
						try {
							sleep(sleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					// Get string from input buffer 
					Message msg = mHubConnection.readBuffer();	// TODO: 666 Doesn't check if there is more than 1 message together
					
					// Create Message from raw
					if(msg != null){
						callEvent(msg);
					}
				}
			}

		};

		listeningThread.start();
	}

	// -----------------------------------------------------------------------------------------------------------------

}
