package com.lplus.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.graphics.Bitmap;

public class LplusGLMesh {

    float[] mVertexArray;
    int mVertexCount;
    int mVertexStride;
    short[] mIndexArray;
    int mIndexCount;
    
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mIndexBuffer;
    
    private int mTexture;
    
    public LplusGLMesh() {
	mVertexBuffer = null;
	mIndexBuffer = null;
    }
    
    public void setVertexArray(float[] array, int vertexCount) {
	// TODO : implement vertex type
	if (array == null || vertexCount == 0)
	    return;
	
	// P3T2 Type
	mVertexStride = 5;
	
	if (mVertexArray == null || mVertexCount != vertexCount) {
	    if (mVertexArray != null)
		mVertexArray = null;
	    
	    mVertexArray = new float[vertexCount * mVertexStride];
	    mVertexCount = vertexCount;
	}
	
	System.arraycopy(array, 0, mVertexArray, 0, mVertexCount * mVertexStride);
	
//	CBGLUtil.createBuffer(mVertexBuffer, mVertexArray);
	
	ByteBuffer bb = ByteBuffer.allocateDirect(mVertexArray.length * 4);
        bb.order(ByteOrder.nativeOrder());
        mVertexBuffer = bb.asFloatBuffer();
        mVertexBuffer.put(mVertexArray);
        mVertexBuffer.position(0);
    }
    
    public void setIndexArray(short[] array, int indexCount) {
	if (array == null || indexCount == 0) 
	    return;
	
	if (mIndexArray == null || mIndexCount != indexCount) {
	    if (mIndexArray != null)
		mIndexArray = null;
	    
	    mIndexArray = new short[indexCount];
	    mIndexCount = indexCount;
	}
	
	System.arraycopy(array, 0, mIndexArray, 0, mIndexCount);
	
	//CBGLUtil.createBuffer(mIndexBuffer, mIndexArray);
	ByteBuffer bb = ByteBuffer.allocateDirect(mIndexArray.length * 2);
        bb.order(ByteOrder.nativeOrder());
        mIndexBuffer = bb.asShortBuffer();
        mIndexBuffer.put(mIndexArray);
        mIndexBuffer.position(0);
    }
    
    public void setTexture(Bitmap bitmap) {
	mTexture = LplusGLUtil.loadTexture(bitmap);
    }
    
    public FloatBuffer getVertexBuffer() {
	return mVertexBuffer.duplicate();
    }
    
    public ShortBuffer getIndexBuffer() {
	return mIndexBuffer.duplicate();
    }
    
    public int getVertexCount() {
	return mVertexCount;
    }
    
    public int getIndexCount() {
	return mIndexCount;
    }
    
    public int getVertexStride() {
	return mVertexStride;
    }
    
    public int getTexture() {
	return mTexture;
    }
}
