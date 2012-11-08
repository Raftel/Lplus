package com.lplus.facebook;

import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


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
