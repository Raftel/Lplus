package com.lplus.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
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

	public LplusLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LplusLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginButton(Context context) {
		super(context);
	}
	
	public void init(LplusFramework framework) {
		mFramework = framework;
		
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
		}
	}
	
	private final class FacebookLogoutRequestListener extends LplusBaseRequestListener {
		public void onComplete(String response, Object state) {
			setBackgroundResource(R.drawable.loginbutton_facebook_bg_logout);	
		}
	}
}
