package es.domocracy.domocracyapp.devices.devicecontrollers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;
import es.domocracy.domocracyapp.R;
import es.domocracy.domocracyapp.comm.Message;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceState;

public class SwitchController extends DeviceController {
	// -----------------------------------------------------------------------------------
	// SwitchController members
	private boolean mSwitchVal = false;


	// -----------------------------------------------------------------------------------
	// SwitchController public interface

	public SwitchController(Device _owner) {
		super(_owner);
		
	}

	// -----------------------------------------------------------------------------------
	@Override
	public View constructView(Context _context) {
		LayoutInflater inflater = LayoutInflater.from(_context);

		mControllerView = inflater.inflate(R.layout.switch_controller_layout,
				null);

		((ToggleButton) (mControllerView.findViewById(R.id.switch_button)))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mSwitchVal = !mSwitchVal;
						sendInstruction();
					}
				});

		return mControllerView;
	}
	
	//-----------------------------------------------------------------------------------
	@Override
	public void updateControllerState(DeviceState _state) {
		mSwitchVal = _state.state() == 0xFF ? true : false;
		
	}

	// -----------------------------------------------------------------------------------
	// SwitchController private interface
	@Override
	protected void sendInstruction() {

		// Update Device's State
		DeviceState state = new DeviceState();
		state.setState((byte) (mSwitchVal? 0xFF : 0x00));
		
		mOwnerInstance.updateState(state);
		
		// Send instruction
		byte[] payload = {mOwnerInstance.UUID()};
		Message msg = new Message(	(byte) 0x03,
									mSwitchVal ? Message.Type.ON.value : Message.Type.OFF.value, 
									payload);
		
		mOwnerInstance.sendInstruction(msg);
		
		
	}

}
