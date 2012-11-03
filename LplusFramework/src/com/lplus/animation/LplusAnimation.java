package com.lplus.animation;

import java.util.ArrayList;

public class LplusAnimation {
    public static final int STATE_STOPPED = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_FINISHED = 2;

    public static final int FUNC_LINEAR = 0;
    public static final int FUNC_EASE_IN = 1;
    public static final int FUNC_EASE_OUT = 2;
    public static final int FUNC_EASE_IN_OUT = 3;
    public static final int FUNC_EASE_BOUNCE_OUT = 4;

    public static final int ENDTYPE_STAY_VALUE = 0;
    public static final int ENDTYPE_START_VALUE = 1;
    public static final int ENDTYPE_END_VALUE = 2;

    private LplusAnimationCallback mCallback = null;
    private LplusAnimation mNextAnimation = null;
    private ArrayList<LplusAnimationProp> mPropList = null;

    private int mStatus = STATE_STOPPED;
    private int mEaseFunc = FUNC_LINEAR;
    private long mDelay = 0;
    private long mDuration = 0;
    private long mStartTime = 0;
    private long mEndTime = 0;

    public LplusAnimation(long delay, long duration, int interpolationType) {
	mStatus = STATE_STOPPED;
	mDelay = delay;
	mDuration = duration;
	mEaseFunc = interpolationType;
	mPropList = new ArrayList<LplusAnimationProp>();
    }

    public boolean isRunning() {
	return (mStatus == STATE_RUNNING);
    }

    public boolean isFinished() {
	return (mStatus == STATE_FINISHED);
    }

    public void setCallback(LplusAnimationCallback callback) {
	mCallback = callback;
    }

    public void start() {

	if (mStatus == STATE_RUNNING)
	    return;

	if (mCallback != null)
	    mCallback.onStart();

	mStartTime = System.currentTimeMillis() + mDelay;
	mEndTime = mStartTime + mDuration;

	mStatus = STATE_RUNNING;
    }

    public void cancel(int endType) {

	if (mStatus == STATE_FINISHED)
	    return;

	switch (endType) {
	case ENDTYPE_STAY_VALUE:
	    break;
	case ENDTYPE_START_VALUE:
	    for (int i = 0; i < mPropList.size(); i++)
		mPropList.get(i).progress(0.0f);
	    break;
	case ENDTYPE_END_VALUE:
	    for (int i = 0; i < mPropList.size(); i++)
		mPropList.get(i).progress(1.0f);
	    break;
	}

	if (mCallback != null)
	    mCallback.onEnd(endType);

	mStatus = STATE_FINISHED;
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
	float t = (float) (time - mStartTime) / (float) mDuration;

	if (time < mStartTime)
	    return 0.0f;
	else if (time > mEndTime)
	    return 1.0f;

	switch (mEaseFunc) {
	case FUNC_LINEAR:
	    return t;
	case FUNC_EASE_IN:
	    return (float) Math.pow(t, 3.0f);
	case FUNC_EASE_OUT:
	    return (float) Math.pow((t - 1), 3.0f) + 1.0f;
	case FUNC_EASE_IN_OUT:
	    if (t < 0.5f)
		return (float) Math.pow(t, 3.0f);
	    else
		return (float) Math.pow((t - 1), 3.0f) + 1.0f;
	case FUNC_EASE_BOUNCE_OUT:
	    if (t < 1.0f / 2.75f) {
		return 7.5625f * t * t;
	    } else if (t < 2.0f / 2.75f) {
		t -= 1.5f / 2.75f;
		return 7.5625f * t * t + 0.75f;
	    } else if (t < 2.5f / 2.75f) {
		t -= 2.25f / 2.75f;
		return 7.5625f * t * t + 0.9375f;
	    } else {
		t -= 2.625f / 2.75f;
		return 7.5625f * t * t + 0.984375f;
	    }
	default:
	    break;
	}

	return 0.0f;
    }

    public void doAnimation(long now) {
	if (mStatus == STATE_RUNNING && now > mStartTime) {

	    if (now > mEndTime) {
		cancel(ENDTYPE_END_VALUE);
	    } else {
		float value = animate(now);
		for (int i = 0; i < mPropList.size(); i++) {
		    mPropList.get(i).progress(value);
		}
	    }

	}

    }

}
