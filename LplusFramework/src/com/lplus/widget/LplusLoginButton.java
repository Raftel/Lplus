package com.lplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.lplus.facebook.R;

public class LplusLoginButton extends ImageButton implements LplusButton {

	public LplusLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}

	public LplusLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init();
	}

	public LplusLoginButton(Context context) {
		super(context);
		
		init();
	}
	
	public void init() {
		setImageResource(R.drawable.facebook_icon);
	}

}
