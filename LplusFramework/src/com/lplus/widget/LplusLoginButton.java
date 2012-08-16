package com.lplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.lplus.facebook.R;

public class LplusLoginButton extends ImageButton implements LplusButton {

	public LplusLoginButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LplusLoginButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginButton(Context context) {
		super(context);
	}
	
	public void init(LplusType type) {
		if (type == LplusType.LPLUS_TYPE_FACEBOOK) {
			setBackgroundResource(R.drawable.facebook_icon);
		}
	}
}
