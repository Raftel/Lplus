package com.lplus.animation;

import android.view.View;

import com.lplus.graphics.LplusNode;

public class LplusAnimationProp {

    public static final int PROP_CUSTOM = 0;
    public static final int PROP_TRANSLATE_X = 1;
    public static final int PROP_TRANSLATE_Y = 2;
    public static final int PROP_ROTATE_X = 3;
    public static final int PROP_ROTATE_Y = 4;
    public static final int PROP_ROTATE_Z = 5;
    public static final int PROP_SCALE_X = 6;
    public static final int PROP_SCALE_Y = 7;
    
    private View mView = null;
    private LplusNode mNode = null;
    private int mPropType = PROP_CUSTOM;
    private CustomCallback mCustomCallback = null;
    private float mStartValue;
    private float mEndValue;
    private float mDelta;

    public LplusAnimationProp(int propType, View view, float fromValue, float toValue) {
	mPropType = propType;
	mView = view;
	mStartValue = fromValue;
	mEndValue = toValue;
	mDelta = mEndValue - mStartValue;
    }
    
    public LplusAnimationProp(int propType, LplusNode node, float fromValue, float toValue) {
	mPropType = propType;
	mNode = node;
	mStartValue = fromValue;
	mEndValue = toValue;
	mDelta = mEndValue - mStartValue;
    }

    public LplusAnimationProp(CustomCallback callback, float fromValue, float toValue) {
	mPropType = PROP_CUSTOM;
	mCustomCallback = callback;
	mStartValue = fromValue;
	mEndValue = toValue;
	mDelta = mEndValue - mStartValue;
    }

    // TODO : review Model animation 
    public void progress(float value) {
	float currentValue = mStartValue + mDelta * value;
	
	switch (mPropType) {
	case PROP_CUSTOM:
	    if (mCustomCallback != null)
		mCustomCallback.onProgress(currentValue);
	    break;
	case PROP_TRANSLATE_X:
	    if (mView != null)
		mView.setTranslationX(currentValue);
	    
	    if (mNode != null)
		mNode.setTranslationX(currentValue);
	    break;
	case PROP_TRANSLATE_Y:
	    if (mView != null)
		mView.setTranslationY(currentValue);
	    
	    if (mNode != null)
		mNode.setTranslationY(currentValue);
	    break;
	case PROP_ROTATE_X:
	    if (mView != null)
		mView.setRotationX(currentValue);
	    
	    if (mNode != null)
		mNode.setRotationX(currentValue);
	    break;
	case PROP_ROTATE_Y:
	    if (mView != null)
		mView.setRotationY(currentValue);
	    
	    if (mNode != null)
		mNode.setRotationY(currentValue);
	    break;
	case PROP_ROTATE_Z:
	    if (mView != null)
		mView.setRotation(currentValue);
	    
	    if (mNode != null)
		mNode.setRotationZ(currentValue);
	    break;
	case PROP_SCALE_X:
	    if (mView != null)
		mView.setScaleX(currentValue);
	    
	    if (mNode != null)
		mNode.setScaleX(currentValue);
	    break;
	case PROP_SCALE_Y:
	    if (mView != null)
		mView.setScaleY(currentValue);
	    
	    if (mNode != null)
		mNode.setScaleY(currentValue);
	    break;

	default:
	    break;
	}
    }
    
    public interface CustomCallback {
	public void onProgress(float progress);
    }
}
