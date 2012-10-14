package com.lplus.graphics;


public class LplusGLModel extends LplusGLNode {

    private LplusGLMesh mMesh;
    
    public LplusGLModel() {
	super();
    }
    
    public void setMesh(LplusGLMesh mesh) {
	mMesh = mesh;
    }
    
    public LplusGLMesh getMesh() {
	return mMesh;
    }
}
