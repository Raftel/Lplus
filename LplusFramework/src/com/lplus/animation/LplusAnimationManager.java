package com.lplus.animation;

import java.util.Vector;

public class LplusAnimationManager {

    private Vector<LplusAnimation> mAnims = new Vector<LplusAnimation>();

    public LplusAnimationManager() {
    }

    public LplusAnimation createAnimation(long delay, long duration, int interpolationType) {
	LplusAnimation newAnimation = new LplusAnimation(delay, duration, interpolationType);
	mAnims.add(newAnimation);

	return newAnimation;
    }

    public void doAnimations() {
	if (!hasAnimation()){
	    return;
	}

	long now = System.currentTimeMillis();

	for (int i = 0; i < mAnims.size();) {
	    if (mAnims.get(i).isRunning())
		mAnims.get(i).doAnimation(now);

	    if (mAnims.get(i).isFinished()) {
		mAnims.remove(i);
	    } else {
		i++;
	    }
	}
    }

    public boolean hasAnimation() {
	return mAnims.size() > 0 ? true : false;
    }

    public void cancelAnimation(LplusAnimation anim) {
	mAnims.remove(anim);
	anim = null;
    }

    public void stopAnimations() {
	for (int i = 0; i < mAnims.size(); i++) {
	    mAnims.get(i).stop();
	}
    }

    public void resumeAnimations() {
	for (int i = 0; i < mAnims.size(); i++) {
	    mAnims.get(i).resume();
	}
    }

}
