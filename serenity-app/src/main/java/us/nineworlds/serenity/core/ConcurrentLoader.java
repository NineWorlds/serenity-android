package us.nineworlds.serenity.core;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.OnImageLoadedListener;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.loader.Loader;
import com.novoda.imageloader.core.loader.util.LoaderTask;
import com.novoda.imageloader.core.model.ImageWrapper;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentLoader implements Loader {

    private final LoaderSettings loaderSettings;

    private WeakReference<OnImageLoadedListener> onImageLoadedListener;
    
	private ExecutorService taskExecutorService;


    public ConcurrentLoader(LoaderSettings loaderSettings) {
        this.loaderSettings = loaderSettings;
        taskExecutorService = Executors.newFixedThreadPool(5);
    }

    public void load(ImageView imageView) {
        if (!isValidImageView(imageView)) {
            Log.w("ImageLoader", "You should never call load if you don't set a ImageTag on the view");
            return;
        }
        loadBitmap(new ImageWrapper(imageView));
    }

    private boolean isValidImageView(ImageView imageView) {
        return imageView.getTag() != null;
    }

    private void loadBitmap(ImageWrapper w) {
        if (!isTaskAlreadyRunning(w)) {
            if (isBitmapAlreadyInCache(getCachedBitmap(w))) {
                Bitmap cachedBitmap = getCachedBitmap(w);
                w.setBitmap(cachedBitmap);
                return;
            }
            setDefaultImage(w);
            if (!w.isUseCacheOnly()) {
                startTask(w);
            }
        }
    }

    private boolean isBitmapAlreadyInCache(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    private Bitmap getCachedBitmap(ImageWrapper w) {
        return loaderSettings.getCacheManager().get(w.getUrl(), w.getHeight(), w.getWidth());
    }

    private void setDefaultImage(ImageWrapper w) {
        if (hasPreviewUrl(w.getPreviewUrl())) {
            if (isBitmapAlreadyInCache(getPreviewCachedBitmap(w))) {
                w.setBitmap(getPreviewCachedBitmap(w));
            } else {
                w.setResourceBitmap(getResourceAsBitmap(w, w.getLoadingResourceId()));
            }
        } else {
            w.setResourceBitmap(getResourceAsBitmap(w, w.getLoadingResourceId()));
        }
    }

    private Bitmap getPreviewCachedBitmap(ImageWrapper w) {
        return loaderSettings.getCacheManager().get(w.getPreviewUrl(), w.getPreviewHeight(), w.getPreviewWidth());
    }

    private void startTask(ImageWrapper w) {
        try {
            LoaderTask task = createTask(w);
            w.setLoaderTask(task);
            taskExecutorService.submit(new TaskRunnable(task));
            //task.execute();
        } catch (ImageNotFoundException inf) {
            w.setResourceBitmap(getResourceAsBitmap(w, w.getNotFoundResourceId()));
        } catch (Throwable t) {
            w.setResourceBitmap(getResourceAsBitmap(w, w.getNotFoundResourceId()));
        }
    }

    private Bitmap getResourceAsBitmap(ImageWrapper w, int resId) {
        Bitmap b = loaderSettings.getResCacheManager().get("" + resId, w.getWidth(), w.getHeight());
        if (b != null) {
            return b;
        }
        b = loaderSettings.getBitmapUtil().decodeResourceBitmapAndScale(w, resId, loaderSettings.isAllowUpsampling());
        loaderSettings.getResCacheManager().put(String.valueOf(resId), b);
        return b;
    }

    private boolean hasPreviewUrl(String previewUrl) {
        return previewUrl != null;
    }

    private LoaderTask createTask(ImageWrapper imageWrapper) {
        return onImageLoadedListener == null ? new LoaderTask(imageWrapper, loaderSettings) :
                new LoaderTask(imageWrapper, loaderSettings, onImageLoadedListener);
    }

    public void setLoadListener(WeakReference<OnImageLoadedListener> onImageLoadedListener) {
        this.onImageLoadedListener = onImageLoadedListener;
    }

    /**
     * checks whether a previous task is loading the same url
     *
     * @param imageWrapper url of the image to be fetched
     *                     task that might already fetching an image, might be null
     * @return false if there is no other concurrent task running
     */

    private static boolean isTaskAlreadyRunning(ImageWrapper imageWrapper) {
        LoaderTask oldTask = imageWrapper.getLoaderTask();
        if (oldTask == null) {
            return false;
        }

        if ((!imageWrapper.getUrl().equals(oldTask.getUrl()))) {
            return true;
        }
        oldTask.cancel(true);
        return false;
    }
    
    
    protected class TaskRunnable implements Runnable {
    	
    	private LoaderTask task;
    	
    	public TaskRunnable(LoaderTask task) {
    		this.task = task;
    	}

		public void run() {
			task.execute();
		}
    	
    }

}
