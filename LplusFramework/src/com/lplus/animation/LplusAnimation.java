package com.lplus.animation;

import java.util.ArrayList;

public class LplusAnimation {
    public static final int STOPPED = 0;
    public static final int RUNNING = 1;
    public static final int FINISHED = 2;

    public static final int FUNC_LINEAR = 0;
    public static final int FUNC_EASE_IN = 1;
    public static final int FUNC_EASE_OUT = 2;

    private LplusAnimationCallback mCallback = null;
    private LplusAnimation mNextAnimation = null;
    private ArrayList<LplusAnimationProp> mPropList = null;

    private int mStatus = STOPPED;
    private int mEaseFunc = FUNC_LINEAR;
    private long mDelay = 0;
    private long mDuration = 0;
    private long mStartTime = 0;
    private long mEndTime = 0;
    private long mStopedTime = 0;

    public LplusAnimation(long delay, long duration, int interpolationType) {
	mStatus = STOPPED;
	mDelay = delay;
	mDuration = duration;
	mEaseFunc = interpolationType;
	mPropList = new ArrayList<LplusAnimationProp>();
    }

    public boolean isRunning() {
	return (mStatus == RUNNING);
    }

    public boolean isFinished() {
	return (mStatus == FINISHED);
    }

    public void setCallback(LplusAnimationCallback callback) {
	mCallback = callback;
    }

    public void start() {

	if (mStatus == RUNNING)
	    return;

	if (mCallback != null)
	    mCallback.onStart();

	mStartTime = System.currentTimeMillis() + mDelay;
	mEndTime = mStartTime + mDuration;

	mStatus = RUNNING;
    }

    public void stop() {

	if (mStatus != RUNNING)
	    return;

	if (mCallback != null)
	    mCallback.onStop();

	mStopedTime = System.currentTimeMillis();

	mStatus = STOPPED;
    }

    public void resume() {

	if (mStatus != STOPPED)
	    return;

	if (mCallback != null)
	    mCallback.onResume();

	mStartTime = System.currentTimeMillis() - mStopedTime + mStartTime;
	mEndTime = mStartTime + mDuration;

	mStatus = RUNNING;
    }

    public void finish() {

	if (mStatus == FINISHED)
	    return;

	for (int i = 0; i < mPropList.size(); i++) {
	    mPropList.get(i).progress(1.0f);
	}

	if (mCallback != null)
	    mCallback.onFinish();

	mStatus = FINISHED;
	if (mNextAnimation != null)
	    mNextAnimation.start();
    }

    public void addNextAnimation(LplusAnimation nextAnimation) {
	mNextAnimation = nextAnimation;
    }

    public void addProperty(LplusAnimationProp prop) {
	mPropList.add(prop);
    }

    private float animate(long time) {
	float value = (float) (time - mStartTime) / (float) mDuration;

	if (time < mStartTime)
	    return 0.0f;
	else if (time > mEndTime)
	    return 1.0f;

	switch (mEaseFunc) {
	case FUNC_LINEAR:
	    return value;
	case FUNC_EASE_IN:
	    return (float) Math.pow(value, 3.0f);
	case FUNC_EASE_OUT:
	    return -(float) Math.pow(0.01, value) + 1.01f;
	default:
	    break;
	}

	return 0.0f;
    }

    public void doAnimation(long now) {
	if (mStatus == RUNNING && now > mStartTime) {

	    if (now > mEndTime) {
		finish();
	    } else {
		float value = animate(now);
		for (int i = 0; i < mPropList.size(); i++) {
		    mPropList.get(i).progress(value);
		}
	    }

	}

    }

}
