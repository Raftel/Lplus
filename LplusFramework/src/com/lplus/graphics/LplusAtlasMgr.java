package com.lplus.graphics;

import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.FloatMath;
import android.util.Log;

public class LplusAtlasMgr {

    /* setting for atlas manager  
     * MGR_TYPE_ATLAS -> SCGAtlasMgr creates textures on atlases.
     * MGR_TYPE_STANDALONG -> SCGAtlasMgr creates stand-along textures which works individually. 
     *                        SCGAtlasMgr just manages their memory usage*/
    
    public static final int MGR_TYPE_ATLAS = 0;
    public static final int MGR_TYPE_STANDALONE = 1;
    public static final int MGR_TYPE = MGR_TYPE_STANDALONE;
        

    public static final int ATLAS_WIDTH = 1800;
    public static final int ATLAS_HEIGHT = 1800;

    private static int BOUND_TEXTURE = -1;
    private static final int ATLAS_MAX_ATLAS_PAGES = 20;
    private static final int ATLAS_MIN_ATLAS_PAGES = 1;

    public static final long ATLAS_STATUS_INIT = 0x000000;
    public static final long ATLAS_STATUS_ONE = 0x000001;
    public static final int NUM_BLOCKS_ON_ROW = 60;
    private static final long GARBAGE_COLLECTING_LIMIT_TIME = 20000;
    private static final long REQUEST_PENDING_LIMIT_TIME = 10000;

    private Vector<SCGAtlas> mAtlases;
    private LplusContextGLES mContext;

    @SuppressWarnings("all")
    public LplusAtlasMgr(LplusContextGLES context) {
	mContext = context;
	mAtlases = new Vector<SCGAtlas>();

	if (!mContext.isRunningOnRenderThread(Thread.currentThread())) {
	    Log.e("SCGAtlasMgr", "SCGAtlasMgr has not been initiated on Render thread");
	}

	for (int i = 0; i < ATLAS_MIN_ATLAS_PAGES; i++) {
	    createAtlas();
	}

    }

    private SCGAtlas createAtlas() {

	if (mAtlases.size() >= ATLAS_MAX_ATLAS_PAGES) {
	    Log.d("SCGAtlasMgr", "exceed num max atlas");
	    return null;
	}

	SCGAtlas atlas = new SCGAtlas(ATLAS_WIDTH, ATLAS_HEIGHT);
	mAtlases.add(atlas);	
	return atlas;
    }

    int mLastAddedIndex = 0;

    @SuppressWarnings("all")
    public SCGAtlasTexture createTexture(Bitmap bm, boolean recycle) {

	if (!mContext.isRunningOnRenderThread(Thread.currentThread())) {
	    Log.e("CoreBench", "createTexture can only effect on render thread");
	    return null;
	}

	if (bm == null || bm.getWidth() > ATLAS_WIDTH || bm.getHeight() > ATLAS_HEIGHT) {
	    Log.d("SCGAtlasMgr", "texture creation failed");
	    return null;
	}

	SCGAtlasTexture tex = null;

	if (MGR_TYPE == MGR_TYPE_ATLAS) {
	    for (int i = 0; i < mAtlases.size(); i++) {
		tex = mAtlases.get((i + mLastAddedIndex) % mAtlases.size()).genTexture(bm);
		if (tex != null) {
		    Log.d("SCGAtlasMgr", "texture created on " + (i + mLastAddedIndex) % mAtlases.size());
		    mLastAddedIndex = i;
		    break;
		}
	    }

	    if (tex == null) {
		SCGAtlas atlas = createAtlas();
		if (atlas != null) {
		    mLastAddedIndex = mAtlases.size() - 1;
		    tex = atlas.genTexture(bm);
		}
	    }

	    if (recycle && bm != null) {
		bm.recycle();
		bm = null;
	    }
	} else if (MGR_TYPE == MGR_TYPE_STANDALONE) {
	    tex = mAtlases.firstElement().genTexture(bm);
	    if (recycle) {
		bm.recycle();
		bm = null;
	    }
	}

	return tex;

    }

