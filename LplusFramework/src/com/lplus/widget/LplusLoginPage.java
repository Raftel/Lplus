package com.lplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lplus.facebook.R;


public class LplusLoginPage extends LinearLayout implements LplusPage {
	
	LinearLayout		layout;
	LplusLoginButton 	loginBtn;

	public LplusLoginPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginPage(Context context) {
		super(context);
	}
	
	public void init(LplusType type) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = (LinearLayout) inflater.inflate(R.layout.lplusloginpage, null);
		this.addView(layout);
		this.setGravity(Gravity.CENTER);

		loginBtn = (LplusLoginButton)findViewById(R.id.lgoinbutton);
		loginBtn.init(type);
	}
}
