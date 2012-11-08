package com.lplus.animation;

import com.lplus.widget.LplusLoginButton;

import android.view.View;

public class LplusAnimationUtil {

	public static void spin(View view) {
		LplusAnimation anim = LplusAnimationManager.getInstance().createAnimation(0, 500, LplusAnimation.FUNC_EASE_OUT);
        anim.addProperty(new LplusAnimationProp(LplusAnimationProp.PROP_ROTATE_Y, view, 0.0f, 360.0f));
        anim.start();
	}
}