    private class SCGAtlas {

	private int mTextureID;
	private long[] mAtlasStatusMap; // 0 = vacant, 1 = on use.
	private int[] mAtlasRemainingRowSpaces;
	private int mRemainingSpace;

	private int mWidth, mHeight, mNumBlocksWidth, mNumBlocksHeight;
	private float mMinBlockSize;
	private Vector<SCGAtlasTexture> mContainingTextures;
	private boolean mAtlasReady = false;

	@SuppressWarnings("all")
	public SCGAtlas(final int width, final int height) {

	    // generating texture
	    mWidth = width;
	    mHeight = height;

	    mContainingTextures = new Vector<SCGAtlasTexture>();

	    if (MGR_TYPE == MGR_TYPE_ATLAS) {
		mNumBlocksWidth = NUM_BLOCKS_ON_ROW; // Integer size
		mMinBlockSize = (float) mWidth / (float) mNumBlocksWidth;
		mNumBlocksHeight = (int) (FloatMath.floor((float) mHeight / mMinBlockSize));
		mAtlasStatusMap = new long[mNumBlocksHeight];
		mAtlasRemainingRowSpaces = new int[mNumBlocksHeight];
		mRemainingSpace = mNumBlocksHeight * NUM_BLOCKS_ON_ROW;

		Bitmap bm = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
		bm.eraseColor(Color.TRANSPARENT);

		mTextureID = LplusUtil.loadTexture(bm);
		BOUND_TEXTURE = mTextureID;

		for (int i = 0; i < mAtlasStatusMap.length; i++) {
		    mAtlasStatusMap[i] = ATLAS_STATUS_INIT;
		    mAtlasRemainingRowSpaces[i] = mNumBlocksWidth;
		}

		bm.recycle();
		bm = null;
	    } else if (MGR_TYPE == MGR_TYPE_STANDALONE) {

	    }

	    mAtlasReady = true;

	}

	// TO DO : need to be optimized
	private float[] getPosition(float width, float height) {

	    int numWidthBlocks = (int) (FloatMath.ceil(width / mMinBlockSize));
	    int numHeightBlocks = (int) (FloatMath.ceil(height / mMinBlockSize));

	    if (numWidthBlocks * numHeightBlocks > mRemainingSpace)
		return null;

	    long blockMask = ATLAS_STATUS_INIT;
	    for (int i = 0; i < numWidthBlocks; i++) {
		blockMask <<= 1;
		blockMask += 1;
	    }
	    blockMask <<= (mNumBlocksWidth - numWidthBlocks);

	    for (int i = 0; i <= mNumBlocksHeight - numHeightBlocks; i++) {
		long tmpBlockMask = blockMask;
		if (numWidthBlocks > mAtlasRemainingRowSpaces[i])
		    continue;
		for (int j = 0; j <= mNumBlocksWidth - numWidthBlocks; j++) {
		    notfound: if ((mAtlasStatusMap[i] & tmpBlockMask) == 0 && (mAtlasStatusMap[i + numHeightBlocks - 1] & tmpBlockMask) == 0) {
			for (int k = 0; k < numHeightBlocks; k++) {
			    if (numWidthBlocks > mAtlasRemainingRowSpaces[k + i] || (mAtlasStatusMap[k + i] & tmpBlockMask) != 0) {
				break notfound;
			    }
			}
			return new float[] { (float) j * mMinBlockSize, (float) i * mMinBlockSize, width, height };
		    }
		    tmpBlockMask >>= 1;
		}
	    }

	    return null;

	}

