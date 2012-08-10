package com.lplus.android;

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

	String[] permission = new String[] {
			// User and Friends Permissions
			  "user_about_me"
			, "user_activities"
			, "user_birthday"
			, "user_checkins"
			, "user_education_history"
			, "user_events"
			, "user_groups"
			, "user_hometown"
			, "user_interests"
			, "user_likes"
			, "user_location"
			, "user_notes"
			, "user_photos"
			, "user_questions"
			, "user_relationships"
			, "user_relationship_details"
			, "user_religion_politics"
			, "user_status"
			, "user_subscriptions"
			, "user_videos"
			, "user_website"
			, "user_work_history"
			, "email"
			// Extended Permissions
			, "read_friendlists"
			, "read_insights"
			, "read_mailbox"
			, "read_requests"
			, "read_stream"
			, "xmpp_login"
			, "ads_management"
			, "create_event"
			, "manage_friendlists"
			, "manage_notifications"
			, "user_online_presence"
			, "friends_online_presence"
			, "publish_checkins"
			, "publish_stream"
			, "rsvp_event"
			// Open Graph Permissions
			/*, "publish_actions"
			, "user_actions.music"
			, "user_actions.news"
			, "user_actions.video"
			, "user_actions:APP_NAMESPACE"
			, "user_games_activity"*/
			// Page Permissions
			, "manage_pages"
		};
	
	Facebook mfacebook;
	AsyncFacebookRunner mAsyncRunner;
	Activity mMainActivity;
	
	public LplusFacebook() {
		mfacebook = new Facebook("338804906187760");
		mAsyncRunner = new AsyncFacebookRunner(mfacebook);
	}
	
	public void login(Activity activity, DialogListener listener) {
		if (listener == null) {
			listener = new DefaultDialogListener();
		}
		
		mfacebook.authorize(mMainActivity, permission, listener);
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
