package es.domocracy.domocracyapp.comm;



public abstract class MessageListener{
	//-----------------------------------------------------------------------------------------------------------------

	private byte[] mMessageTypes;
	
	
	//-----------------------------------------------------------------------------------------------------------------
	
	//  Public interface
	public MessageListener(byte[] _types){
		mMessageTypes = _types;
		
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public byte[] messageTypes(){	// Get the list of supported messages.
		return mMessageTypes;
	}
	
	//-----------------------------------------------------------------------------------------------------------------
	public abstract void onMessage(Message _message);	// Implement on child class
	
}
