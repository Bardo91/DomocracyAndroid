package es.domocracy.domocracyapp.devices.devicecontrollers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import es.domocracy.domocracyapp.R;
import es.domocracy.domocracyapp.comm.Message;
import es.domocracy.domocracyapp.devices.Device;
import es.domocracy.domocracyapp.devices.DeviceState;

public class DimmController extends DeviceController {
	// -----------------------------------------------------------------------------------
	// DimmController members
	private byte mDimmVal = 0x00;

	// -----------------------------------------------------------------------------------
	// DimmController public interface

	public DimmController(Device _owner) {
		super(_owner);

	}

	// -----------------------------------------------------------------------------------
	@Override
	public View getView(Context _context) {
		LayoutInflater inflater = LayoutInflater.from(_context);

		mControllerView = inflater.inflate(R.layout.dimm_controller_layout,
				null);

		((SeekBar) (mControllerView.findViewById(R.id.dimm_bar)))
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						mDimmVal = (byte) progress;
						sendInstruction();
					}
				});

		return mControllerView;
	}

	// -----------------------------------------------------------------------------------
	@Override
	public void updateControllerState(DeviceState _state) {
		mDimmVal = _state.getDimmValue();
	}

	// -----------------------------------------------------------------------------------
	// DimmController private interface

	@Override
	protected void sendInstruction() {
		mOwner.state().setDimmValue(mDimmVal);

		// Send Instruction
		byte[] payload = { mOwner.UUID(), mDimmVal };
		Message msg = new Message((byte) 0x04, Message.Type.Dimmer.value,
				payload);
		mOwner.sendInstruction(msg);

	}

}
