package es.domocracy.domocracyapp.comm;

import java.util.Arrays;

import android.util.Log;

public class Message {
	// -----------------------------------------------------------------------------------------------------------------
	// List of message types
	public static enum Type {
		ON(0x01), 								//
		OFF(0x02), 
		Dimmer(0x03), 
		RGB(0x04),

		Look4Devices(0x80), 					//	
		RequestRoomListInfo(0x81),				//	
		RoomListInfo(0x82), 					//	
		NewDevice(0x83),						//  Message that tells n new devices to be added. Followed by n messages 0x86
		UpdateState(0x84),						//	Raspi tells mobile that a device has changed state
		InfoRoom(0x85),							//	ID + n Devices + Name 
		InfoDevice(0x86),						//	Device Type + Device ID + Name
		PairDevice2Room(0x87),					//	Device ID + Room ID
		RenameDevice(0x88),						// 	Device ID + Device Name
		
		HandShake(0x90), 						//	ID of sender
		RequestID(0x91), 						//	Request new client ID.
		SubmitID(0x92);							//	Submit del menssage 0x91 con la ID del cliente

		public final byte value;

		Type(int _value) {
			this.value = (byte) _value;
		}
		
	}

	// -----------------------------------------------------------------------------------------------------------------
	static private boolean checkIntegrity(byte[] _rawMessage) {
		// If _rawMessage is not initialized.
		if(_rawMessage == null)
			return false;
		
		// Check if length is proper.
		if ((byte) _rawMessage.length != _rawMessage[0]) {
			Log.d("DMC-DEBUG", "Received corrupted message. Lenght is not correct! Raw is: " + new String(_rawMessage));
			return false;
		}
		return true;
	}

	// -----------------------------------------------------------------------------------------------------------------
	static public Message decode(byte[] _rawMessage) {
		// Check if message is correct.
		if (!checkIntegrity(_rawMessage)) {
			return null;
		}

		// Decode raw message and create an instance of it.

		return new Message(	_rawMessage[0],
							_rawMessage[1],
							Arrays.copyOfRange(_rawMessage, 2, _rawMessage.length));
	}

	// -----------------------------------------------------------------------------------------------------------------

	private byte mType;
	private byte mSize;
	private byte[] mPayload;
	private byte[] mRawMessage;

	// -----------------------------------------------------------------------------------------------------------------

	public byte type() {
		return mType;
	}


	public byte size() {
		return mSize;
	}

	public byte[] payload() {
		return mPayload;
	}

	public byte[] rawMessage() {
		return mRawMessage;
	}

	// -----------------------------------------------------------------------------------------------------------------

	public Message(byte _size, byte _type, byte[] _payload) {
		mSize = _size;
		mType = _type;
		mPayload = _payload;
		mRawMessage = new byte[mSize];
		mRawMessage[0] = mSize;
		mRawMessage[1] = mType;
		for(int i = 0 ; i < mPayload.length ; i++){
			mRawMessage[i+2] = mPayload[i];
		}
		
	}

	// -----------------------------------------------------------------------------------------------------------------

	// -----------------------------------------------------------------------------------------------------------------
}
