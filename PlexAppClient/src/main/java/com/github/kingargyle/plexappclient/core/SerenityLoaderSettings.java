package com.github.kingargyle.plexappclient.core;

import android.content.Context;
import android.os.Build;

import com.novoda.imageloader.core.bitmap.BitmapUtil;
import com.novoda.imageloader.core.cache.CacheManager;
import com.novoda.imageloader.core.cache.SoftMapCache;
import com.novoda.imageloader.core.file.BasicFileManager;
import com.novoda.imageloader.core.file.FileManager;
import com.novoda.imageloader.core.file.util.AndroidFileContext;
import com.novoda.imageloader.core.file.util.FileUtil;
import com.novoda.imageloader.core.loader.ConcurrentLoader;
import com.novoda.imageloader.core.loader.Loader;
import com.novoda.imageloader.core.loader.SimpleLoader;
import com.novoda.imageloader.core.network.NetworkManager;
import com.novoda.imageloader.core.network.UrlNetworkManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * LoaderSettings is the main class used to customize the behavior of the imageLoader.
 * To provide a more user friendly way to set different parameters it is possible to use
 * a builder : SettingsBuilder. 
 */
public class SerenityLoaderSettings extends com.novoda.imageloader.core.LoaderSettings {

    private static final long DEFAULT_EXPIRATION_PERIOD = 7l * 24l * 3600l * 1000l;
    private static final boolean DEFAULT_INCLUDE_QUERY_IN_HASH = true;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 10 * 1000;
    private static final int DEFAULT_READ_TIMEOUT = 10 * 1000;
    private static final boolean DEFAULT_DISCONNECT_ON_EVERY_CALL = false;
    private static final boolean DEFAULT_USE_ASYNC_TASKS = true;
    private static final boolean DEFAULT_ALLOW_UPSAMPLING = false;
    private static final boolean DEFAULT_ALWAYS_USE_ORIGINAL_SIZE = false;

    private final BitmapUtil bitmapUtil = new BitmapUtil();

    private CacheManager cacheManager;
    private CacheManager resCacheManager;
    private FileManager fileManager;
    private NetworkManager networkManager;
    private Loader loader;

    private File cacheDir;
    private int connectionTimeout;
    private int readTimeout;
    private final Map<String, String> headers = new HashMap<String, String>();
    private long expirationPeriod;
    private boolean isQueryIncludedInHash;
    private boolean disconnectOnEveryCall;
    private String sdkVersion;
    private boolean useAsyncTasks;
    private boolean allowUpsampling;
    private boolean alwaysUseOriginalSize;

    /**
     * Constructor with all settings set to default values
     */
    public SerenityLoaderSettings() {
        this.setExpirationPeriod(DEFAULT_EXPIRATION_PERIOD);
        this.setQueryIncludedInHash(DEFAULT_INCLUDE_QUERY_IN_HASH);
        this.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        this.setReadTimeout(DEFAULT_READ_TIMEOUT);
        this.setDisconnectOnEveryCall(DEFAULT_DISCONNECT_ON_EVERY_CALL);
        this.setUseAsyncTasks(DEFAULT_USE_ASYNC_TASKS);
        this.setAllowUpsampling(DEFAULT_ALLOW_UPSAMPLING);
        this.setAlwaysUseOriginalSize(DEFAULT_ALWAYS_USE_ORIGINAL_SIZE);
    }

    @Override
	public BitmapUtil getBitmapUtil() {
        return bitmapUtil;
    }

    @Override
	public File getCacheDir() {
        return cacheDir;
    }

    @Override
	public void setCacheDir(File cacheDir) {
        this.cacheDir = cacheDir;
    }

    /**
     * Time period in millis how long cached images should be kept in the file storage. 
     * @return
     */
    @Override
	public long getExpirationPeriod() {
        return expirationPeriod;
    }

    @Override
	public void setExpirationPeriod(long expirationPeriod) {
        this.expirationPeriod = expirationPeriod;
    }

    /**
     * Flag indicating whether queries of image urls should be used as part of the cache key.
     * If set to false the cache returns the same image e.g. 
     * for <code>http://king.com/img.png?v=1</code> and <code>http://king.com/img.png?v=2</code>
     *  
     * @return true if urls with different queries refer to different images.
     */
    @Override
	public boolean isQueryIncludedInHash() {
        return isQueryIncludedInHash;
    }

    @Override
	public void setQueryIncludedInHash(boolean isQueryIncludedInHash) {
        this.isQueryIncludedInHash = isQueryIncludedInHash;
    }

    @Override
	public int getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
	public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
	public int getReadTimeout() {
        return readTimeout;
    }

    @Override
	public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    
    @Override
	public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
	public void addHeader(String key, String value) {
        try {
            headers.put(key, URLEncoder.encode(value, "UTF8"));
        } catch (UnsupportedEncodingException e) {
        }
    }

    @Override
	public boolean getDisconnectOnEveryCall() {
        return disconnectOnEveryCall;
    }

    @Override
	public void setDisconnectOnEveryCall(boolean disconnectOnEveryCall) {
        this.disconnectOnEveryCall = disconnectOnEveryCall;
    }

    @Override
	public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    @Override
	public String getSdkVersion() {
        return this.sdkVersion;
    }

    @Override
	public CacheManager getCacheManager() {
        if (cacheManager == null) {
            cacheManager = new SoftMapCache();
        }
        return cacheManager;
    }

    @Override
	public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
	public CacheManager getResCacheManager() {
        if (resCacheManager == null) {
            resCacheManager = new SoftMapCache();
        }
        return resCacheManager;
    }

    @Override
	public void setResCacheManager(CacheManager resCacheManager) {
        this.resCacheManager = resCacheManager;
    }

