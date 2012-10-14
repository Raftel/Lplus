package com.lplus.graphics;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class LplusGLContext extends GLSurfaceView implements Renderer {
    
    private LplusGLShader mShader;
    private LplusGLScene mScene;
    private LplusGLMesh mPrevMesh;
    private Callback mCallback;
    
    private final float[] mProjMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    
    public interface Callback {
	public void onSurfaceCreated();
    }

    public LplusGLContext(Context context) {
	super(context);

	setEGLContextClientVersion(2);
	setRenderer(this);
	
	// Check
	//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private void initialize() {
	
	int mWidth = getWidth();
	int mHeight = getHeight();
		
	GLES20.glEnable(GLES20.GL_BLEND);
	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	
	GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	GLES20.glDepthFunc(GLES20.GL_LEQUAL);
	
	GLES20.glViewport(0, 0, mWidth, mHeight);
	
	Matrix.setIdentityM(mProjMatrix, 0);
	Matrix.setIdentityM(mViewMatrix, 0);
    }
    
    public void setScene(LplusGLScene scene) {
	mScene = scene;
    }
    
    public LplusGLScene getScene() {
	return mScene;
    }
    
    public void setCallback(LplusGLContext.Callback callback) {
	mCallback = callback;
    }
    
    public LplusGLContext.Callback getCallback() {
	return mCallback;
    }
    
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
	// TODO Auto-generated method stub
	
	// Set the background frame color
	GLES20.glClearColor(0.0f, 0.5f, 0.0f, 1.0f);
	
	initialize();
	
	// check only one shader
	mShader = new LplusGLShader(
		LplusGLShaderStr.strVertexShaderDefault, 
		LplusGLShaderStr.strFragmentShaderDefault);
	
	if (mCallback != null) 
	    mCallback.onSurfaceCreated();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
	// Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
        
        Matrix.setIdentityM(mProjMatrix, 0);
//      Matrix.orthoM(mProjMatrix, 0, -width*0.5f, width*0.5f, -height*0.5f, height*0.5f, height*0.5f, height*10.0f);
        
	Matrix.frustumM(
		mProjMatrix, 0, 
		-width * 0.25f, width * 0.25f, 
		-height * 0.25f, height * 0.25f, 
		height * 0.5f, height * 10.0f);
        
        // mirror
        mProjMatrix[5] = -mProjMatrix[5];
        
        Matrix.translateM(mProjMatrix, 0, mProjMatrix, 0, -width*0.5f, -height*0.5f, -height);
    }

    public void onDrawFrame(GL10 gl) {
	GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
	
	mShader.useProgram();
	
	ArrayList<LplusGLModel> modelList = mScene.getModelList();
	for (int i = 0; i < mScene.getModelList().size(); i++) {
	    LplusGLModel model = modelList.get(i);
	    renderModel(model);
	}
	
	mShader.unuseProgram();
     } 
    
    private void renderModel(LplusGLModel model) {
	if (model == null)
	    return;
	
	mShader.updateMatrix(model.getModelMatrix(), mViewMatrix, mProjMatrix);
	
	LplusGLMesh mesh = model.getMesh();
	
	if (mPrevMesh != mesh) {
	    mShader.updateMesh(mesh);
	    mShader.updateMaterial(mesh);
	    mPrevMesh = mesh;
	}

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getIndexCount(),
                              GLES20.GL_UNSIGNED_SHORT, mesh.getIndexBuffer());
    }
}
