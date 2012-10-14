package com.lplus.graphics;

import java.util.ArrayList;

public class LplusGLScene {

    ArrayList<LplusGLModel> mModelList;
    
    public LplusGLScene() {
	// TODO Auto-generated constructor stub
	mModelList = new ArrayList<LplusGLModel>();
    }

    public void addModel(LplusGLModel model) {
	if (mModelList != null) {
	    mModelList.add(model);
	}
    }
    
    public boolean removeModel(LplusGLModel model) {
	if (mModelList != null) {
	    return mModelList.remove(model);
	}
	
	return false;
    }
    
    public ArrayList<LplusGLModel> getModelList() {
	return mModelList;
    }
}
