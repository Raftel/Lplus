package com.lplus.widget;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.lplus.animation.LplusAnimation;
import com.lplus.animation.LplusAnimationManager;
import com.lplus.animation.LplusAnimationProp;
import com.lplus.animation.LplusAnimationUtil;
import com.lplus.common.LplusFramework;
import com.lplus.common.LplusFrameworkType;
import com.lplus.facebook.LplusBaseDialogListener;
import com.lplus.facebook.LplusBaseRequestListener;
import com.lplus.facebook.LplusFacebook;
import com.lplus.facebook.LplusFacebook.BitmapAsyncTaskListener;
import com.lplus.facebook.R;

public class LplusLoginButton extends ImageButton implements LplusButton {
	LplusFramework mFramework;
	LplusAnimationManager mAnimManager;
	BitmapAsyncTaskListener mListener;
	UserInfo mUserInfo;
	
	private class UserInfo {
		String id;
		String firstName;
		String lastName;
		String userName;
		String gender;
		String locale;
	}

	private LplusLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private LplusLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginButton(Context context) {
		super(context);
		setLayerType(LAYER_TYPE_HARDWARE, null);
	}

	public void init(LplusFramework framework, BitmapAsyncTaskListener listener) {
		mFramework = framework;
		mListener = listener;
		mUserInfo = new UserInfo();
		mAnimManager = LplusAnimationManager.getInstance();

		if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) {
			setBackgroundResource(R.drawable.loginbutton_facebook_bg_logout);
		} else {

		}

		setOnClickListener(new LplusLoginButtonOnClickListener());
	}

	private LplusFacebook getLplusFacebook() {
		if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK)
			return (LplusFacebook) mFramework;
		else
			return null;
	}

	private final class LplusLoginButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			LplusAnimationUtil.spin(LplusLoginButton.this);
			
			if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) {
				LplusFacebook lplusFacebook = getLplusFacebook();
				if (lplusFacebook.isSessionValid()) {
					lplusFacebook.logout(new FacebookLogoutRequestListener());
				} else {
					lplusFacebook.login((Activity) getContext(), new FacebookLoginDialogListener());
				}	
			} else {

			}
		}
	}

	private final class FacebookLoginDialogListener extends LplusBaseDialogListener {
		public void onComplete(Bundle values) {
			setBackgroundResource(R.drawable.loginbutton_facebook_bg_login);

			if (mListener != null) {
				if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) {
					LplusFacebook lplusFacebook = getLplusFacebook();					
					lplusFacebook.getUserInfo("me", new FacebookUserInfoRequestListener());
				} else {
				}
			}
		}
		
	    public void onError(DialogError e) {
	    	super.onError(e);
	    }
	}

	private final class FacebookLogoutRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			setBackgroundResource(R.drawable.loginbutton_facebook_bg_logout);
			mListener.onComplete(null);
		}
	}
	
	private final class FacebookUserInfoRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			try {				
				JSONObject userInfo = Util.parseJson(response);
				if (userInfo != null) {
					mUserInfo.id = userInfo.getString("id");
					mUserInfo.userName = userInfo.getString("username");
					mUserInfo.gender = userInfo.getString("gender");
					mUserInfo.locale = userInfo.getString("locale");
					mUserInfo.firstName = userInfo.getString("first_name");
					mUserInfo.lastName = userInfo.getString("last_name");
					
					
					if (mFramework.getType() == LplusFrameworkType.LPLUS_TYPE_FACEBOOK) {
						LplusFacebook lplusFacebook = getLplusFacebook();		
						lplusFacebook.getUserProfilePic(mUserInfo.userName, mListener);
					} else {
						
					}
				}
                
			} catch (FacebookError e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