    @Override
	public NetworkManager getNetworkManager() {
        if (networkManager == null) {
            networkManager = new UrlNetworkManager(this);
        }
        return networkManager;
    }

    @Override
	public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
	public FileManager getFileManager() {
        if (fileManager == null) {
            fileManager = new BasicFileManager(this);
        }
        return fileManager;
    }

    @Override
	public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
	public boolean isUseAsyncTasks() {
        return useAsyncTasks;
    }

    @Override
	public void setUseAsyncTasks(boolean useAsyncTasks) {
        this.useAsyncTasks = useAsyncTasks;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
	public Loader getLoader() {
        if (loader == null) {
            if (isUseAsyncTasks()) {
                this.loader = new ConcurrentLoader(this);
            } else {
                this.loader = new SimpleLoader(this);
            }
        }
        return loader;
    }

    @Override
	public boolean isCleanOnSetup() {
        return true;
    }       

    /**
     * Flag to enable upsampling for small images.
     * If true and the image is smaller than the requested size the image is resized to a larger image.  
     * Default is false.
     * 
     * @return true if 
     */
    @Override
	public boolean isAllowUpsampling() {
		return allowUpsampling;
	}

	@Override
	public void setAllowUpsampling(boolean allowUpsampling) {
		this.allowUpsampling = allowUpsampling;
	}

	/**
	 * Flag to disable image resizing.
	 * Set this flag to true if you want to avoid bitmap resizing
	 * Default is false.
	 * 
	 * @return true if images are always cached in the original size
	 */
	@Override
	public boolean isAlwaysUseOriginalSize() {
		return alwaysUseOriginalSize;
	}

	@Override
	public void setAlwaysUseOriginalSize(boolean alwaysUseOriginalSize) {
		this.alwaysUseOriginalSize = alwaysUseOriginalSize;
	}

	/**
     * Builder for the LoaderSettings.
     */
    public static class SettingsBuilder {

        private SerenityLoaderSettings settings;

        public SettingsBuilder() {
            settings = new SerenityLoaderSettings();
        }

        /**
         * Change setting of time period before cached images are removed from file storage.
         *  
         * @param timePeriodInMillis time period in milli seconds
         * @return this SettingsBuilder
         */
        public SettingsBuilder withExpirationPeriod(long timePeriodInMillis) {
        	settings.setExpirationPeriod(timePeriodInMillis);
        	return this;
        }
        
        /**
         * Change flag indicating whether queries of image urls should be used as part of the cache key. 
         * If set to false the cache returns the same image e.g. for <code>http://king.com/img.png?v=1</code> and <code>http://king.com/img.png?v=2</code>
         * 
         * @param enableQueryInHashGeneration set to false if querys in urls should be ignored. 
         * @return this SettingsBuilder.
         */
        public SettingsBuilder withEnableQueryInHashGeneration(boolean enableQueryInHashGeneration) {
            settings.setQueryIncludedInHash(enableQueryInHashGeneration);
            return this;
        }

        public SettingsBuilder withConnectionTimeout(int connectionTimeout) {
            settings.setConnectionTimeout(connectionTimeout);
            return this;
        }

        public SettingsBuilder withReadTimeout(int readTimeout) {
            settings.setReadTimeout(readTimeout);
            return this;
        }
        
        public SettingsBuilder addHeader(String key, String value) {
        	settings.addHeader(key, value);
            return this;
        }

        public SettingsBuilder withDisconnectOnEveryCall(boolean disconnectOnEveryCall) {
            settings.setDisconnectOnEveryCall(disconnectOnEveryCall);
            return this;
        }

        public SettingsBuilder withCacheManager(CacheManager cacheManager) {
            settings.setCacheManager(cacheManager);
            return this;
        }

        public SettingsBuilder withResCacheManager(CacheManager resCacheManager) {
            settings.setResCacheManager(resCacheManager);
            return this;
        }

        public SettingsBuilder withAsyncTasks(boolean useAsyncTasks) {
            settings.setUseAsyncTasks(useAsyncTasks);
            return this;
        }
        
        public SettingsBuilder withCacheDir(File file) {
            settings.setCacheDir(file);
            return this;
        }
        
        /**
         * Changes flag to enable upsampling for small images. 
         * If true and the image is smaller than the requested size 
         * the image is resized to a larger image. Default is false.
         * 
         * @param allowUpsampling set to true if you want to enlarge small images
         * @return this SettingsBuilder
         */
        public SettingsBuilder withUpsampling(boolean allowUpsampling){
        	settings.setAllowUpsampling(allowUpsampling);
        	return this;
        }
        
        /**
         * Changes flag to disable image resizing. 
         * Set the flag to true if you want to avoid bitmap resizing. Default is false.
         * 
         * @param alwaysUseOriginalSize set to true if you want to avoid bitmap resizing 
         * @return this SettingsBuilder
         */
        public SettingsBuilder withoutResizing(boolean alwaysUseOriginalSize ){
        	settings.setAlwaysUseOriginalSize(alwaysUseOriginalSize);
        	return this;
        }

        public SettingsBuilder withFileManager(FileManager fileManager) {
            settings.setFileManager(fileManager);
            return this;
        }

        public SettingsBuilder withNetworkManager(NetworkManager networkManager) {
            settings.setNetworkManager(networkManager);
            return this;
        }

        public SettingsBuilder withLoader(Loader loader) {
            settings.setLoader(loader);
            return this;
        }

        public SerenityLoaderSettings build(Context context) {
            File dir = new FileUtil().prepareCacheDirectory(new AndroidFileContext(context));
            settings.setCacheDir(dir);
            settings.setSdkVersion(Build.VERSION.SDK);
            return settings;
        }

    }

}
