package com.lplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lplus.common.LplusFramework;
import com.lplus.facebook.LplusFacebook;
import com.lplus.facebook.R;


public class LplusLoginPage extends LinearLayout implements LplusPage {
	
	LinearLayout		mLayout;
	LplusLoginButton 	mLoginBtn;
	LplusFramework		mFramework;

	public LplusLoginPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginPage(Context context) {
		super(context);
	}
	
	public void init(LplusFramework framework) {
		mFramework = framework;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayout = (LinearLayout) inflater.inflate(R.layout.lplusloginpage, null);
		this.addView(mLayout);
		this.setGravity(Gravity.CENTER);

		mLoginBtn = (LplusLoginButton)findViewById(R.id.lgoinbutton);
		mLoginBtn.init(mFramework);
	}
	
	private LplusFacebook getFacebook() {
		return (LplusFacebook)mFramework;
	}
}
