package com.lplus.facebook;

import android.util.Log;

import com.facebook.android.old.DialogError;
import com.facebook.android.old.FacebookError;
import com.facebook.android.old.Facebook.DialogListener;


public abstract class LplusBaseDialogListener implements DialogListener {

	public void onFacebookError(FacebookError e) {
		Log.e("Lplus", "error" + e.getMessage());
        e.printStackTrace();
    }

    public void onError(DialogError e) {
    	Log.e("Lplus", "error " + e.getMessage());
        e.printStackTrace();        
    }

    public void onCancel() {        
    }
}
