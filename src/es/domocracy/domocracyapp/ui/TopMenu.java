package es.domocracy.domocracyapp.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import es.domocracy.domocracyapp.R;

public class TopMenu {
	// -----------------------------------------------------------------------------------------------------------------
	// Class members
	private ActionBar mActionBar;
	
	// -----------------------------------------------------------------------------------------------------------------
	// Public interface
	public TopMenu(Activity _activity){
		initActionBar(_activity);
		
	}
	
	// -----------------------------------------------------------------------------------------------------------------
	// Private Interface
	private void initActionBar(Activity _activity) {		
		mActionBar = _activity.getActionBar();

		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
	}
}
