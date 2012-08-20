package com.lplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.lplus.common.LplusFramework;
import com.lplus.common.LplusUtil;
import com.lplus.facebook.LplusFacebook;
import com.lplus.facebook.R;


public class LplusLoginPage extends LinearLayout implements LplusPage {
	
	private static final int BUTTON_WIDTH_DIP = 128;
	private static final int BUTTON_HEIGHT_DIP = 128;
	
	LinearLayout		mLayout;
	LplusButtonGroup	mLoginButtonGroup;
	LplusLoginButton 	mLoginBtn;
	LplusFramework		mFramework;

	public LplusLoginPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginPage(Context context) {
		super(context);
	}
	
	public void init(LplusFramework framework) {
		this.setBackgroundResource(R.drawable.loginpage_bg);
		
		mFramework = framework;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayout = (LinearLayout) inflater.inflate(R.layout.lplusloginpage, null);
		this.addView(mLayout);
		this.setGravity(Gravity.CENTER);
		
		mLoginBtn = new LplusLoginButton(getContext());
		mLoginBtn.init(mFramework);
		
		int width = LplusUtil.dpToPx(getResources().getDisplayMetrics(), BUTTON_WIDTH_DIP);
		int height = LplusUtil.dpToPx(getResources().getDisplayMetrics(), BUTTON_HEIGHT_DIP);
				

		LplusLoginButton a = new LplusLoginButton(getContext());
		a.init(mFramework);
						
		mLoginButtonGroup = (LplusButtonGroup)findViewById(R.id.loginbuttongroup);
		TableRow tr = new TableRow(getContext());
		tr.addView(mLoginBtn, width, width);
		
		TableRow tr2 = new TableRow(getContext());
		
		mLoginButtonGroup.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mLoginButtonGroup.addView(tr2, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		tr.addView(a, width, width);
	}
	
	private LplusFacebook getFacebook() {
		return (LplusFacebook)mFramework;
	}
}
