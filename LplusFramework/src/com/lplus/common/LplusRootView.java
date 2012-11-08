package com.lplus.common;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.FrameLayout;

import com.lplus.animation.LplusAnimationManager;

public class LplusRootView extends FrameLayout {

	LplusAnimationManager mAnimManager;

	public LplusRootView(Context context) {
		super(context);
		setWillNotDraw(false);
		mAnimManager = LplusAnimationManager.getInstance();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mAnimManager != null) {
			mAnimManager.doAnimations();
			invalidate();
		}
	}

}
