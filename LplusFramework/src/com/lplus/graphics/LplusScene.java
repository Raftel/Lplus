package com.lplus.graphics;

import java.util.ArrayList;

public class LplusScene {

	private ArrayList<LplusNode> mRenderNodeList;
	private ArrayList<LplusLight> mLightList;
	private LplusCamera mCamera;

	public LplusScene() {
		mRenderNodeList = new ArrayList<LplusNode>();
		mLightList = new ArrayList<LplusLight>();
	}

	public boolean addRenderNode(LplusNode root) {
		if (root != null)
			return mRenderNodeList.add(root);
		return false;
	}

	public boolean removeRenderNode(LplusNode root) {
		if (root != null)
			return mRenderNodeList.remove(root);
		return false;
	}

	public void removeAllRenderNodes() {
		mRenderNodeList.clear();
	}

	public ArrayList<LplusNode> getRenderNodeList() {
		return mRenderNodeList;
	}
	
	public boolean addLight(LplusLight light) {
		if (light != null)
			return mLightList.add(light);
		return false;
	}

	public boolean removeLight(LplusLight light) {
		if (light != null)
			return mLightList.remove(light);
		return false;
	}

	public void removeAllLights() {
		mLightList.clear();
	}

	public ArrayList<LplusLight> getLightList() {
		return mLightList;
	}

	public void setCamera(LplusCamera camera) {
		mCamera = camera;
	}

	public LplusCamera getCamera() {
		return mCamera;
	}
}
