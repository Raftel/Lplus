package com.lplus.facebook;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;

public class LplusFacebook {

	Facebook mfacebook;
	AsyncFacebookRunner mAsyncRunner;
	Activity mMainActivity;
	
	public LplusFacebook(String appID) {
		mfacebook = new Facebook(appID);
		mAsyncRunner = new AsyncFacebookRunner(mfacebook);
	}
	
	public void login(Activity activity, String[] permissions, DialogListener listener) {
		if (listener == null) {
			listener = new DefaultDialogListener();
		}
		
		mfacebook.authorize(mMainActivity, permissions, listener);
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

	public class DefaultDialogListener extends LplusBaseDialogListener { 
		public void onComplete(Bundle values) {
		}
	}
	
	private class DefaultRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			
		}
	}
}
