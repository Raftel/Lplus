package com.lplus.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class LplusShader {
	private int mProgram;
	private int mVertexShader;
	private int mFragemenShader;

	// Vertex attribute
	private int mColorHandle;
	private int mPositionHandle;
	private int mNormalHandle;
	private int mTextureHandle;
	
	// Camera
	private int mCameraHandle;
	
	// Matrix
	private int mMVPMatrixHandle;
	private int mNormalMatrixHandle;
	private int mModelMatrixHandle;
	
	// Light
	private int mLightCountHandle;
	private int mLightPositionHandle;
	private int mLightRangeHandle;
	private int mLightIntensityHandle;	
	
	// Material
	private int mMaterialAmbientHandle;
	private int mMaterialDiffuseHandle;
	private int mMaterialSpecularHandle;

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
			GLES20.glDeleteProgram(mProgram);
			LplusUtil.checkError("LplusShader", "loadShader", "linkStatus is not true");
			return;
		}

		// attributes
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
		mTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTextureCoord");
		mNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		
		// uniforms
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor");
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		mNormalMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uNormalMatrix");
		mModelMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uModelMatrix");
		
		mCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCameraPosition");
		
		mLightCountHandle = GLES20.glGetUniformLocation(mProgram, "uLightCount");
		mLightPositionHandle = GLES20.glGetUniformLocation(mProgram, "uLightPosition");
		mLightRangeHandle = GLES20.glGetUniformLocation(mProgram, "uLightRange");
		mLightIntensityHandle = GLES20.glGetUniformLocation(mProgram, "uLightIntensity");
		
		mMaterialAmbientHandle = GLES20.glGetUniformLocation(mProgram, "uMaterialAmbient");
		mMaterialDiffuseHandle = GLES20.glGetUniformLocation(mProgram, "uMaterialDiffuse");
		mMaterialSpecularHandle = GLES20.glGetUniformLocation(mProgram, "uMaterialSpecular");
		
		LplusUtil.checkError("LplusShader", "loadShader", "");
	}

	public void useProgram() {
		
		GLES20.glUseProgram(mProgram);

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mNormalHandle);
		GLES20.glEnableVertexAttribArray(mTextureHandle);	
		
		LplusUtil.checkError("LplusShader", "useProgram", "");
	}

	public void unuseProgram() {
		GLES20.glDisableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormalHandle);
		GLES20.glDisableVertexAttribArray(mTextureHandle);		
		
		LplusUtil.checkError("LplusShader", "unuseProgram", "");
	}

	public void updateMesh(LplusMesh mesh) {
		FloatBuffer buffer = mesh.getVertexBuffer();

		buffer.position(0);
		GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, mesh.getVertexStride() * 4, buffer);

		buffer.position(3);
		GLES20.glVertexAttribPointer(mNormalHandle, 3, GLES20.GL_FLOAT, false, mesh.getVertexStride() * 4, buffer);
		
		buffer.position(6);
		GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false, mesh.getVertexStride() * 4, buffer);
		
		LplusUtil.checkError("LplusShader", "updateMesh", "");
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
		
		float[] diffuse = material.getDiffuse();
		float[] ambient = material.getAmbient();
		float[] specular = material.getSpecular();
		
		GLES20.glUniform3f(mMaterialAmbientHandle, ambient[0], ambient[1], ambient[2]);
		GLES20.glUniform3f(mMaterialDiffuseHandle, diffuse[0], diffuse[1], diffuse[2]);
		GLES20.glUniform3f(mMaterialSpecularHandle, specular[0], specular[1], specular[2]);
		
		LplusUtil.checkError("LplusShader", "updateMaterial", "");
	}
	
	public void updateLight(ArrayList<LplusLight> lightList) {
		
		FloatBuffer posBuffer;
		FloatBuffer rangeBuffer;
		FloatBuffer intensityBuffer;
		
		ByteBuffer posBb = ByteBuffer.allocateDirect(lightList.size() * 3 * 4);
		posBb.order(ByteOrder.nativeOrder());
		posBuffer = posBb.asFloatBuffer();
		
		ByteBuffer rangeBb = ByteBuffer.allocateDirect(lightList.size() * 4);
		rangeBb.order(ByteOrder.nativeOrder());
		rangeBuffer = rangeBb.asFloatBuffer();
		
		ByteBuffer intensityBb = ByteBuffer.allocateDirect(lightList.size() * 4);
		intensityBb.order(ByteOrder.nativeOrder());
		intensityBuffer = intensityBb.asFloatBuffer();
		
		for (int i = 0; i < lightList.size(); i++) {
			posBuffer.put(lightList.get(i).getPosition());
			rangeBuffer.put(lightList.get(i).getRange());
			intensityBuffer.put(lightList.get(i).getIntensity());
		}
		
		posBuffer.position(0);
		rangeBuffer.position(0);
		intensityBuffer.position(0);
		
		GLES20.glUniform1i(mLightCountHandle, lightList.size());
		GLES20.glUniform3fv(mLightPositionHandle, lightList.size(), posBuffer);
		GLES20.glUniform1fv(mLightRangeHandle, lightList.size(), rangeBuffer);
		GLES20.glUniform1fv(mLightIntensityHandle, lightList.size(), intensityBuffer);
		
		LplusUtil.checkError("LplusShader", "updateLight", "");
	}
	
	public void updateCamera(float x, float y, float z) {
		GLES20.glUniform3f(mCameraHandle, x, y, z); 
		
		LplusUtil.checkError("LplusShader", "updateCamera", "");
	}

	public void updateMatrix(float[] mMatrix, float[] vMatrix, float[] pMatrix) {
		float[] mvMatrix = new float[16];
		float[] mvpMatrix = new float[16];
		
		Matrix.multiplyMM(mvMatrix, 0, vMatrix, 0, mMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, pMatrix, 0, mvMatrix, 0);

		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		
		GLES20.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mMatrix, 0);
		
		GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, LplusUtil.calcNormalMatrix(mMatrix), 0);
		
		LplusUtil.checkError("LplusShader", "updateMatrix", "");
	}
	
	public void updateMatrix(float[] mMatrix, float[] vpMatrix) {
		float[] mvpMatrix = new float[16];
		Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, mMatrix, 0);

		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
		
		GLES20.glUniformMatrix4fv(mModelMatrixHandle, 1, false, mMatrix, 0);
		
		GLES20.glUniformMatrix4fv(mNormalMatrixHandle, 1, false, LplusUtil.calcNormalMatrix(mMatrix), 0);
		
		LplusUtil.checkError("LplusShader", "updateMatrix", "");
	}
}
