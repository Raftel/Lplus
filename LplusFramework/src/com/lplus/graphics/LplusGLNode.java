package com.lplus.graphics;

import java.util.ArrayList;

import android.opengl.Matrix;

public class LplusGLNode {
    
    private final float[] mTVector = new float[3];
    private final float[] mRVector = new float[3];
    private final float[] mSVector = new float[3];
    
    private boolean mModelMatrixDirty;
    private float[] mModelMatrix = new float[16];
    
    private LplusGLNode mParent;
    private ArrayList<LplusGLNode> mChildList;
    
    public LplusGLNode() {
	// TODO Auto-generated constructor stub
	mParent = null;
	mChildList = new ArrayList<LplusGLNode>();
	Matrix.setIdentityM(mModelMatrix, 0);
	
	mTVector[0] = 0.0f;
	mTVector[1] = 0.0f;
	mTVector[2] = 0.0f;
	
	mRVector[0] = 0.0f;
	mRVector[1] = 0.0f;
	mRVector[2] = 0.0f;
	
	mSVector[0] = 1.0f;
	mSVector[1] = 1.0f;
	mSVector[2] = 1.0f;
    }
    
    public LplusGLNode getParent() {
	return mParent;
    }
    
    public void setParent(LplusGLNode parent) {
	mParent = parent;
    }
    
    public boolean addChild(LplusGLNode node) {
	if (node == null)
	    return false;
	
	LplusGLNode prevParent = node.getParent();
	if (prevParent == this)
	    return true;
	
	if (prevParent != null) {
	    prevParent.removeChild(node);
	}
	
	mChildList.add(node);
	node.setParent(this);
	node.setMatrixDirty();
	
	return true;
    }
    
    public boolean removeChild(LplusGLNode node) {
	if (node == null)
	    return false;
	
	if (node.getParent() != this)
	    return false;
	
	mChildList.remove(node);
	node.setParent(null);
	node.setMatrixDirty();
	
	return true;
    }
    
    public void removeAllChild() {
	mChildList.clear();
    }
    
    public void setTranslation(float x, float y, float z) {
	mTVector[0] = x;
	mTVector[1] = y;
	mTVector[2] = z;
	
	setMatrixDirty();
    }
    
    public float[] getTranslation() {
	return mTVector;
    }
    
    public void setRotation(float x, float y, float z) {
	mRVector[0] = x;
	mRVector[1] = y;
	mRVector[2] = z;
	
	setMatrixDirty();
    }
    
    public float[] getRotation() {
	return mRVector;
    }
    
    public void setScale(float x, float y, float z) {
	mSVector[0] = x;
	mSVector[1] = y;
	mSVector[2] = z;
	
	setMatrixDirty();
    }
    
    public float[] getScale() {
	return 	mSVector;
    }
    
    public void setMatrixDirty()
    {
    	for (int i = 0; i < mChildList.size(); i++)
    	{
    		LplusGLNode node = mChildList.get(i);
    		if (node != null)
    		    node.setMatrixDirty();
    	}
    	
    	mModelMatrixDirty = true;
    }
    
    public float[] getModelMatrix() {
	if (mModelMatrixDirty) {
	    if (mParent != null)
		mModelMatrix = mParent.getModelMatrix();
	    else
		Matrix.setIdentityM(mModelMatrix, 0);
	    
	    Matrix.translateM(mModelMatrix, 0, mModelMatrix, 0, mTVector[0], mTVector[1], mTVector[2]);
	    
	    if( mRVector[0] != 0.0f)
		Matrix.rotateM(mModelMatrix, 0, mModelMatrix, 0, mRVector[0], 1, 0, 0);
	    if( mRVector[1] != 0.0f)
		Matrix.rotateM(mModelMatrix, 0, mModelMatrix, 0, mRVector[1], 0, 1, 0);
	    if( mRVector[2] != 0.0f)
		Matrix.rotateM(mModelMatrix, 0, mModelMatrix, 0, mRVector[2], 0, 0, 1);
	    
	    if( mSVector[0] != 1.0f && mSVector[1] != 1.0f && mSVector[2] != 1.0f )
		Matrix.scaleM(mModelMatrix, 0, mModelMatrix, 0, mSVector[0], mSVector[1], mSVector[2]);
	    
	    mModelMatrixDirty = false;
	}
	
	return mModelMatrix;
    }
}
