package com.lplus.animation;

import android.view.View;

public class LplusAnimationUtil {

	public static void spin(View view) {
		LplusAnimation anim = LplusAnimationManager.getInstance().createAnimation(0, 500, LplusAnimation.FUNC_EASE_OUT);
        anim.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_ROTATE_Y, view, 0.0f, 360.0f));
        anim.start();
	}
	
	public static void swing(View view) {
		LplusAnimation animRight = LplusAnimationManager.getInstance().createAnimation(0, 1000, LplusAnimation.FUNC_EASE_OUT);
		animRight.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_TRANSLATE_X, view, view.getTranslationX(), view.getRootView().getWidth()));
        
		LplusAnimation animLeft = LplusAnimationManager.getInstance().createAnimation(500, 500, LplusAnimation.FUNC_EASE_OUT);
        animLeft.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_TRANSLATE_X, view, view.getRootView().getWidth(), -view.getRootView().getWidth()));
        
        animRight.addNextAnimation(animLeft);
        animRight.start();
	}
}
