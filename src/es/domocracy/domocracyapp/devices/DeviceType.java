package es.domocracy.domocracyapp.devices;

import java.util.ArrayList;
import java.util.List;

import es.domocracy.domocracyapp.devices.devicecontrollers.DeviceController;
import es.domocracy.domocracyapp.devices.devicecontrollers.DimmController;
import es.domocracy.domocracyapp.devices.devicecontrollers.SwitchController;


public class DeviceType {
	//-----------------------------------------------------------------------------------
	// Device Type members
	
	// 666 Structure?
	
	//-----------------------------------------------------------------------------------
	// Control Factory
	public List<DeviceController> buildControllers(Device _dev){
		// 666 TODO dummy function 
		
	 	List<DeviceController> list = new ArrayList<DeviceController>();
		

		list.add(new DimmController(_dev));
		list.add(new SwitchController(_dev));
		
		return list; 
	}
	
	
	//-----------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------
	
	//-----------------------------------------------------------------------------------
	
	
}
