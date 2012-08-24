package com.lplus.widget;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.lplus.common.LplusFramework;
import com.lplus.common.LplusFrameworkType;
import com.lplus.facebook.LplusBaseDialogListener;
import com.lplus.facebook.LplusBaseRequestListener;
import com.lplus.facebook.LplusFacebook;
import com.lplus.facebook.R;

public class LplusLoginButton extends ImageButton implements LplusButton {
	LplusFramework mFramework;
	LoginCompleteListener mListener;

	public LplusLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LplusLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginButton(Context context) {
		super(context);
	}
	
	public void init(LplusFramework framework, LoginCompleteListener listener) {
		mFramework = framework;
		mListener = listener;
		
		if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) {
			setBackgroundResource(R.drawable.loginbutton_facebook_bg_logout);
		} else {
			
		}
				
		setOnClickListener(new LplusLoginButtonOnClickListener());
	}
	
	private LplusFacebook getLplusFacebook() {
		if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) 
			return (LplusFacebook)mFramework;
		else
			return null;
	}
	
	public interface LoginCompleteListener {
		public void onComplete(Bitmap pic);
	}
	
	private final class LplusLoginButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			
			if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) {
				LplusFacebook lplusFacebook = getLplusFacebook();
				if (lplusFacebook.isSessionValid()) {
					lplusFacebook.logout(new FacebookLogoutRequestListener());
		        } else {
		        	lplusFacebook.login((Activity)getContext(), new FacebookLoginDialogListener());	        	
		        }
			} else {
				
			}
		}
	}
	
	private final class FacebookLoginDialogListener extends LplusBaseDialogListener {
		public void onComplete(Bundle values) {
			setBackgroundResource(R.drawable.loginbutton_facebook_bg_login);
			
			if (mListener != null) {
				Bitmap profilePic = null;
				
				if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) {
					LplusFacebook lplusFacebook = getLplusFacebook();		
					profilePic = lplusFacebook.getUserProfilePic("gooson", new TestRequestListener());
					//setImageBitmap(profilePic);
				} else {
					
				}
				
				//mListener.onComplete(profilePic);
			}
		}
	}
	
	private class TestRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			Bitmap profilePic = null;
			
		    try {
		    	Log.e("Lplus", "onComplete try entry");
		        // URL pictureUrl = new URL(response);
		        // HttpURLConnection.setFollowRedirects(false);                
		        // HttpURLConnection pictureUrlConnect = (HttpURLConnection) pictureUrl.openConnection();

///		         InputStream is = pictureUrlConnect.getInputStream();
///		         BufferedInputStream bis = new BufferedInputStream(is);
		         
		         profilePic = BitmapFactory.decodeByteArray(response.getBytes(), 0, response.length());
		         
		         Log.e("Lplus", "onComplete try end ");

		    } catch (Exception e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		        Log.e("Lplus", "onComplete " + e.toString());
		    }
		    
		    mListener.onComplete(profilePic);
		}
	}
		
	
	private final class FacebookLogoutRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			setBackgroundResource(R.drawable.loginbutton_facebook_bg_logout);	
		}
	}
}
