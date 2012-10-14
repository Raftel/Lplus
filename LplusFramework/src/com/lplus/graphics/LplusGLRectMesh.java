package com.lplus.graphics;

import android.graphics.RectF;

public class LplusGLRectMesh extends LplusGLMesh {

    RectF mRect;
    
    public LplusGLRectMesh(float width, float height) {
	super();
	
	float left = -width * 0.5f;
	float right = width * 0.5f;
	float top = -height * 0.5f;
	float bottom = height * 0.5f;	
	
	// Vertex Type : P3T2
	float vertexArray[] = { 
		left,     top, 0.0f, 0.0f, 0.0f,
		left,  bottom, 0.0f, 0.0f, 1.0f,
		right, bottom, 0.0f, 1.0f, 1.0f,
		right, 	  top, 0.0f, 1.0f, 0.0f
	};

	short indexArray[] = {0, 1, 2, 2, 3, 0 }; 

	mRect = new RectF(left, top, right, bottom);
	setVertexArray(vertexArray, 4);
	setIndexArray(indexArray, 6);
    }
}
