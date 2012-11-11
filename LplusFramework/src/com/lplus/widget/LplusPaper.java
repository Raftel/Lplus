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
	float mBg1OriginY;
	ImageView mBg2;

	public LplusPaper(Context context) {
		super(context);

		this.setBackgroundColor(0x00000000);
		this.setOnClickListener(this);

		this.post(new Runnable() {

			public void run() {

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
				Bitmap upperBitmap = Bitmap.createBitmap(bitmap, 0, 0, LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/2);
				Bitmap lowwerBitmap = Bitmap.createBitmap(bitmap, 0, LplusPaper.this.getHeight()/2, LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/2);
				
				LplusPaper.this.setLayoutParams(new LayoutParams(getRootView().getWidth(), getRootView().getHeight()));
				
				LayoutParams lp1 = new LayoutParams(LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/2);
				mBg1 = new ImageView(getContext());
				mBg1.setImageBitmap(upperBitmap);
				LplusPaper.this.addView(mBg1, lp1);
				mBg1OriginY = mBg1.getY();

				LayoutParams lp2 = new LayoutParams(LplusPaper.this.getWidth(), LplusPaper.this.getHeight()/2);
				mBg2 = new ImageView(getContext());
				mBg2.setTranslationY(LplusPaper.this.getHeight()/2);
				mBg2.setImageBitmap(lowwerBitmap);
				//LplusPaper.this.addView(mBg2, lp2);

			}
		});

	}

	static float angle = 0.0f;
	static float dir = 1.0f;

	public void onClick(View v) {
		float scaleFactor = Math.abs(90.0f - angle) / 90;
		mBg1.setRotationX(-angle);
		//mBg1.setScaleY(scaleFactor);
		mBg1.setTranslationY((float) (mBg1OriginY + (mBg1.getHeight() - Math.cos(-angle) * mBg1.getHeight() /2)));
		Log.e("gooson", "y : " + mBg1.getY() + "// scale :" + (Math.cos(angle) * mBg1.getHeight() /2));

		mBg2.setRotationX(angle);
		mBg2.setScaleY(scaleFactor);
		mBg2.setTranslationY(mBg2.getY() + -dir * ((10.0f / 90) * mBg2.getHeight()) / 2);

		angle += dir * 10.0f;
		if (angle % 90.0f == 0.0f)
			dir *= -1.0f;
	}
}
