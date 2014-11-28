///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	Project:	Android Application
//	Date:		2014/08/22
//	Author:		Pablo Ramon Soria
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

package es.domocracy.domocracyapp.comm;

import android.content.Context;

public interface HubConnection {
	// ----------------------------------------------------------------------------------------------------------------
	// Connection interface
	public boolean connectToHub(final Hub _hub, Context _context);
	public boolean closeConnection(Context _context);
	
	public boolean isConnected();
	
	// ----------------------------------------------------------------------------------------------------------------
	// Communication interface
	public boolean sendMsg(Message _msg);
	public Message readBuffer();
}
