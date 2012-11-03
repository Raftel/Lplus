package com.lplus.bitmap;


public class LplusBitmapData {

    public static final int CB_GALLERY_PHOTO_RESOURCE_TYPE_NONE = -1;
    public static final int CB_GALLERY_PHOTO_RESOURCE_TYPE_FILE = 0;
    public static final int CB_GALLERY_PHOTO_RESOURCE_TYPE_DB = 1;
    public static final int CB_GALLERY_PHOTO_RESOURCE_TYPE_RESID = 2;

    private int mPhotoIndex;

    private int mResourceType;
    private String mResourcePath;
    private int mResourceIndex;
    private int mResourceId;
    private long mMediaId;

    public LplusBitmapData(int index) {
	mPhotoIndex = index;
	mResourceType = CB_GALLERY_PHOTO_RESOURCE_TYPE_NONE;
	mResourceIndex = -1;
	mResourceId = -1;
    }
    
    public long getMediaId() {
	return mMediaId;
    }
    
    public void setMediaId(long id) {
	mMediaId = id;
    }

    public int getPhotoIndex() {
	return mPhotoIndex;
    }

    public int getReourceType() {
	return mResourceType;
    }

    public void setResourceType(int type) {
	mResourceType = type;
    }

    public String getResourcePath() {
	return mResourcePath;
    }

    public void setResourcePath(String path) {
	mResourcePath = path;
    }

    public int getResourceIndex() {
	return mResourceIndex;
    }

    public void setResourceIndex(int index) {
	mResourceIndex = index;
    }

    public int getResourceId() {
	return mResourceId;
    }

    public void setResourceId(int id) {
	mResourceId = id;
    }
}
