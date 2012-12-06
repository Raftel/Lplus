package com.lplus.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class LplusUtil {

	public static void createBuffer(FloatBuffer buffer, float[] src) {
		ByteBuffer bb = ByteBuffer.allocateDirect(src.length * 4);
		bb.order(ByteOrder.nativeOrder());
		buffer = bb.asFloatBuffer();
		buffer.put(src);
		buffer.position(0);
	}

	public static void createBuffer(ShortBuffer buffer, short[] src) {
		ByteBuffer bb = ByteBuffer.allocateDirect(src.length * 2);
		bb.order(ByteOrder.nativeOrder());
		buffer = bb.asShortBuffer();
		buffer.put(src);
		buffer.position(0);
	}

	public static int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}
	
	public static void checkError(String className, String funcName, String msg) {
		int error = GLES20.glGetError();
		if (error != GLES20.GL_NO_ERROR)
			Log.e("Lplus", className + ":" + funcName + "(" + msg + ")" + "gl error - " + GLUtils.getEGLErrorString(error));
	}
	
	public static float[] calcNormalMatrix(float[] matrix) {
		float rdet; 
		float[] matNormal = new float[16];
		float[] temp = new float[3];

		temp[0] = matrix[5] * matrix[10] - matrix[6] * matrix[9];
		temp[1] = matrix[6] * matrix[8]  - matrix[4] * matrix[10];
		temp[2] = matrix[4] * matrix[9]  - matrix[5] * matrix[8];

		rdet = matrix[0] * temp[0] + matrix[1] * temp[1] + matrix[2] * temp[2];
		rdet = 1.0f / rdet;

		matNormal[0]  = temp[0] * rdet;
		matNormal[1]  = temp[1] * rdet;
		matNormal[2]  = temp[2] * rdet;

		matNormal[4]  = (matrix[2] * matrix[9]  - matrix[1] * matrix[10])* rdet;
		matNormal[5]  = (matrix[0] * matrix[10] - matrix[2] * matrix[8]) * rdet;
		matNormal[6]  = (matrix[1] * matrix[8]  - matrix[0] * matrix[9]) * rdet;

		matNormal[8]  = (matrix[1] * matrix[6] - matrix[2] * matrix[5]) * rdet;
		matNormal[9]  = (matrix[2] * matrix[4] - matrix[0] * matrix[6]) * rdet;
		matNormal[10] = (matrix[0] * matrix[5] - matrix[1] * matrix[4]) * rdet;
		
		return matNormal;
	}

	public static int loadTexture(Bitmap bitmap) {
		int[] textureId = new int[1];

		if (GLES20.glGetError() != GLES20.GL_NO_ERROR)
			Log.e("SCGUtil", "loadTexture error - " + GLUtils.getEGLErrorString(GLES20.glGetError()));

		GLES20.glGenTextures(1, textureId, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		// bitmap.recycle();

		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		if (GLES20.glGetError() != GLES20.GL_NO_ERROR)
			Log.e("SCGUtil", "loadTexture error - " + GLUtils.getEGLErrorString(GLES20.glGetError()));

		return textureId[0];
	}

	public static void loadTexture(int textureId, Bitmap bitmap) {
		int[] textureArray = new int[1];
		textureArray[0] = textureId;

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureArray[0]);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		// bitmap.recycle();
	}

	public static void unloadTexture(int textureId) {
		if (GLES20.glGetError() != GLES20.GL_NO_ERROR)
			Log.e("SCGUtil", "unloadTexture error - " + GLUtils.getEGLErrorString(GLES20.glGetError()));

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		int idArr[] = new int[textureId];
		GLES20.glDeleteTextures(1, idArr, 0);

		if (GLES20.glGetError() != GLES20.GL_NO_ERROR)
			Log.e("SCGUtil", "unloadTexture error - " + GLUtils.getEGLErrorString(GLES20.glGetError()));
	}
}
