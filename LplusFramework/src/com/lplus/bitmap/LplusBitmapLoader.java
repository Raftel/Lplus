package com.lplus.bitmap;


public class LplusBitmapLoader {

//    private Context mContext;
//    private LruCache<String, Bitmap> mMemoryCache;
//
//    public LplusBitmapLoader(Context context, CBGalleryResourceManager resourceManager) {
//	mContext = context;
//	// Get memory class of this device, exceeding this amount will throw an
//	// OutOfMemory exception.
//	final int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
//
//	// Use 1/8th of the available memory for this memory cache.
//	final int cacheSize = 1024 * 1024 * memClass / 8;
//
//	mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//	    protected int sizeOf(String key, Bitmap bitmap) {
//		// The cache size will be measured in bytes rather than number
//		// of items.
//		return bitmap.getByteCount();
//	    }
//	};	
//    }
//
//    public String getPhotoCacheKey(CBGalleryPhotoData data) {
//	String key = null;
//
//	switch (data.getReourceType()) {
//	case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_FILE:
//	    key = String.valueOf(data.getResourcePath());
//	    break;
//	case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_DB:
//	    key = String.valueOf(data.getMediaId());
//	    break;
//	case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_RESID:
//	    key = String.valueOf(data.getResourceId());
//	    break;
//	default:
//	    break;
//	}
//
//	return key;
//    }
//
//    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//	if (getBitmapFromMemCache(key) == null) {
//	    mMemoryCache.put(key, bitmap);
//	}
//    }
//
//    public Bitmap getBitmapFromMemCache(String key) {
//	if (key == null)
//	    return null;
//
//	return (Bitmap) mMemoryCache.get(key);
//    }
//
//    public void asyncLoadBitmapWithCache(CBGalleryPhotoData data, ImageView imageView, int reqW, int reqH) {
//	final String imageKey = getPhotoCacheKey(data);
//	Bitmap bitmap = null;
//
//	if (!SCGContextGLES.USING_TEXTURE_ATLAS)
//	    bitmap = getBitmapFromMemCache(imageKey);
//
//	if (bitmap != null) {
//	    imageView.setImageBitmap(bitmap);
//	} else {
//	    if (cancelPotentialWork(data, imageView)) {
//		final BitmapLoaderTask task = new BitmapLoaderTask(imageView, reqW, reqH);
//		final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), null, task);
//		imageView.setImageDrawable(asyncDrawable);
//		task.execute(data);
//	    }
//	}
//    }   
//
//    public void asyncLoadBitmapWithCache(final CBGalleryPhotoData data, final SCGModel model, final int reqW, final int reqH) {
//
//	final String imageKey = getPhotoCacheKey(data);
//
//	Bitmap bitmap = null;
//
//	if (!SCGContextGLES.USING_TEXTURE_ATLAS)
//	    bitmap = getBitmapFromMemCache(imageKey);
//
//	if (bitmap != null) {
//	    model.setTexture(bitmap);
//	} else {
//	    Activity av = (Activity) mContext;
//	    av.runOnUiThread(new Runnable() {
//
//		@Override
//		public void run() {
//
//		    if (cancelPotentialWork(data, model)) {
//			final TexLoaderTask task = new TexLoaderTask(model, reqW, reqH, TexLoaderTask.PHOTO_TYPE_NORMAL, TexLoaderTask.CACHE_TYPE_MEM);
//			if (model instanceof CBGallery3DModel) {
//			    ((CBGallery3DModel) model).setTexLoaderTask(task);
//			}
//			task.execute(data);
//		    }
//		}
//
//	    });
//	}
//    }
//    
//    public void asyncLoadBitmap(final CBGalleryPhotoData data, final SCGModel model, final int reqW, final int reqH) {
//	Activity av = (Activity) mContext;
//	av.runOnUiThread(new Runnable() {
//
//	    @Override
//	    public void run() {
//
//		if (cancelPotentialWork(data, model)) {
//		    final TexLoaderTask task = new TexLoaderTask(model, reqW, reqH, TexLoaderTask.PHOTO_TYPE_NORMAL, TexLoaderTask.CACHE_TYPE_NONE);
//		    if (model instanceof CBGallery3DModel) {
//			((CBGallery3DModel) model).setTexLoaderTask(task);
//		    }
//		    task.execute(data);
//		}
//	    }
//	});
//
//     }
//    
//    public void asyncLoadCenterCropedBitmapWithCache(final CBGalleryPhotoData data, final SCGModel model, final int reqW, final int reqH) {
//
//	final String imageKey = getPhotoCacheKey(data) + "croped";
//
//	Bitmap bitmap = null;
//
//	if (!SCGContextGLES.USING_TEXTURE_ATLAS)
//	    bitmap = getBitmapFromMemCache(imageKey);
//
//	if (bitmap != null) {
//	    model.setTexture(bitmap);
//	} else {
//	    Activity av = (Activity) mContext;
//	    av.runOnUiThread(new Runnable() {
//
//		@Override
//		public void run() {
//
//		    if (cancelPotentialWork(data, model)) {
//			final TexLoaderTask task = new TexLoaderTask(model, reqW, reqH, TexLoaderTask.PHOTO_TYPE_CROP, TexLoaderTask.CACHE_TYPE_MEM);
//			if (model instanceof CBGallery3DModel) {
//			    ((CBGallery3DModel) model).setTexLoaderTask(task);
//			}
//			task.execute(data);
//		    }
//		}
//
//	    });
//	}
//    }
//    
//    public void ayncloadCenterCropedBitmap(final CBGalleryPhotoData data, final SCGModel model, final int reqW, final int reqH) {
//	Bitmap bitmap = null;
//
//	Activity av = (Activity) mContext;
//	av.runOnUiThread(new Runnable() {
//
//	    @Override
//	    public void run() {
//
//		if (cancelPotentialWork(data, model)) {
//		    final TexLoaderTask task = new TexLoaderTask(model, reqW, reqH, TexLoaderTask.PHOTO_TYPE_CROP, TexLoaderTask.CACHE_TYPE_NONE);
//		    if (model instanceof CBGallery3DModel) {
//			((CBGallery3DModel) model).setTexLoaderTask(task);
//		    }
//		    task.execute(data);
//		}
//	    }
//
//	});
//    }
//
//    public Bitmap loadBitmap(CBGalleryPhotoData data, int reqW, int reqH) {
//	Bitmap bitmap = null;
//
//	switch (data.getReourceType()) {
//	case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_FILE:
//	    bitmap = decodeSampledBitmapFromFile(data.getResourcePath(), reqW, reqH);
//	    break;
//	case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_DB:
//	    Uri uri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, data.getMediaId());
//	    try {
//		bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
//	    } catch (FileNotFoundException e) {
//		e.printStackTrace();
//	    } catch (IOException e) {
//		e.printStackTrace();
//	    }
//	    break;
//	case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_RESID:
//	    bitmap = decodeSampledBitmapFromResource(mContext.getResources(), data.getResourceId(), reqW, reqH);
//	    break;
//	default:
//	    break;
//	}
//
//	return bitmap;
//    }
//
//    public static boolean cancelPotentialWork(CBGalleryPhotoData data, ImageView imageView) {
//	final BitmapLoaderTask task = getBitmapLoaderTask(imageView);
//
//	if (task != null) {
//	    final CBGalleryPhotoData bitmapData = task.data;
//	    if (bitmapData != data) {
//		// Cancel previous task
//		task.cancel(true);
//	    } else {
//		// The same work is already in progress
//		return false;
//	    }
//	}
//	// No task associated with the ImageView, or an existing task was
//	// cancelled
//	return true;
//    }
//
//    public static boolean cancelPotentialWork(CBGalleryPhotoData data, SCGModel model) {
//	if (model instanceof CBGallery3DModel) {
//	    final TexLoaderTask task = ((CBGallery3DModel) model).getTexLoaderTask();
//
//	    if (task != null) {
//		final CBGalleryPhotoData bitmapData = task.data;
//		if (bitmapData != data) {
//		    // Cancel previous task
//		    task.cancel(true);
//		} else {
//		    // The same work is already in progress
//		    return false;
//		}
//	    }
//	}
//	// No task associated with the ImageView, or an existing task was
//	// cancelled
//	return true;
//    }
//
//    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
//
//	// First decode with inJustDecodeBounds=true to check dimensions
//	final BitmapFactory.Options options = new BitmapFactory.Options();
//	options.inJustDecodeBounds = true;
//	BitmapFactory.decodeResource(res, resId, options);
//
//	// Calculate inSampleSize
//	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//	// Decode bitmap with inSampleSize set
//	options.inJustDecodeBounds = false;
//	return BitmapFactory.decodeResource(res, resId, options);
//    }
//
//    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
//
//	// First decode with inJustDecodeBounds=true to check dimensions
//	final BitmapFactory.Options options = new BitmapFactory.Options();
//	options.inJustDecodeBounds = true;
//	BitmapFactory.decodeFile(path, options);
//
//	// Calculate inSampleSize
//	options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//	// Decode bitmap with inSampleSize set
//	options.inJustDecodeBounds = false;
//	return BitmapFactory.decodeFile(path, options);
//    }
//
//    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//	// Raw height and width of image
//	final int height = options.outHeight;
//	final int width = options.outWidth;
//	int inSampleSize = 1;
//
//	if (height > reqHeight || width > reqWidth) {
//	    if (width > height) {
//		inSampleSize = Math.round((float) height / (float) reqHeight);
//	    } else {
//		inSampleSize = Math.round((float) width / (float) reqWidth);
//	    }
//	}
//	return inSampleSize;
//    }
//
//    private static BitmapLoaderTask getBitmapLoaderTask(ImageView imageView) {
//	if (imageView != null) {
//	    final Drawable drawable = imageView.getDrawable();
//
//	    if (drawable instanceof AsyncDrawable) {
//		final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
//		return asyncDrawable.getBitmapWorkerTask();
//	    }
//	}
//	return null;
//    }
//
//    class AsyncDrawable extends BitmapDrawable {
//	private final WeakReference<BitmapLoaderTask> bitmapLoaderTaskReference;
//
//	public AsyncDrawable(Resources res, Bitmap bitmap, BitmapLoaderTask bitmapWorkerTask) {
//	    super(res, bitmap);
//	    bitmapLoaderTaskReference = new WeakReference<BitmapLoaderTask>(bitmapWorkerTask);
//	}
//
//	public BitmapLoaderTask getBitmapWorkerTask() {
//	    return bitmapLoaderTaskReference.get();
//	}
//    }
//
//    // class BitmapLoaderTask extends AsyncTask<Integer, Void, Bitmap> {
//    class BitmapLoaderTask extends AsyncTask<CBGalleryPhotoData, Void, Bitmap> {
//	private final WeakReference<ImageView> mImageViewReference;
//	private int mReqWidth;
//	private int mReqHeight;
//	private CBGalleryPhotoData data = null;
//
//	public BitmapLoaderTask(ImageView imageView, int reqWidth, int reqHeight) {
//	    // Use a WeakReference to ensure the ImageView can be garbage
//	    // collected
//	    mImageViewReference = new WeakReference<ImageView>(imageView);
//	    mReqWidth = reqWidth;
//	    mReqHeight = reqHeight;
//	}
//
//	// Decode image in background.
//	@Override
//	protected Bitmap doInBackground(CBGalleryPhotoData... params) {
//	    Bitmap bitmap = null;
//	    data = params[0];
//	    switch (data.getReourceType()) {
//	    case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_FILE:
//		bitmap = decodeSampledBitmapFromFile(data.getResourcePath(), mReqWidth, mReqHeight);
//		break;
//	    case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_DB:
//		break;
//	    case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_RESID:
//		bitmap = decodeSampledBitmapFromResource(mContext.getResources(), data.getResourceId(), mReqWidth, mReqHeight);
//		break;
//	    default:
//		break;
//	    }
//
//	    if (!SCGContextGLES.USING_TEXTURE_ATLAS) {
//		if (bitmap != null) {
//		    addBitmapToMemoryCache(getPhotoCacheKey(data), bitmap);
//		}
//	    }
//
//	    return bitmap;
//	}
//
//	// Once complete, see if ImageView is still around and set bitmap.
//	@Override
//	protected void onPostExecute(Bitmap bitmap) {
//	    if (isCancelled()) {
//		bitmap = null;
//	    }
//
//	    if (mImageViewReference != null && bitmap != null) {
//		final ImageView imageView = mImageViewReference.get();
//		final BitmapLoaderTask bitmapWorkerTask = getBitmapLoaderTask(imageView);
//		if (this == bitmapWorkerTask && imageView != null) {
//		    BitmapDrawable bd = (BitmapDrawable) imageView.getDrawable();
//
//		    if (bd != null) {
//			Bitmap image = bd.getBitmap();
//
//			if (image != null)
//			    image.recycle();
//		    }
//
//		    imageView.setImageBitmap(bitmap);
//		}
//	    }
//	}
//    }
//
//    public class TexLoaderTask extends AsyncTask<CBGalleryPhotoData, Void, Bitmap> {
//
//	public static final int PHOTO_TYPE_NORMAL = 0;
//	public static final int PHOTO_TYPE_CROP = 1;
//	
//	public static final int CACHE_TYPE_NONE = 0;
//	public static final int CACHE_TYPE_MEM = 1;	
//
//	private final WeakReference<SCGModel> mModel;
//	private CBGalleryPhotoData data = null;
//	private int mReqWidth;
//	private int mReqHeight;
//	private int mPhotoType = PHOTO_TYPE_NORMAL;
//	private int mCacheType = CACHE_TYPE_MEM;
//	private BitmapLoaderCallback mCallback = null;
//
//	public TexLoaderTask(SCGModel model, int reqWidth, int reqHeight, int photoType, int lodingType) {
//	    // Use a WeakReference to ensure the ImageView can be garbage
//	    // collected
//	    mModel = new WeakReference<SCGModel>(model);
//	    mReqWidth = reqWidth;
//	    mReqHeight = reqHeight;
//	    mPhotoType = photoType;
//	    mCacheType = lodingType;
//	}
//
//	public void setCallback(BitmapLoaderCallback callback) {
//	    mCallback = callback;
//	}
//
//	public BitmapLoaderCallback getCallback() {
//	    return mCallback;
//	}
//
//	// Decode image in background.
//	@Override
//	protected Bitmap doInBackground(CBGalleryPhotoData... params) {
//	    Bitmap bitmap = null;
//	    data = params[0];
//
//	    synchronized (data) {
//
//		if (!SCGContextGLES.USING_TEXTURE_ATLAS || mCacheType != CACHE_TYPE_NONE) {
//		    if (mPhotoType == PHOTO_TYPE_NORMAL)
//			bitmap = getBitmapFromMemCache((getPhotoCacheKey(data)));
//		    else if (mPhotoType == PHOTO_TYPE_CROP)
//			bitmap = getBitmapFromMemCache((getPhotoCacheKey(data) + "croped"));
//		}
//
//		if (bitmap == null) {
//		    Bitmap tempBitmap = null;
//
//		    switch (data.getReourceType()) {
//		    case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_FILE:
//			tempBitmap = decodeSampledBitmapFromFile(data.getResourcePath(), mReqWidth, mReqHeight);
//			break;
//		    case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_DB: 
//			
//			if (mPhotoType == PHOTO_TYPE_CROP) {
//			    BitmapFactory.Options options = new BitmapFactory.Options();
//			    options.inJustDecodeBounds = true;
//			    options.inSampleSize = calculateInSampleSize(options, mReqWidth, mReqHeight);
//			    options.inJustDecodeBounds = false;
//			    tempBitmap = MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), data.getMediaId(), Images.Thumbnails.MICRO_KIND, options);
//			} else if (mPhotoType == PHOTO_TYPE_NORMAL) {
//			    tempBitmap = decodeSampledBitmapFromFile(data.getResourcePath(), mReqWidth, mReqHeight);
////			    Uri uri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, data.getMediaId()); 
////			    try {
////				tempBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
////				
////			    } catch (FileNotFoundException e) {
////				e.printStackTrace();
////			    } catch (IOException e) {
////				e.printStackTrace();
////			    }
//			}
//		    
//			break;
//		    case CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_RESID:
//			tempBitmap = decodeSampledBitmapFromResource(mContext.getResources(), data.getResourceId(), mReqWidth, mReqHeight);
//			break;
//		    default:
//			break;
//		    }
//
//		    if (tempBitmap != null) {
//			if (mPhotoType == PHOTO_TYPE_NORMAL) {
//			    bitmap = tempBitmap;
//			} else if (mPhotoType == PHOTO_TYPE_CROP) {
//			    if (data.getReourceType() != CBGalleryPhotoData.CB_GALLERY_PHOTO_RESOURCE_TYPE_DB) {
//				int cropSize = Math.min(tempBitmap.getWidth(), tempBitmap.getHeight());
//				bitmap = CBBitmapUtil.cropCenterBitmap(tempBitmap, cropSize, cropSize);
//				tempBitmap.recycle();
//				tempBitmap = null;
//			    } else {
//				bitmap = tempBitmap;
//			    }
//			}
//			
//			if (!SCGContextGLES.USING_TEXTURE_ATLAS || mCacheType != CACHE_TYPE_NONE) {
//			    if (bitmap != null) {
//				addBitmapToMemoryCache((getPhotoCacheKey(data) + "croped"), bitmap);
//			    }
//			}
//		    }
//		}
//	    }
//	    return bitmap;
//	}
//
//	// Once complete, see if ImageView is still around and set bitmap.
//	@Override
//	protected void onPostExecute(Bitmap bitmap) {
//	    if (isCancelled()) {
//		bitmap = null;
//	    }
//
//	    if (mModel != null && bitmap != null) {
//		final SCGModel model = mModel.get();
//		if (model != null) {
//
//		    if (mCacheType == CACHE_TYPE_NONE)
//			model.setTexture(bitmap, true);
//		    else 
//			model.setTexture(bitmap);
//
//		    if (model instanceof CBGallery3DModel) {
//			((CBGallery3DModel) model).setTexLoaderTask(null);
//		    }
//		}
//	    }
//
//	    if (mCallback != null)
//		mCallback.onBitmapLoaded(bitmap);
//	}
//    }
//
//    public interface BitmapLoaderCallback {
//	public void onBitmapLoaded(Bitmap bitmap);
//    }
}
