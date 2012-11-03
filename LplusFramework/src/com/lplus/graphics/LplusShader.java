package com.lplus.graphics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class LplusShader {
    private int mProgram;
    private int mVertexShader;
    private int mFragemenShader;

    private int mColorHandle;
    private int mPositionHandle;
    private int mTextureHandle;
    private int mMVPMatrixHandle;

    public LplusShader(String vShaderCode, String fShaderCode) {
	loadShader(vShaderCode, fShaderCode);
    }

    public void loadShader(String vShaderCode, String fShaderCode) {
	mProgram = GLES20.glCreateProgram();
	mVertexShader = LplusUtil.loadShader(GLES20.GL_VERTEX_SHADER, vShaderCode);
	mFragemenShader = LplusUtil.loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderCode);

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
	mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
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

    public void updateMesh(LplusMesh mesh) {
	FloatBuffer buffer = mesh.getVertexBuffer();
	buffer.position(0);

	GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, mesh.getVertexStride() * 4, buffer);

	buffer.position(3);
	GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false, mesh.getVertexStride() * 4, buffer);
    }

    public void updateMaterial(LplusMaterial material) {
	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	if (LplusContextGLES.USING_TEXTURE_ATLAS) {
	    if (material != null && material.getAtlasTexture() != null)
		material.getAtlasTexture().bind();
	} else {
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, material.getTexture());
	}

	// headnum : Handling color
	float r = (float) Color.red(material.getColor()) / 255f;
	float g = (float) Color.green(material.getColor()) / 255f;
	float b = (float) Color.blue(material.getColor()) / 255f;
	float a = (float) Color.alpha(material.getColor()) / 255f;

	GLES20.glUniform4f(mColorHandle, r, g, b, a);
    }

    public void updateMatrix(float[] mMatrix, float[] vpMatrix) {
	float[] mvpMatrix = new float[16];
	Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mMatrix, 0);

	updateMatrix(mvpMatrix);
    }

    public void updateMatrix(float[] mvpMatrix) {
	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
    }
}
