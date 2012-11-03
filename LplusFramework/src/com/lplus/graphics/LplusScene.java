package com.lplus.graphics;

import java.util.ArrayList;

public class LplusScene {

    private ArrayList<LplusNode> mRenderNodeList;
    private LplusCamera mCamera;

    public LplusScene() {
	mRenderNodeList = new ArrayList<LplusNode>();
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

    public void setCamera(LplusCamera camera) {
	mCamera = camera;
    }

    public LplusCamera getCamera() {
	return mCamera;
    }
}