	// TO DO : need to be optimized
	private void checkAtlas(float offsetX, float offsetY, float width, float height, boolean checkOrUncheck) {

	    int numWidthBlocks = (int) (FloatMath.ceil(width / mMinBlockSize));
	    long blockMask = ATLAS_STATUS_INIT;
	    for (int i = 0; i < numWidthBlocks; i++) {
		blockMask <<= 1;
		blockMask += 1;
	    }

	    int numShift = (int) (FloatMath.floor(mNumBlocksWidth - numWidthBlocks - (offsetX) / mMinBlockSize));
	    blockMask <<= numShift;

	    int blockedOffsetY = (int) (FloatMath.floor((offsetY) / mMinBlockSize));
	    int blockedNumY = (int) (FloatMath.ceil(height / mMinBlockSize));

	    for (int i = 0; i < blockedNumY; i++) {
		if (checkOrUncheck) {
		    mAtlasStatusMap[blockedOffsetY + i] |= blockMask;
		    mAtlasRemainingRowSpaces[blockedOffsetY + i] -= numWidthBlocks;
		    mRemainingSpace -= numWidthBlocks;
		} else {
		    mAtlasStatusMap[blockedOffsetY + i] &= ~blockMask;
		    mAtlasRemainingRowSpaces[blockedOffsetY + i] += numWidthBlocks;
		    mRemainingSpace += numWidthBlocks;
		}

	    }

	}

	@SuppressWarnings("all")
	private SCGAtlasTexture genTexture(Bitmap bm) {

	    if (!mAtlasReady || bm == null || bm.isRecycled() || bm.getWidth() > mWidth || bm.getHeight() > mHeight)
		return null;

	    gc();

	    if (MGR_TYPE == MGR_TYPE_ATLAS) {
		final float[] pos = getPosition(bm.getWidth(), bm.getHeight());

		if (pos == null)
		    return null;

		bind();
		GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, (int) pos[0], (int) pos[1], bm);

		checkAtlas(pos[0], pos[1], pos[2], pos[3], true);

		SCGAtlasTexture tex = new SCGAtlasTexture(pos[0], pos[1], pos[2], pos[3], this);
		mContainingTextures.add(tex);
		return tex;

	    } else if (MGR_TYPE == MGR_TYPE_STANDALONE) {
		SCGAtlasTexture tex = new SCGAtlasTexture(bm, this);
		mContainingTextures.add(tex);
		return tex;
	    }
	    return null;

	}

	private void bind() {

	    if (BOUND_TEXTURE != mTextureID) {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
		BOUND_TEXTURE = mTextureID;

	    }
	}

	@SuppressWarnings("all")
	private void recycleTexture(SCGAtlasTexture tex) {
	    if (tex == null)
		return;

	    if (MGR_TYPE == MGR_TYPE_ATLAS) {
		checkAtlas(tex.mAtlasCoordOffsetX, tex.mAtlasCoordOffsetY, tex.mAtlasCoordWidth, tex.mAtlasCoordHeight, false);
		mContainingTextures.remove(tex);
		tex.mIsRecycled = true;

		if (mContainingTextures.size() == 0) {
		    destroy();
		}
	    } else if (MGR_TYPE == MGR_TYPE_STANDALONE) {
		LplusUtil.unloadTexture(tex.mTextureID);
		mContainingTextures.remove(tex);
		tex.mIsRecycled = true;
	    }

	}

	private void destroy() {

	    if (mContainingTextures.size() > 0)
		return;

	    LplusUtil.unloadTexture(mTextureID);
	    mAtlases.remove(this);
	    mLastAddedIndex = mAtlases.size() - 1;
	    Log.d("SCGAtlasMgr", "atlas destroyed : " + mTextureID);
	}

