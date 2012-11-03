package com.lplus.graphics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.lplus.animation.LplusAnimationManager;
import com.lplus.graphics.LplusAtlasMgr.SCGAtlasTexture;

public class LplusContextGLES implements Renderer {

    //headnum : experimental 
    public static final boolean USING_TEXTURE_ATLAS = false;
    

    private LplusSurfaceGLES mSurface;
    private LplusAnimationManager mAnimManager;
    private LplusScene mScene;
    private LplusShader mShader;
    private LplusMesh mPrevMesh;
    private LplusMaterial mPrevMaterial;
    private LplusAtlasMgr mAtlasManager;

    private static final int NUM_MAX_RUNNABLES_ON_SINGLE_LOOP = 3;
    private Queue<Runnable> mRenderThreadRunnables;
    private Callback mCallback;
    private Thread mRenderThread;

    private final float[] mVPMatrix = new float[16];
    private final float[] mIdentityMatrix = new float[16];

    public interface Callback {
	public void onSurfaceCreated();

	public void onDrawFrame();
    }

    // Constructor
    public LplusContextGLES(Context context) {

	mRenderThreadRunnables = new LinkedList<Runnable>();

	LplusScene scene = new LplusScene();
	LplusSurfaceGLES surface = new LplusSurfaceGLES(context);

	setScene(scene);
	setSurface(surface);

	runOnRenderThread(new Runnable() {
	    public void run() {
		mAtlasManager = new LplusAtlasMgr(LplusContextGLES.this);
	    }
	});
    }

    public LplusContextGLES(LplusScene scene, LplusSurfaceGLES surface) {

	mRenderThreadRunnables = new LinkedList<Runnable>();

	setScene(scene);
	setSurface(surface);

	runOnRenderThread(new Runnable() {
	    public void run() {
		mAtlasManager = new LplusAtlasMgr(LplusContextGLES.this);
	    }
	});

    }

    // PUBLIC METHODS
    public void setScene(LplusScene scene) {
	mScene = scene;
    }

    public LplusScene getScene() {
	return mScene;
    }

    public LplusAtlasMgr getAtlasManager() {
	return mAtlasManager;
    }

    public void setSurface(LplusSurfaceGLES surface) {
	mSurface = surface;
	if (mSurface != null)
	    mSurface.setRenderer(this);
    }

    public void setAnimManager(LplusAnimationManager animManager) {
	mAnimManager = animManager;
    }

    public LplusSurfaceGLES getSurface() {
	return mSurface;
    }

    public void setCallback(Callback cb) {
	mCallback = cb;
    }

    public void runOnRenderThread(Runnable r) {
	mRenderThreadRunnables.add(r);
    }

    // RENDERER METHODS
    public void onDrawFrame(GL10 arg0) {

	// CHECK :
	if (mAnimManager != null) {
	    mAnimManager.doAnimations();
	}

	for (int i = 0; i < mRenderThreadRunnables.size() && i < NUM_MAX_RUNNABLES_ON_SINGLE_LOOP; i++) {
	    Log.d("hello", "renderthread");
	    mRenderThreadRunnables.poll().run();
	}

	if (!USING_TEXTURE_ATLAS) {
	    LplusMaterial.doReservedTexLoading();
	}

	GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

	mShader.useProgram();

	if (GLES20.glGetError() != GLES20.GL_NO_ERROR)
	    Log.e("SCGContext", "gl error - " + GLUtils.getEGLErrorString(GLES20.glGetError()));

	ArrayList<LplusNode> renderNodeList = mScene.getRenderNodeList();
	for (int i = 0; i < renderNodeList.size(); i++) {
	    renderModel(renderNodeList.get(i));
	}

	if (GLES20.glGetError() != GLES20.GL_NO_ERROR)
	    Log.e("SCGContext", "gl error - " + GLUtils.getEGLErrorString(GLES20.glGetError()));

	mShader.unuseProgram();

	// CHECK :
	if (mCallback != null)
	    mCallback.onDrawFrame();

    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
	resize(width, height);
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

	initialize();

	// check only one shader
	mShader = new LplusShader(LplusShaderStr.strVertexShaderDefault, LplusShaderStr.strFragmentShaderDefault);

	if (mCallback != null)
	    mCallback.onSurfaceCreated();

	mRenderThread = Thread.currentThread();

    }

    public boolean isRunningOnRenderThread(Thread t) {
	if (t == null || mRenderThread == null)
	    return false;
	return t.getId() == mRenderThread.getId();
    }

    // PRIVATE METHODS
    private void initialize() {
	GLES20.glEnable(GLES20.GL_BLEND);
	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

	GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	GLES20.glDepthFunc(GLES20.GL_LEQUAL);

	Matrix.setIdentityM(mVPMatrix, 0);
	Matrix.setIdentityM(mIdentityMatrix, 0);
    }

