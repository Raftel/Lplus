package com.lplus.graphics;

import java.util.ArrayList;

import android.opengl.Matrix;

public class LplusNode {

    private final float[] mTVector = new float[3];
    private final float[] mRVector = new float[3];
    private final float[] mSVector = new float[3];

    private boolean mModelMatrixDirty;
    private float[] mModelMatrix = new float[16];

    private boolean mVisible;
    private boolean mPickable;

    private LplusNode mParent = null;
    private ArrayList<LplusNode> mChildList = new ArrayList<LplusNode>();

    public static class PickedNode {
	public LplusNode mNode = null;
	public float mDistance = -1;

	public PickedNode() {

	}
    }

    public LplusNode() {
	Matrix.setIdentityM(mModelMatrix, 0);
	mModelMatrixDirty = false;

	mTVector[0] = 0.0f;
	mTVector[1] = 0.0f;
	mTVector[2] = 0.0f;

	mRVector[0] = 0.0f;
	mRVector[1] = 0.0f;
	mRVector[2] = 0.0f;

	mSVector[0] = 1.0f;
	mSVector[1] = 1.0f;
	mSVector[2] = 1.0f;

	mVisible = true;
	mPickable = false;
    }

    public LplusNode getParent() {
	return mParent;
    }

    public void setParent(LplusNode parent) {
	if (mParent != parent) {
	    mParent = parent;
	    setMatrixDirty();
	}
    }

    public ArrayList<LplusNode> getChildList() {
	return mChildList;
    }

    public boolean addChild(LplusNode node) {
	if (node == null)
	    return false;

	LplusNode prevParent = node.getParent();
	if (prevParent == this)
	    return true;

	if (prevParent != null) {
	    prevParent.removeChild(node);
	}

	mChildList.add(node);
	node.setParent(this);

	return true;
    }

    public boolean removeChild(LplusNode node) {
	if (node == null)
	    return false;

	if (node.getParent() != this)
	    return false;

	mChildList.remove(node);
	node.setParent(null);

	return true;
    }

    public void removeAllChild() {
	for (int i = 0; i < mChildList.size(); i++) {
	    mChildList.get(i).setParent(null);
	}
	mChildList.clear();
    }

    public void setTranslation(float x, float y, float z) {
	mTVector[0] = x;
	mTVector[1] = y;
	mTVector[2] = z;

	setMatrixDirty();
    }

    public void setTranslationX(float x) {
	mTVector[0] = x;
	setMatrixDirty();
    }

    public void setTranslationY(float y) {
	mTVector[1] = y;
	setMatrixDirty();
    }

    public void setTranslationZ(float z) {
	mTVector[2] = z;
	setMatrixDirty();
    }

    public float[] getTranslation() {
	return mTVector;
    }

    public void addTranslation(float x, float y, float z) {
	mTVector[0] += x;
	mTVector[1] += y;
	mTVector[2] += z;

	setMatrixDirty();
    }

    public void setRotation(float x, float y, float z) {
	mRVector[0] = x;
	mRVector[1] = y;
	mRVector[2] = z;

	setMatrixDirty();
    }

    public void setRotationX(float x) {
	mRVector[0] = x;
	setMatrixDirty();
    }

    public void setRotationY(float y) {
	mRVector[1] = y;
	setMatrixDirty();
    }

    public void setRotationZ(float z) {
	mRVector[2] = z;
	setMatrixDirty();
    }

    public float[] getRotation() {
	return mRVector;
    }

    public void addRotation(float x, float y, float z) {
	mRVector[0] += x;
	mRVector[1] += y;
	mRVector[2] += z;

	setMatrixDirty();
    }

    public void setScale(float x, float y, float z) {
	mSVector[0] = x;
	mSVector[1] = y;
	mSVector[2] = z;

	setMatrixDirty();
    }

    public void setScaleX(float x) {
	mSVector[0] = x;
	setMatrixDirty();
    }

    public void setScaleY(float y) {
	mSVector[1] = y;
	setMatrixDirty();
    }

    public void setScaleZ(float z) {
	mSVector[2] = z;
	setMatrixDirty();
    }

    public float[] getScale() {
	return mSVector;
    }

    public void addScale(float x, float y, float z) {
	mSVector[0] += x;
	mSVector[1] += y;
	mSVector[2] += z;

	setMatrixDirty();
    }

    public void setMatrixDirty() {
	
	if( mModelMatrixDirty )
	    return;
	
	for (int i = 0; i < mChildList.size(); i++) {
	    LplusNode node = mChildList.get(i);
	    if (node != null)
		node.setMatrixDirty();
	}
	mModelMatrixDirty = true;
    }

    public float[] getModelMatrix() {
	if (mModelMatrixDirty) {
	    if (mParent != null)
		// mModelMatrix = mParent.getModelMatrix();
		System.arraycopy(mParent.getModelMatrix(), 0, mModelMatrix, 0, mModelMatrix.length);
	    else
		Matrix.setIdentityM(mModelMatrix, 0);

	    Matrix.translateM(mModelMatrix, 0, mModelMatrix, 0, mTVector[0], mTVector[1], mTVector[2]);

	    if (mRVector[0] != 0.0f)
		Matrix.rotateM(mModelMatrix, 0, mModelMatrix, 0, mRVector[0], 1, 0, 0);
	    if (mRVector[1] != 0.0f)
		Matrix.rotateM(mModelMatrix, 0, mModelMatrix, 0, mRVector[1], 0, 1, 0);
	    if (mRVector[2] != 0.0f)
		Matrix.rotateM(mModelMatrix, 0, mModelMatrix, 0, mRVector[2], 0, 0, 1);

	    if (mSVector[0] != 1.0f || mSVector[1] != 1.0f || mSVector[2] != 1.0f)
		Matrix.scaleM(mModelMatrix, 0, mModelMatrix, 0, mSVector[0], mSVector[1], mSVector[2]);

	    mModelMatrixDirty = false;
	}

	return mModelMatrix;
    }

    public boolean isVisible() {
	return mVisible;
    }

    public void setVisible(boolean visible) {
	this.mVisible = visible;
    }

    public PickedNode findPickedNode(PickedNode pickedNode, float[] matVP, float[] inNear, float[] inFar) {

	if (pickedNode == null)
	    return null;

	if (isVisible() == false)
	    return null;

	if (isPickable() == true) {
	    float distSq = isPicked(matVP, inNear, inFar);
	    if (distSq > 0) {
		if (pickedNode.mNode == null || distSq <= pickedNode.mDistance) {
		    pickedNode.mNode = this;
		    pickedNode.mDistance = distSq;
		}
	    }
	}

	for (int i = 0; i < mChildList.size(); i++) {
	    mChildList.get(i).findPickedNode(pickedNode, matVP, inNear, inFar);
	}

	return pickedNode;
    }

    // return : distance^2.
    // distance^2 < 0 : not picked.
    protected float isPicked(float[] matVP, float[] inNear, float[] inFar) {
	return -1.0f;
    }

    public boolean isPickable() {
	return mPickable;
    }

    public void setPickable(boolean pickable) {
	mPickable = pickable;
    }
}
