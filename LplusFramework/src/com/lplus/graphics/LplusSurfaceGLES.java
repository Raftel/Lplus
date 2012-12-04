package com.lplus.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class LplusSurfaceGLES extends GLSurfaceView {

	public LplusSurfaceGLES(Context context) {
		super(context);
		setEGLContextClientVersion(2);
	}
}