    private void resize(int width, int height) {
	// Adjust the viewport based on geometry changes,
	// such as screen rotation
	GLES20.glViewport(0, 0, width, height);

	Matrix.setIdentityM(mVPMatrix, 0);
	// Matrix.orthoM(mProjMatrix, 0, -width*0.5f, width*0.5f, -height*0.5f,
	// height*0.5f, height*0.5f, height*10.0f);

	Matrix.frustumM(mVPMatrix, 0, -width * 0.25f, width * 0.25f, -height * 0.25f, height * 0.25f, height * 0.5f, height * 10.0f);

	// mirror
	mVPMatrix[5] = -mVPMatrix[5];

	Matrix.translateM(mVPMatrix, 0, mVPMatrix, 0, -width * 0.5f, -height * 0.5f, -height);
    }

    private void renderModel(LplusNode node) {
	if (node == null || node.isVisible() == false)
	    return;

	synchronized (node) {

	    if (node instanceof LplusModel) {
		LplusModel model = (LplusModel) node;
		LplusMesh mesh = model.getMesh();
		final LplusMaterial material = model.getMaterial();

		if (mesh != null) {
		    mShader.updateMatrix(model.getModelMatrix(), mVPMatrix);
		    if (mPrevMesh != mesh) {
			mShader.updateMesh(mesh);
			mPrevMesh = mesh;
		    }
		    if (USING_TEXTURE_ATLAS) {
			if (material != null && material.getPendingTexture() != null && !material.getPendingTexture().isRecycled()) {
			    SCGAtlasTexture tex = mAtlasManager.createTexture(material.getPendingTexture(), true);
			    material.setTexture(tex);
			} else if (material != null && material.getAtlasTexture() != null && !material.getAtlasTexture().isRecycled()) {
			    if (mPrevMaterial != material) {
				mShader.updateMaterial(material);
				mPrevMaterial = material;
			    }
			    GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getIndexCount(), GLES20.GL_UNSIGNED_SHORT, mesh.getIndexBuffer());
			}
		    } else {

			if (material != null) {
			    if (mPrevMaterial != material) {
				mShader.updateMaterial(material);
				mPrevMaterial = material;
			    }
			    GLES20.glDrawElements(GLES20.GL_TRIANGLES, mesh.getIndexCount(), GLES20.GL_UNSIGNED_SHORT, mesh.getIndexBuffer());
			}

		    }
		}
	    }

	    // render child
	    ArrayList<LplusNode> childList = node.getChildList();
	    for (int i = 0; i < childList.size(); i++) {
		renderModel(childList.get(i));
	    }
	}
    }

    public LplusNode pick(float x, float y) {

	float[] inNear = new float[4];
	float[] inFar = new float[4];

	inNear[0] = (x / (float) getSurface().getWidth()) * 2 - 1;
	inNear[1] = 1 - (y / (float) getSurface().getHeight()) * 2;
	inNear[2] = -1;
	inNear[3] = 1;

	inFar[0] = (x / (float) getSurface().getWidth()) * 2 - 1;
	inFar[1] = 1 - (y / (float) getSurface().getHeight()) * 2;
	inFar[2] = 1;
	inFar[3] = 1;

	LplusNode.PickedNode pickedNode = new LplusNode.PickedNode();

	ArrayList<LplusNode> renderNodeList = mScene.getRenderNodeList();
	for (int i = 0; i < renderNodeList.size(); i++) {
	    renderNodeList.get(i).findPickedNode(pickedNode, mVPMatrix, inNear, inFar);
	}

	return pickedNode.mNode;
    }

    public LplusNode pick(LplusNode renderNode, float x, float y) {

	float[] inNear = new float[4];
	float[] inFar = new float[4];

	inNear[0] = (x / (float) getSurface().getWidth()) * 2 - 1;
	inNear[1] = 1 - (y / (float) getSurface().getHeight()) * 2;
	inNear[2] = -1;
	inNear[3] = 1;

	inFar[0] = (x / (float) getSurface().getWidth()) * 2 - 1;
	inFar[1] = 1 - (y / (float) getSurface().getHeight()) * 2;
	inFar[2] = 1;
	inFar[3] = 1;

	LplusNode.PickedNode pickedNode = new LplusNode.PickedNode();

	ArrayList<LplusNode> renderNodeList = mScene.getRenderNodeList();
	for (int i = 0; i < renderNodeList.size(); i++) {
	    if (renderNodeList.get(i) == renderNode) {
		renderNodeList.get(i).findPickedNode(pickedNode, mVPMatrix, inNear, inFar);
	    }
	}

	return pickedNode.mNode;
    }

}
