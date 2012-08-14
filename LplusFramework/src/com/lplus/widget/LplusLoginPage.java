package com.lplus.widget;

import com.lplus.facebook.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public class LplusLoginPage extends LinearLayout implements LplusPage {
	
	LplusLoginButton loginBtn;

	public LplusLoginPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}

	public LplusLoginPage(Context context) {
		super(context);
		
		init();
	}
	
	public void init() {
		loginBtn = (LplusLoginButton)findViewById(R.id.lgoinbutton);
		loginBtn.init();
	}
}
