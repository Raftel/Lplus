package com.lplus.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.lplus.common.LplusFramework;
import com.lplus.common.LplusUtil;
import com.lplus.facebook.R;


public class LplusLoginPage extends LinearLayout implements LplusPage {
	
	private static final int BUTTON_WIDTH_DIP = 56;
	private static final int BUTTON_HEIGHT_DIP = 56;
	private static final int BUTTON_GROUP_ROW = 2;
	private static final int BUTTON_MAX_NUM = 6;
	private static final int BUTTON_MAX_ROW = 3;
	
	LinearLayout		mLayout;
	LplusButtonGroup	mLoginButtonGroup;
	TableRow			mTableRow[];
	LplusLoginButton 	mLoginBtn[];
	int 				mNumButtons = 0;
	int					mAddedButtons = 0;

	public LplusLoginPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LplusLoginPage(Context context) {
		super(context);
	}
	
	public void init(int numButtons) {
		this.setBackgroundResource(R.drawable.loginpage_bg_yellow);

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLayout = (LinearLayout) inflater.inflate(R.layout.lplusloginpage, null);
		this.addView(mLayout);
		this.setGravity(Gravity.CENTER);
						
		mLoginButtonGroup = (LplusButtonGroup)findViewById(R.id.loginbuttongroup);
		mTableRow = new TableRow[BUTTON_GROUP_ROW];
		
		for (int i = 0; i < BUTTON_GROUP_ROW; i++) {
			mTableRow[i] = new TableRow(getContext());
			mTableRow[i].setGravity(Gravity.CENTER);
			mLoginButtonGroup.addView(mTableRow[i], new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		
		mNumButtons = LplusUtil.getMin(numButtons, BUTTON_MAX_NUM);
		mLoginBtn = new LplusLoginButton[mNumButtons];
	}
	
	public void addLoginButton(LplusFramework framework) {
		
		if (mAddedButtons == mNumButtons)
			return; 
		
		int width = LplusUtil.dpToPx(getResources().getDisplayMetrics(), BUTTON_WIDTH_DIP);
		int height = LplusUtil.dpToPx(getResources().getDisplayMetrics(), BUTTON_HEIGHT_DIP);
		
		mLoginBtn[mAddedButtons] = new LplusLoginButton(getContext());
		mLoginBtn[mAddedButtons].init(framework);
				
		int row = LplusUtil.getMin(mNumButtons/2, BUTTON_MAX_ROW);
		mTableRow[mAddedButtons < row ? 0 : 1].addView(mLoginBtn[mAddedButtons], width, width);
		
		mAddedButtons++;
	}
}
