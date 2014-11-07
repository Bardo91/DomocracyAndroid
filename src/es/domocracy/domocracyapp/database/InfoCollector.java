package es.domocracy.domocracyapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import es.domocracy.domocracyapp.comm.HubConnection;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceState;
import es.domocracy.domocracyapp.devices.DeviceType;
import es.domocracy.domocracyapp.rooms.Room;

public class InfoCollector {
	static public List<Room> getRooms(HubConnection _hubConnection){
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
			devices.add(Device.getDevice((byte) 0x15, "Lampara", new DeviceType(), new DeviceState(), _hubConnection));
			rooms.add(new Room(UUID.randomUUID(), roomNames[i], devices));
		}
		
		
		return rooms;
		
	}
}
