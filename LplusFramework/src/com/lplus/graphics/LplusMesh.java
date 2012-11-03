package com.lplus.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class LplusMesh {

    int mVertexCount;
    int mVertexStride;
    int mIndexCount;

    private float[] mOriginalVertexArray;
    private FloatBuffer mVertexBuffer;
    private ShortBuffer mIndexBuffer;

    public LplusMesh() {
	mVertexBuffer = null;
	mIndexBuffer = null;

	// P3T2 Type
	mVertexStride = 5;
    }

    public void setVertexArray(float[] array, int vertexCount) {
	// TODO : implement vertex type
	if (array == null || vertexCount == 0)
	    return;

	int arraySize = mVertexStride * vertexCount;

	if (array.length < arraySize)
	    return;
	
	mVertexCount = vertexCount;
	ByteBuffer bb = ByteBuffer.allocateDirect(arraySize * 4);
	bb.order(ByteOrder.nativeOrder());	
	mVertexBuffer = bb.asFloatBuffer();
	mVertexBuffer.put(array, 0, arraySize);
	mVertexBuffer.position(0);
	
	mOriginalVertexArray = new float[array.length];
	System.arraycopy(array, 0, mOriginalVertexArray, 0, array.length);
    }

    public void setIndexArray(short[] array, int indexCount) {

	if (array == null || indexCount == 0)
	    return;
	
	if (array.length < indexCount)
	    return;

	mIndexCount = indexCount;

	ByteBuffer bb = ByteBuffer.allocateDirect(indexCount * 2);
	bb.order(ByteOrder.nativeOrder());
	mIndexBuffer = bb.asShortBuffer();
	mIndexBuffer.put(array, 0, indexCount);
	mIndexBuffer.position(0);
    }

    public FloatBuffer getVertexBuffer() {
	return mVertexBuffer.duplicate();
    }

    public ShortBuffer getIndexBuffer() {
	return mIndexBuffer.duplicate();
    }
    
    public void updateVertexBuffer(float[] data, final int initoffset, final int size, final int stride){
	if(data == null || mVertexBuffer == null) 
	    return;
	
	if((data.length / size) * stride + initoffset >= mVertexBuffer.capacity())
	    return;
	
	int position = initoffset;
	int sizecount = 0;
	for(int i = 0; i < data.length; i++){
	    mVertexBuffer.put(position, data[i]);
	    position += ((++sizecount) % size == 0) ? stride : 1;  
	}	
    }
    
    public void scaleTexCoords(float offsetX, float offsetY, float ratioX, float ratioY){
	if(mVertexBuffer == null)
	    return;
	
	for(int i = 0; i < mVertexCount; i++){
	    	     
	    int px = i * mVertexStride + 3;
	    int py = i * mVertexStride + 4;
	    //need to change 3,4 w/ variables in future.
	    
	    float tX = mOriginalVertexArray[px]; 
	    float tY = mOriginalVertexArray[py];
	    float ntX = tX * ratioX + offsetX;
	    float ntY = tY * ratioY + offsetY;
	    mVertexBuffer.put(px, ntX);
	    mVertexBuffer.put(py, ntY);
	    
	}
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

    public float[] getIntersectionPoint(float[] origin, float[] ray) {
	return null;
    }
}
