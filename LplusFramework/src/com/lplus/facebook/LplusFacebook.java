package com.lplus.facebook;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.lplus.common.LplusFramework;
import com.lplus.common.LplusFrameworkType;

public class LplusFacebook implements LplusFramework {

	Facebook mfacebook;
	AsyncFacebookRunner mAsyncRunner;
	Activity mMainActivity;
	String[] mPermissions;
	
	public LplusFacebook(String appID, String[] permissions) {
		mfacebook = new Facebook(appID);
		mAsyncRunner = new AsyncFacebookRunner(mfacebook);
		mPermissions = permissions;
	}
	
	public void login(Activity activity, DialogListener listener) {
		if (listener == null) {
			listener = new DefaultDialogListener();
		}
		
		mMainActivity = activity;
		
		mfacebook.authorize(mMainActivity, mPermissions, listener);
	}
	
	public void logout(RequestListener listener) {
		if (listener == null) {
			listener = new DefaultRequestListener();
		}
		mAsyncRunner.logout(mMainActivity, listener);
	}

	public void authorizeCallback(int requestCode, int resultCode, Intent data) {
		mfacebook.authorizeCallback(requestCode, resultCode, data);
	}

	public boolean isSessionValid() {
		return mfacebook.isSessionValid();
	}
	
	public void postOnFeed(String message, RequestListener listener) {
		if (listener == null) {
			listener = new DefaultRequestListener();
		}
		
		Bundle parameters = new Bundle();
		parameters.putString("message", message);

		mAsyncRunner.request("me/feed", parameters, "POST", listener, null);
	}
	
	public void postOnFeed(String message, Bitmap img, RequestListener listener) {
		if (listener == null) {
			listener = new DefaultRequestListener();
		}
		
		Bundle parameters = new Bundle();
		parameters.putString("message", message);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapdata = stream.toByteArray(); 
		parameters.putByteArray("source", bitmapdata);
		
		
		mAsyncRunner.request("me/photos", parameters, "POST", listener, null);
	}
	
	public Bitmap getUserProfilePic(String Id) {
		String imageURL;
	    Bitmap pic = null;
	    
	    imageURL = "http://graph.facebook.com/" + Id + "/picture?type=large";
	    
	    try {
	    	pic = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return pic;
	}

	@Override
	public LplusFrameworkType getType() {
		return LplusFrameworkType.LPLUS_TYPE_FACEBOOK;
	}
	
	public class DefaultDialogListener extends LplusBaseDialogListener { 
		public void onComplete(Bundle values) {
		}
	}
	
	private class DefaultRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			
		}
	}
}
