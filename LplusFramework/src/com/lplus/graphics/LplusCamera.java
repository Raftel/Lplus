package com.lplus.graphics;

import android.opengl.Matrix;

public class LplusCamera extends LplusNode {

	float[] mProjMat = new float[16];
	
    public LplusCamera() {
    	Matrix.setIdentityM(mProjMat, 0);
    }
    
    public void setFrustum(float width, float height) {
    	Matrix.setIdentityM(mProjMat, 0);
    	Matrix.frustumM(mProjMat, 0, -width * 0.25f, width * 0.25f, -height * 0.25f, height * 0.25f, height * 0.5f, height * 10.0f);    	
    	mProjMat[5] = -mProjMat[5];    	
    	Matrix.translateM(mProjMat, 0, mProjMat, 0, -width * 0.5f, -height * 0.5f, -height);
    }
    
    public void setPerspective(float fovy, float aspect, float near, float far) {
    	float frustumW, frustumH;

    	frustumH = (float) (Math.tan(fovy / 360.0f * Math.PI) * near);
    	frustumW = frustumH * aspect;

    	Matrix.setIdentityM(mProjMat, 0);
    	Matrix.frustumM(mProjMat, 0, -frustumW, frustumW, -frustumH, frustumH, near, far);
    	mProjMat[5] = -mProjMat[5];    	
    	Matrix.translateM(mProjMat, 0, mProjMat, 0, -720 * 0.5f, -1280 * 0.5f, -1280);
    }
    
    public void setLookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ) {
    	float		x0, x1, x2;
    	float		y0, y1, y2;
    	float		z0, z1, z2;
    	float		angX, angY, angZ;
    	float 		mag;
    	
    	/* Make rotation matrix */
    	/* Z vector */
    	z0 = eyeX - centerX;
    	z1 = eyeY - centerY;
    	z2 = eyeZ - centerZ;

    	/* Y vector */
    	y0 = upX;
    	y1 = upY;
    	y2 = upZ;
    	
    	/* X vector = Y cross Z */
    	x0 =  y1 * z2 - y2 * z1;
    	x1 = -y0 * z2 + y2 * z0;
    	x2 =  y0 * z1 - y1 * z0;
    	
    	/* Recompute Y = Z cross X */
    	y0 =  z1 * x2 - z2 * x1;
    	y1 = -z0 * x2 + z2 * x0;
    	y2 =  z0 * x1 - z1 * x0;

    	/* mpichler, 19950515 */
    	/* cross product gives area of parallelogram, which is < 1.0 for
    	 * non-perpendicular unit-length vectors; so normalize x, y here
    	 */
    	mag = (float) (1.0f / Math.sqrt(x0 * x0 + x1 * x1 + x2 * x2));
    	x0 *= mag;
    	x1 *= mag;
    	x2 *= mag;
    	
    	mag = (float) (1.0f / Math.sqrt(y0 * y0 + y1 * y1 + y2 * y2));
    	y0 *= mag;
    	y1 *= mag;
    	y2 *= mag;


    	/* this is rotate matrix, calculate angles */
    	angX = (float) Math.atan2(z1, z2);
    	angY = (float) Math.atan2(-z0 * Math.sin(angX), z1);
    	angZ = (float) Math.atan2(y0, x0);

    	setTranslation(eyeX, eyeY, eyeZ);
    	
    	float rotX = (float) (angX * (180.0f / Math.PI));
    	float rotY = (float) (angY * (180.0f / Math.PI));
    	float rotZ = (float) (angZ * (180.0f / Math.PI));
    	
    	setRotation(rotX, rotY, rotZ);
    }
    
    public float[] getProjectionMatrix() {
    	return mProjMat;
    }
}
