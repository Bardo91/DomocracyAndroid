package es.domocracy.domocracyapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.domocracy.domocracyapp.comm.ConnectionManager;
import es.domocracy.domocracyapp.comm.HubConnection;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceState;
import es.domocracy.domocracyapp.devices.DeviceType;
import es.domocracy.domocracyapp.rooms.Room;

public class InfoCollector {
	static public List<Room> getRooms(ConnectionManager _conMgr){
		List<Room> rooms = new ArrayList<Room>();
		
		String[] roomNames = {"Timmy", "Johny", "Tony", "Colly", "Polly"};
		//String[] deviceNames = {"Lamp", "Switch", "LightBulb", "Laundry", "Smartphone Charger"};
		
		Random random = new Random();
		int nRooms = random.nextInt(3) + 2;
		for(int i = 0; i <  nRooms; i++){
			List<Device> devices = new ArrayList<Device>();
			//int nDevices = random.nextInt(3)+2;
			//for(int j = 0 ; j < nDevices; j++){
			//	devices.add(Device.getDevice(	(byte) 0x1C, 
			//									deviceNames[random.nextInt(5)], 
			//									new DeviceType(), 
			//									null, 
			//									_hubConnection));
			//}
			
			// 666 TODO: where is the info about the device type? Default. wifi.
			HubConnection connection = _conMgr.wifiConnection();
			devices.add(Device.getDevice((byte) 0x15, "Lampara", new DeviceType(), new DeviceState(), connection));
			rooms.add(new Room((byte) 0x69, roomNames[i], devices));
		}
		
		
		return rooms;
		
	}
}
