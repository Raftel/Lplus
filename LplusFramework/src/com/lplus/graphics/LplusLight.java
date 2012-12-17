package com.lplus.graphics;

public class LplusLight extends LplusNode {
	
	public static final int LPLUS_LIGHT_TYPE_POINT = 0x1000;
	public static final int LPLUS_LIGHT_TYPE_DIRECTION = 0x1001;
	public static final int LPLUS_LIGHT_TYPE_SPOT = 0x1002;
	
	private float mRange;
	private float mIntensity;
	private int	mColor;
	private int	mLightType;

	public LplusLight() {
		mRange = 1.0f;
		mIntensity = 1.0f;
		mColor = 0x00000000;
		mLightType = LPLUS_LIGHT_TYPE_POINT;
	}
	
	public float getRange() {
		return mRange;
	}

	public void setRange(float range) {
		this.mRange = range;
	}

	public float getIntensity() {
		return mIntensity;
	}

	public void setIntensity(float intensity) {
		this.mIntensity = intensity;
	}

	public int getColor() {
		return mColor;
	}

	public void setColor(int Color) {
		this.mColor = Color;
	}

	public int getLightType() {
		return mLightType;
	}

	public void setLightType(int lightType) {
		this.mLightType = lightType;
	}
}