	private void gc() {
	    long curTime = System.currentTimeMillis();
	    int numRecycled = 0;
	    for (int i = 0; i < mContainingTextures.size();) {
		SCGAtlasTexture tex = mContainingTextures.get(i);
		long timediff = (curTime - tex.mLastUsedTime);
		if ((tex.mIsRecycleRequested && timediff > REQUEST_PENDING_LIMIT_TIME) || timediff > GARBAGE_COLLECTING_LIMIT_TIME) {
		    numRecycled++;
		    recycleTexture(tex);
		} else {
		    i++;
		}
	    }
	    if (numRecycled > 0)
		Log.d("SCGAtlasMgr", "GC : " + numRecycled);
	}

    }

    public class SCGAtlasTexture {

	private float mAtlasCoordOffsetX, mAtlasCoordOffsetY;
	private float mAtlasCoordWidth, mAtlasCoordHeight;
	private float mTexCoordOffsetX, mTexCoordOffsetY;
	private float mTexCoordWidth, mTexCoordHeight;
	private SCGAtlas mContainingAtlas;
	private long mLastUsedTime;
	private boolean mIsRecycled, mIsRecycleRequested;

	// variables only for stand-alone textures
	private int mTextureID = LplusMaterial.INVALID_TEXTURE_ID;

	private SCGAtlasTexture(float atlasCoordOffsetX, float atlasCoordOffsetY, float atlasCoordWidth, float atlasCoordHeight, SCGAtlas containingAtlas) {
	    mAtlasCoordOffsetX = atlasCoordOffsetX;
	    mAtlasCoordOffsetY = atlasCoordOffsetY;
	    mAtlasCoordWidth = atlasCoordWidth;
	    mAtlasCoordHeight = atlasCoordHeight;

	    mTexCoordOffsetX = atlasCoordOffsetX / (float) ATLAS_WIDTH;
	    mTexCoordOffsetY = atlasCoordOffsetY / (float) ATLAS_WIDTH;
	    mTexCoordWidth = atlasCoordWidth / (float) ATLAS_HEIGHT;
	    mTexCoordHeight = atlasCoordHeight / (float) ATLAS_HEIGHT;

	    mContainingAtlas = containingAtlas;
	    mLastUsedTime = System.currentTimeMillis();
	    mIsRecycled = false;
	    mIsRecycleRequested = false;
	}

	// stand-alone texture
	private SCGAtlasTexture(Bitmap bm, SCGAtlas containingAtlas) {
	    if (bm == null)
		return;

	    mAtlasCoordOffsetX = 0;
	    mAtlasCoordOffsetY = 0;
	    mAtlasCoordWidth = bm.getWidth();
	    mAtlasCoordHeight = bm.getHeight();

	    mTexCoordOffsetX = 0;
	    mTexCoordOffsetY = 0;
	    mTexCoordWidth = 1;
	    mTexCoordHeight = 1;

	    mLastUsedTime = System.currentTimeMillis();
	    mTextureID = LplusUtil.loadTexture(bm);
	    mContainingAtlas = containingAtlas;

	}

	public float[] getTextureCoords() {
	    // offsetX, offsetY, width, height
	    return new float[] { mTexCoordOffsetX, mTexCoordOffsetY, mTexCoordWidth, mTexCoordHeight };
	}

	public float[] getAtlasCoords() {
	    // offsetX, offsetY, width, height
	    return new float[] { mAtlasCoordOffsetX, mAtlasCoordOffsetY, mAtlasCoordWidth, mAtlasCoordHeight };
	}

	@SuppressWarnings("all")
	public void bind() {
	    mLastUsedTime = System.currentTimeMillis();
	    if (MGR_TYPE == MGR_TYPE_ATLAS) {
		mContainingAtlas.bind();
	    } else if (MGR_TYPE == MGR_TYPE_STANDALONE) {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		if (mTextureID != LplusMaterial.INVALID_TEXTURE_ID)
		    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
	    }

	}

	public void destroy() {
	    if (!mIsRecycled) {
		mIsRecycleRequested = true;
		// mContainingAtlas.recycleTexture(this);
	    }
	}

	public boolean isRecycled() {
	    return mIsRecycled;
	}

    }

}
