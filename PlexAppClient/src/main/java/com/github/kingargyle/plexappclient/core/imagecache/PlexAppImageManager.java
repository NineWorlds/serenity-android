package com.github.kingargyle.plexappclient.core.imagecache;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.novoda.imageloader.core.LoaderContext;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.OnImageLoadedListener;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.cache.SoftMapCache;
import com.novoda.imageloader.core.exception.ImageNotFoundException;
import com.novoda.imageloader.core.file.BasicFileManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.loader.ConcurrentLoader;
import com.novoda.imageloader.core.loader.Loader;
import com.novoda.imageloader.core.loader.SimpleLoader;
import com.novoda.imageloader.core.network.NetworkManager;
import com.novoda.imageloader.core.network.UrlNetworkManager;

import java.io.File;

/**
 * ImageManager has the responsibility to provide a
 * simple and easy interface to access three fundamental part of the imageLoader
 * library : the FileManager, the NetworkManager, and the CacheManager.
 * An ImageManager instance can be instantiated at the application level and used
 * statically across the application.
 * <p/>
 * Manifest.permission.WRITE_EXTERNAL_STORAGE and Manifest.permission.INTERNET are
 * currently necessary for the imageLoader library to work properly.
 */
public class PlexAppImageManager {

    private LoaderContext loaderContext;
    private Loader loader;
    private CacheManager cacheManager;

    public PlexAppImageManager(Context context, LoaderSettings settings) {
        this.loaderContext = new LoaderContext();
        loaderContext.setSettings(settings);
        loaderContext.setFileManager(new BasicFileManager(settings));
        loaderContext.setNetworkManager(new UrlNetworkManager(settings));
        loaderContext.setResBitmapCache(new SoftMapCache());
        cacheManager = settings.getCacheManager();
        if (cacheManager == null) {
            cacheManager = new SoftMapCache();
        }
        loaderContext.setCache(cacheManager);
        setLoader(settings);
        verifyPermissions(context);
    }

    /**
     * Constructor for advanced use. The loaderContext has to be setup correctly.
     *
     * @param context       context where this image manager is used
     * @param loaderContext pre-configured loader context
     */
    public PlexAppImageManager(Context context, LoaderContext loaderContext) {
        assert (loaderContext.getSettings() != null);
        LoaderSettings settings = loaderContext.getSettings();
        if (loaderContext.getFileManager() == null) {
            loaderContext.setFileManager(new BasicFileManager(settings));
        }
        if (loaderContext.getNetworkManager() == null) {
            loaderContext.setNetworkManager(new UrlNetworkManager(settings));
        }
        cacheManager = settings.getCacheManager();
        if (cacheManager == null) {
            cacheManager = new SoftMapCache();
        }
        loaderContext.setCache(cacheManager);

        this.loaderContext = loaderContext;

        setLoader(settings);
        verifyPermissions(context);

    }

    public Loader getLoader() {
        return loader;
    }

    public FileManager getFileManager() {
        return loaderContext.getFileManager();
    }

    public NetworkManager getNetworkManager() {
        return loaderContext.getNetworkManager();
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    protected void setLoader(LoaderSettings settings) {
        if (settings.isUseAsyncTasks()) {
            this.loader = new ConcurrentLoader(loaderContext);
        } else {
            this.loader = new SimpleLoader(loaderContext);
        }
    }

    private void verifyPermissions(Context context) {
        verifyPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        verifyPermission(context, Manifest.permission.INTERNET);
    }

    private void verifyPermission(Context c, String permission) {
        int p = c.getPackageManager().checkPermission(permission, c.getPackageName());
        if (p == PackageManager.PERMISSION_DENIED) {
            throw new RuntimeException("ImageLoader : please add the permission " + permission + " to the manifest");
        }
    }

    public void setOnImageLoadedListener(OnImageLoadedListener listener) {
        loaderContext.setListener(listener);
    }

    public void unRegisterOnImageLoadedListener(OnImageLoadedListener listener) {
        loaderContext.removeOnImageLoadedListener(listener.hashCode());
    }

    /**
     * Loads an image into the cache, it does not bind the image to any view.
     * This method can be used for pre-fetching images.
     * If the image is already cached, the image is not fetched from the net.
     *
     * <p/>
     * This method runs in the same thread as the caller method.
     * Hence, make sure that this method is not called from the main thread.
     *
     * If the image could be retrieved and decoded the resulting bitmap is cached.
     *
     * @param url Url of image to be pre-fetched
     * @width size of the cached image
     * @height size of the cached image
     */
    public Bitmap cacheImage(String url, int width, int height) {
        Bitmap bm = loaderContext.getCache().get(url, width, height);
        if (bm == null) {

            try {
                File imageFile = loaderContext.getFileManager().getFile(url, width, height);
                if (!imageFile.exists()) {
                    loaderContext.getNetworkManager().retrieveImage(url, imageFile);
                }
                Bitmap b;
                if (loaderContext.getSettings().isAlwaysUseOriginalSize()) {
                    b = loaderContext.getBitmapUtil().decodeFile(imageFile, width, height);
                } else {
                    b = loaderContext.getBitmapUtil().decodeFileAndScale(imageFile, width, height, loaderContext.getSettings().isAllowUpsampling());
                }

                if (b == null) {
                  // decode failed
                  loaderContext.getCache().put(url, b);
                }
                
                return b;


            } catch (ImageNotFoundException inf) {
                inf.printStackTrace();
            }

        }
        return bm;
    }
    
    /**
     * Retrieve an image from the cache based on the URL
     * @return
     */
    public Bitmap getImage(String url, int width, int height) {
    	return cacheImage(url, width, height);
    }

}