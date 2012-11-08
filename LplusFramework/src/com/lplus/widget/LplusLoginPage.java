package com.lplus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.lplus.animation.LplusAnimationUtil;
import com.lplus.common.LplusFramework;
import com.lplus.common.LplusUtil;
import com.lplus.facebook.LplusFacebook.BitmapAsyncTaskListener;
import com.lplus.facebook.R;


public class LplusLoginPage extends LinearLayout implements LplusPage {
	
	private static final int BUTTON_WIDTH_DIP = 56;
	private static final int BUTTON_HEIGHT_DIP = 56;
	private static final int BUTTON_GROUP_ROW = 2;
	private static final int BUTTON_MAX_NUM = 6;
	private static final int BUTTON_MAX_ROW = 3;
	
	
	ImageView					mProfile;
	LinearLayout				mLayout;
	LplusButtonGroup			mLoginButtonGroup;
	TableRow					mTableRow[];
	LplusLoginButton 			mLoginBtn[];
	LoginButtonListener			mLoginBtnListener[];
	int 						mNumButtons = 0;
	int							mAddedButtons = 0;
	
	Bitmap mMaskBitmap;
	Bitmap mDefaultProfile;

	private LplusLoginPage(Context context, AttributeSet attrs) {
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
						
		mProfile = (ImageView)findViewById(R.id.loginprofile);
		mLoginButtonGroup = (LplusButtonGroup)findViewById(R.id.loginbuttongroup);
		mTableRow = new TableRow[BUTTON_GROUP_ROW];
		
		for (int i = 0; i < BUTTON_GROUP_ROW; i++) {
			mTableRow[i] = new TableRow(getContext());
			mTableRow[i].setGravity(Gravity.CENTER);
			mLoginButtonGroup.addView(mTableRow[i], new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
		
		mNumButtons = LplusUtil.getMin(numButtons, BUTTON_MAX_NUM);
		mLoginBtn = new LplusLoginButton[mNumButtons];
		mLoginBtnListener = new LoginButtonListener[mNumButtons];
		mMaskBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.loginpage_profile_mask);
		mDefaultProfile = BitmapFactory.decodeResource(getResources(),R.drawable.loginpage_default_profile);
		setProfilePic(mDefaultProfile);
	}
	
	public void addLoginButton(LplusFramework framework) {
		
		if (mAddedButtons == mNumButtons)
			return; 
		
		int width = LplusUtil.dpToPx(getResources().getDisplayMetrics(), BUTTON_WIDTH_DIP);
		int height = LplusUtil.dpToPx(getResources().getDisplayMetrics(), BUTTON_HEIGHT_DIP);
				
		mLoginBtn[mAddedButtons] = new LplusLoginButton(getContext());
		mLoginBtnListener[mAddedButtons] = new LoginButtonListener();
		mLoginBtn[mAddedButtons].init(framework, mLoginBtnListener[mAddedButtons]);
				
		int row = LplusUtil.getMin(mNumButtons/2, BUTTON_MAX_ROW);
		mTableRow[mAddedButtons < row ? 0 : 1].addView(mLoginBtn[mAddedButtons], width, width);
		
		mAddedButtons++;
	}


	static float dir = 1.0f;
	static float rotationX = 0.0f;
	static float scaleX = 0.0f;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
			
		Paint pnt = new Paint();
		String str = "Tube";
		
		pnt.setTextSize(150);
		pnt.setTextAlign(Paint.Align.CENTER);
		pnt.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		canvas.drawText(str, canvas.getWidth() / 2, mLayout.getTop() - 64, pnt);
		
		invalidate();
	}
	
	public void setProfilePic(Bitmap pic) {
		if (pic == null) {
			pic = mDefaultProfile;
		}
		
		Bitmap scaledMask = Bitmap.createScaledBitmap(mMaskBitmap, pic.getWidth(), pic.getHeight(), true);
		
		Bitmap result = Bitmap.createBitmap(pic.getWidth(), pic.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
        canvas.drawBitmap(pic, 0, 0, null); 
		
		Paint paint2 = new Paint();
        paint2.setFilterBitmap(false);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT) );
        canvas.drawBitmap(scaledMask, 0, 0, paint2); 
      
        mProfile.setImageBitmap(result);
	}
	
	public final class LoginButtonListener implements BitmapAsyncTaskListener {
		public void onComplete(Bitmap pic) {
			LplusAnimationUtil.spin(mProfile);
			setProfilePic(pic);
		}
	}
}
