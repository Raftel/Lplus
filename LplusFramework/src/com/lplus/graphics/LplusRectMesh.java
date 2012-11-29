package com.lplus.graphics;

public class LplusRectMesh extends LplusMesh {

	float mWidth;
	float mHeight;

	public LplusRectMesh(float width, float height) {
		super();

		// Vertex Type : P3T2
		float vertexArray[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, height, 0.0f, 0.0f, 1.0f, width, height, 0.0f, 1.0f, 1.0f, width, 0.0f, 0.0f, 1.0f, 0.0f };

		short indexArray[] = { 0, 1, 2, 2, 3, 0 };

		setVertexArray(vertexArray, 4);
		setIndexArray(indexArray, 6);

		mWidth = width;
		mHeight = height;
	}

	public void reset(float width, float height) {
		// Vertex Type : P3T2
		float vertexArray[] = { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, height, 0.0f, 0.0f, 1.0f, width, height, 0.0f, 1.0f, 1.0f, width, 0.0f, 0.0f, 1.0f, 0.0f };

		setVertexArray(vertexArray, 4);

		mWidth = width;
		mHeight = height;
	}

	@Override
	public float[] getIntersectionPoint(float[] origin, float[] ray) {

		if (ray[2] == 0.0f)
			return null;

		float point[] = new float[4];
		float t = -origin[2] / ray[2];

		point[0] = origin[0] + ray[0] * t;
		point[1] = origin[1] + ray[1] * t;
		point[2] = 0.0f;
		point[3] = 1.0f;

		if (point[0] < 0.0f || point[0] > mWidth)
			return null;
		if (point[1] < 0.0f || point[1] > mHeight)
			return null;

		return point;
	}

}
