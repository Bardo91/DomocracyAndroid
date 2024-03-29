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
	public View getView(Context _context) {
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
		mSwitchVal = _state.isOn();
		
	}

	// -----------------------------------------------------------------------------------
	// SwitchController private interface
	@Override
	protected void sendInstruction() {
		if(mSwitchVal)
			mOwner.state().setOn();
		else
			mOwner.state().setOff();
		
		// Send instruction
		byte[] payload = {mOwner.UUID()};
		Message msg = new Message(	(byte) 0x03,
									mSwitchVal ? Message.Type.ON.value : Message.Type.OFF.value, 
									payload);
		
		mOwner.sendInstruction(msg);

	}

}
