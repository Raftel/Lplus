package com.lplus.graphics;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.lplus.graphics.LplusAtlasMgr.SCGAtlasTexture;

public class LplusMaterial {

    public static final int INVALID_TEXTURE_ID = -1; // XXX : move??
    public static final int INITIAL_COLOR = 0xFFFFFFFF;

    private static ArrayList<LplusMaterial> sReservedTexLoadingList = new ArrayList<LplusMaterial>();

    private static void addTexLoading(LplusMaterial material) {
	sReservedTexLoadingList.add(material);
    }

    /* Must be called in render thread */
    public static void doReservedTexLoading() {
	// TODO : check
	// for (Iterator<SCGMesh> iter = sReservedTexLoadingList.iterator(); iter.hasNext();)
	// iter.next().loadTexture();

	if (sReservedTexLoadingList.size() == 0)
	    return;

	synchronized (sReservedTexLoadingList) {

	    // divide texture loading
	    int count = 5;
	    if (sReservedTexLoadingList.size() < count)
		count = sReservedTexLoadingList.size();

	    while (count != 0) {
		LplusMaterial material = sReservedTexLoadingList.get(0);
		if (material != null) {
		    material.loadTexture();
		    sReservedTexLoadingList.remove(material);
		}
		count--;
	    }
	}

	// original source
	// for (int i = 0; i < sReservedTexLoadingList.size(); i++) {
	// sReservedTexLoadingList.get(i).loadTexture();
	// }
	// sReservedTexLoadingList.clear();
    }

    private int mTexture;
    private Bitmap mBitmap;
    private int mColor;
    private boolean mTexLoadingReserved;
    private boolean mDoRecycle = false;

    // for atlas
    private SCGAtlasTexture mAtlasTexture = null;
    private Bitmap mPendingBitmap = null;

    public LplusMaterial() {
	mAtlasTexture = null;
	mTexture = INVALID_TEXTURE_ID;
	mBitmap = null;
	mTexLoadingReserved = false;
	mColor = INITIAL_COLOR;
    }

    public SCGAtlasTexture getAtlasTexture() {
	return mAtlasTexture;
    }

    public int getTexture() {
	return mTexture;
    }

    public int getColor() {
	return mColor;
    }

    public Bitmap getPendingTexture() {
	return mPendingBitmap;
    }

    @SuppressWarnings(value = { "unused" })
    public void setTexture(Bitmap bitmap, boolean doRecycle) {
	if (LplusContextGLES.USING_TEXTURE_ATLAS) {
	    setTexture(bitmap);
	    return;
	}
	mBitmap = bitmap;
	mDoRecycle = doRecycle;
	mTexLoadingReserved = true;
	addTexLoading(this);
    }

    public void setTexture(SCGAtlasTexture texture) {
	mAtlasTexture = texture;
    }

    private void setTexture(Bitmap bm) {
	mPendingBitmap = bm;
    }

    public void setColor(int color) {
	mColor = color;
    }

    public void setColor(int alpha, int red, int green, int blue) {
	// range = 0 ~ 255
	mColor = Color.argb(alpha, red, green, blue);
    }

    public void setColor(float alpha, float red, float green, float blue) {
	// range = 0.0f ~ 1.0f
	mColor = Color.argb((int) (alpha * 255f), (int) (255f * red), (int) (255f * green), (int) (255f * blue));
    }

    /* Must be called in render thread */
    public void loadTexture() {
	if (mTexLoadingReserved) {
	    if (mTexture != INVALID_TEXTURE_ID) {
		if (mBitmap != null) {
		    if (mBitmap.isRecycled() == false)
			LplusUtil.loadTexture(mTexture, mBitmap);
		    else
			Log.w("SCGMaterial", "loadTexture - bitmap is recylcled");
		    
		    if (mDoRecycle)
			mBitmap.recycle();
		    mBitmap = null;
		    mDoRecycle = false;
		} else {
		    LplusUtil.unloadTexture(mTexture);
		    mTexture = INVALID_TEXTURE_ID;
		    mDoRecycle = false;
		}
	    } else {
		if (mBitmap != null) {
		    if (mBitmap.isRecycled() == false)
			mTexture = LplusUtil.loadTexture(mBitmap);
		    else
			Log.w("SCGMaterial", "loadTexture - bitmap is recylcled");
		    
		    if (mDoRecycle)
			mBitmap.recycle();
		    mBitmap = null;
		    mDoRecycle = false;
		}
	    }

	    mTexLoadingReserved = false;
	}
    }
}
