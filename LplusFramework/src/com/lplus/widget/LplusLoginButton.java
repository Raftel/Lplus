package com.lplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.lplus.common.LplusFramework;
import com.lplus.common.LplusFrameworkType;
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
			setBackgroundResource(R.drawable.loginbutton_bg3);
			//setImageResource(R.drawable.loginbutton_bg3);
		}
		
		//setBackgroundColor(0x00000000);
		setAdjustViewBounds(true); 
		
		
		setOnClickListener(new LplusLoginButtonOnClickListener());
	}
	
	private final class LplusLoginButtonOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
		}
	}
	
	private LplusFacebook getLplusFacebook() {
		return (LplusFacebook)mFramework;
	}
}
