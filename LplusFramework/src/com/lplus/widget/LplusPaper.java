package com.lplus.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.lplus.animation.LplusAnimation;
import com.lplus.animation.LplusAnimationManager;
import com.lplus.animation.LplusAnimationProp;
import com.lplus.facebook.R;

public class LplusPaper extends FrameLayout implements LplusWidget, OnClickListener {

	private static final int CONTENTS_NUM = 4;

	double mCurrentAngle = 0.0f;

	ArrayList<FoldView> mContents;

	private class FoldView extends ImageView {
		public float mOrigin;

		public FoldView(Context context) {
			super(context);
			mOrigin = 0;
		}
	}

	public LplusPaper(Context context) {
		super(context);

		this.setBackgroundColor(0x00000000);
		this.setOnClickListener(this);

		this.post(new Runnable() {

			public void run() {

				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);

				mContents = new ArrayList<FoldView>();

				LplusPaper.this.setLayoutParams(new LayoutParams(getRootView().getWidth(), getRootView().getHeight()));

				for (int i = 0; i < CONTENTS_NUM; i++) {
					FoldView view = new FoldView(getContext());

					LayoutParams lp = new LayoutParams(LplusPaper.this.getWidth(), LplusPaper.this.getHeight() / CONTENTS_NUM);
					LplusPaper.this.addView(view, lp);

					view.setTranslationY(LplusPaper.this.getHeight() * i / CONTENTS_NUM);
					view.mOrigin = view.getY();

					Bitmap viewBg = Bitmap.createBitmap(bitmap, 0, LplusPaper.this.getHeight() * i / CONTENTS_NUM, LplusPaper.this.getWidth(), LplusPaper.this.getHeight() / CONTENTS_NUM);
					view.setImageBitmap(viewBg);

					mContents.add(view);
				}
			}
		});

	}

	private void layout() {

	}

	static float dir = 1.0f;

	private void foldAnimation() {

		LplusAnimationManager animManager = LplusAnimationManager.getInstance();
		LplusAnimation anim = animManager.createAnimation(0, 5000, LplusAnimation.FUNC_EASE_OUT);

		float direction = 1.0f;
		float prevDiff = 0.0f;

		for (int i = 0; i < CONTENTS_NUM; i++) {
			FoldView view = mContents.get(i);

			double radian = Math.PI * 90.0f / 180.0f;
			double newHeight = Math.cos(radian) * ((double) view.getHeight() / (double) 2.0f);
			float diff = (float) ((double) view.getHeight() / (double) 2.0f - newHeight);

			if (dir == 1.0f) {
				anim.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_ROTATE_X, view, 0, 90 * direction));
				anim.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_TRANSLATE_Y, view, view.getTranslationY(), view.mOrigin - diff - prevDiff * 2));
			} else {
				anim.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_ROTATE_X, view, 90 * direction, 0));
				anim.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_TRANSLATE_Y, view, view.getTranslationY(), view.mOrigin));
			}

			prevDiff += diff;
			direction *= -1.0f;
		}
		anim.start();

		dir *= -1.0f;
	}

	public void onClick(View v) {
		foldAnimation();
	}
}
