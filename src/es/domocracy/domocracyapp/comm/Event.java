package es.domocracy.domocracyapp.comm;

import java.util.Vector;


public class Event {
	private Vector<MessageListener> mListeners;		// Registered listeners to this event (Message type)
	
	//-----------------------------------------------------------------------------------------------------------------
	public Event(){
		mListeners = new Vector<MessageListener>();
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void registerListener(MessageListener _messageListener){
		// If this object is already registered, do nothing
		if(mListeners.contains(_messageListener)){
			return;
		}
		
		// If not, register it.
		mListeners.add(_messageListener);
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public void callListeners(Message _message){
		// Call to action to every listener.
		for(MessageListener listener : mListeners){
			listener.onMessage(_message);
			
		}
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	
	
	//-----------------------------------------------------------------------------------------------------------------
}
