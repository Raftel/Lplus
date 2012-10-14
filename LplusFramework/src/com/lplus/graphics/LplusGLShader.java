package com.lplus.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.opengl.GLES20;
import android.opengl.Matrix;

public class LplusGLShader {
    private int mProgram;
    private int mVertexShader;
    private int mFragemenShader;

    private int mPositionHandle;
    private int mTextureHandle;
    private int mMVPMatrixHandle;

    public LplusGLShader(String vShaderCode, String fShaderCode) {
	loadShader(vShaderCode, fShaderCode);
    }

    public void loadShader(String vShaderCode, String fShaderCode) {
	mProgram = GLES20.glCreateProgram();
	mVertexShader = LplusGLUtil.loadShader(GLES20.GL_VERTEX_SHADER, vShaderCode);
	mFragemenShader = LplusGLUtil.loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderCode);

	GLES20.glAttachShader(mProgram, mVertexShader);
	GLES20.glAttachShader(mProgram, mFragemenShader);
	GLES20.glLinkProgram(mProgram);

	IntBuffer linkStatus = IntBuffer.allocate(1);
	GLES20.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, linkStatus);

	if (linkStatus.get() == GLES20.GL_FALSE) {
	    // TODO : Insert ERROR LOG
	    GLES20.glDeleteProgram(mProgram);
	    return;
	}

	// attributes
	mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
	mTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");

	// uniforms
	mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void useProgram() {
	GLES20.glUseProgram(mProgram);

	GLES20.glEnableVertexAttribArray(mPositionHandle);
	GLES20.glEnableVertexAttribArray(mTextureHandle);
    }

    public void unuseProgram() {
	GLES20.glDisableVertexAttribArray(mPositionHandle);
	GLES20.glDisableVertexAttribArray(mTextureHandle);
    }

    public void updateMesh(LplusGLMesh mesh) {
	FloatBuffer buffer = mesh.getVertexBuffer();
	buffer.position(0);

	GLES20.glVertexAttribPointer(
		mPositionHandle, 
		3, 
		GLES20.GL_FLOAT, 
		false,
		mesh.getVertexStride() * 4, 
		buffer);
    }

    public void updateMaterial(LplusGLMesh mesh) {
	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mesh.getTexture());

	FloatBuffer buffer = mesh.getVertexBuffer();
	buffer.position(3);
	
	GLES20.glVertexAttribPointer(
		mTextureHandle, 
		2, 
		GLES20.GL_FLOAT, 
		false, 
		mesh.getVertexStride() * 4,
		buffer); 
    }

    public void updateMatrix(float[] mMatrix, float[] vMatrix, float[] pMatrix) {
	float[] mvpMatrix = new float[16];
	Matrix.multiplyMM(mvpMatrix, 0, vMatrix, 0, mMatrix, 0);
	Matrix.multiplyMM(mvpMatrix, 0, pMatrix, 0, mvpMatrix, 0);

	updateMatrix(mvpMatrix);
    }

    public void updateMatrix(float[] mvpMatrix) {
	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
    }
}
