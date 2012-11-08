package com.lplus.facebook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
	
	public void getUserInfo(String Id, RequestListener listener) {
		mAsyncRunner.request(Id, listener);
	}
	
	public void getUserProfilePic(String Id, final BitmapAsyncTaskListener listener) {
		
		if (listener == null)
			return;
		
		AsyncTask<URL, Void, Bitmap> asyncTask = new AsyncTask<URL, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(URL... arg0) {
				try {
					URL pictureUrl = arg0[0];
					return BitmapFactory.decodeStream(pictureUrl.openConnection().getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (isCancelled())
					result = null;
				
				if (listener != null)
					listener.onComplete(result);
			}
		};
		
		try {
			asyncTask.execute(new URL("http://graph.facebook.com/" + Id + "/picture?type=large"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public LplusFrameworkType getType() {
		return LplusFrameworkType.LPLUS_TYPE_FACEBOOK;
	}
	
	public interface BitmapAsyncTaskListener {
		public void onComplete(Bitmap bitmap);
	}
	
	public class DefaultDialogListener extends LplusBaseDialogListener { 
		public void onComplete(Bundle values) {
		}
	}
	
	private class DefaultRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			
		}
	}
	
	/*private class TestRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			try {
				JSONObject json = Util.parseJson(response);
				final JSONArray friends = json.getJSONArray("data");
				
				String friendId, friendName;
                JSONObject friend;
                for (int i = 0; i < friends.length(); i++) {
                    friend = friends.getJSONObject(i);
                    friendId = friend.getString("id");
                    friendName = friend.getString("pic_square");
                    Log.e("gooson", "friend : (id/name) " + friendId + "/" + friendName);
                }
                
			} catch (FacebookError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
}
