package com.lplus.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lplus.facebook.R;

public class LplusPaper extends FrameLayout implements LplusWidget,
		OnClickListener {

	ImageView mBg1;
	ImageView mBg2;
	ImageView mBg3;
	ImageView mBg4;
	
	float mBg1OriginY;
	float mBg2OriginY;
	float mBg3OriginY;
	float mBg4OriginY;
	

	public LplusPaper(Context context) {
		super(context);

		this.setBackgroundColor(0x00000000);
		this.setOnClickListener(this);

		this.post(new Runnable() {

			public void run() {

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
				Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, LplusPaper.this.getHeight()* 1/4, LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				Bitmap bitmap3 = Bitmap.createBitmap(bitmap, 0, LplusPaper.this.getHeight()* 2/4, LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				Bitmap bitmap4 = Bitmap.createBitmap(bitmap, 0, LplusPaper.this.getHeight()* 3/4, LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				
				LplusPaper.this.setLayoutParams(new LayoutParams(getRootView().getWidth(), getRootView().getHeight()));
				
				LayoutParams lp1 = new LayoutParams(LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				mBg1 = new ImageView(getContext());
				mBg1.setImageBitmap(bitmap1);
				LplusPaper.this.addView(mBg1, lp1);
				mBg1OriginY = mBg1.getY();

				LayoutParams lp2 = new LayoutParams(LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				mBg2 = new ImageView(getContext());
				mBg2.setTranslationY(LplusPaper.this.getHeight() * 1/4);
				mBg2.setImageBitmap(bitmap2);
				LplusPaper.this.addView(mBg2, lp2);
				mBg2OriginY = mBg2.getY();
				
				LayoutParams lp3 = new LayoutParams(LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				mBg3 = new ImageView(getContext());
				mBg3.setTranslationY(LplusPaper.this.getHeight() * 2/4);
				mBg3.setImageBitmap(bitmap3);
				LplusPaper.this.addView(mBg3, lp3);
				mBg3OriginY = mBg3.getY();
				
				LayoutParams lp4 = new LayoutParams(LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/4);
				mBg4 = new ImageView(getContext());
				mBg4.setTranslationY(LplusPaper.this.getHeight() * 3/4);
				mBg4.setImageBitmap(bitmap4);
				LplusPaper.this.addView(mBg4, lp4);
				mBg4OriginY = mBg4.getY();

			}
		});

	}

	static float angle = 0.0f;
	static float dir = 1.0f;

	public void onClick(View v) {
		float scaleFactor = Math.abs(90.0f - angle) / 90;
		double radian = angle / 180 * Math.PI;
				 
		float height = (float) (Math.cos(radian) * (mBg1.getHeight() / 2));
		float diffHeight1 = mBg1.getHeight()/2 - height;

		mBg1.setRotationX(angle);
		mBg1.setTranslationY(mBg1OriginY - diffHeight1);
		
		height = (float) (Math.cos(radian) * (mBg2.getHeight() / 2));
		float diffHeight2 = mBg2.getHeight()/2 - height;
		
		mBg2.setRotationX(-angle);
		mBg2.setTranslationY(mBg2OriginY - diffHeight2 - diffHeight1 * 2);
		
		height = (float) (Math.cos(radian) * (mBg3.getHeight() / 2));
		float diffHeight3 = mBg3.getHeight()/2 - height;
		
		mBg3.setRotationX(angle);
		mBg3.setTranslationY(mBg3OriginY - diffHeight3 - diffHeight2 * 2 - diffHeight1 * 2);
		
		height = (float) (Math.cos(radian) * (mBg4.getHeight() / 2));
		float diffHeight4 = mBg4.getHeight()/2 - height;
		
		mBg4.setRotationX(-angle);
		mBg4.setTranslationY(mBg4OriginY - diffHeight4 - diffHeight3 * 2 - diffHeight2 * 2 - diffHeight1 * 2);

		//Log.e("gooson", "diff " + (mBg1.getHeight() - diffHeight1 * 2) + " / " + (mBg2OriginY - diffHeight2 - diffHeight1 * 2));
		Log.e("gooson", "diff " + mBg1.getY());
		
		angle += dir * 10.0f;
		if (angle > 90.0f) {
			dir *= -1.0f;
			angle = 90.0f;
		} else if (angle < 0.0f) {
			dir *= -1.0f;
			angle = 0.0f;
		}
	}
}
