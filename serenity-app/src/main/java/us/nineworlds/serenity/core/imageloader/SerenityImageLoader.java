/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.core.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import javax.inject.Inject;
import javax.inject.Singleton;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.ApplicationContext;
import us.nineworlds.serenity.injection.BaseInjector;

@Singleton
public class SerenityImageLoader extends BaseInjector {

    @Inject
    @ApplicationContext
    Context context;

    @Inject
    PlexappFactory plexFactory;

    protected DisplayImageOptions defaultImageOptions;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    private static DisplayImageOptions memoryCacheOnly;
    private static DisplayImageOptions movieOptions;
    private static DisplayImageOptions musicOptions;

    public SerenityImageLoader() {
        configure();
    }

    private void configure() {
        configureImageLoader();
    }

    public DisplayImageOptions getDefaultImageOptions() {
        return defaultImageOptions;
    }

    public void displayImage(String imageUrl, ImageView view) {
        displayImage(imageUrl, view, null);
    }

    public void displayImage(String imageUrl, ImageView view,
                             DisplayImageOptions displayImageOptions) {
        displayImage(imageUrl, view, displayImageOptions, null);
    }

    public void displayImage(String imageUrl, ImageView view,
                             DisplayImageOptions displayImageOptions,
                             ImageLoadingListener imageLoaderListener) {
        final ImageViewAware imageViewAware = new ImageViewAware(view);
        imageLoader.displayImage(plexFactory.getImageURL(imageUrl,
                imageViewAware.getWidth(), imageViewAware.getHeight()), view,
                displayImageOptions, imageLoaderListener);
    }

    public void displayImage(String imageUrl, ImageView view, int resId) {
        final ImageViewAware imageViewAware = new ImageViewAware(view);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageForEmptyUri(resId).build();
        imageLoader.displayImage(imageUrl, imageViewAware, options);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public DisplayImageOptions getMovieOptions() {
        return movieOptions;
    }

    public DisplayImageOptions getMusicOptions() {
        return musicOptions;
    }

    public DisplayImageOptions getSycnOptions() {
        return memoryCacheOnly;
    }

    protected void configureImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.default_video_cover).build();

        setStaticImageOptions();

        ImageLoaderConfiguration imageLoaderconfig = new ImageLoaderConfiguration.Builder(
                context).threadPoolSize(5)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .imageDownloader(new OKHttpImageLoader(context))
                .defaultDisplayImageOptions(defaultOptions).build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderconfig);
    }

    private void setStaticImageOptions() {
        musicOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageForEmptyUri(R.drawable.default_music).build();

        movieOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.ARGB_8888)
                .resetViewBeforeLoading(true).build();

        memoryCacheOnly = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(false).bitmapConfig(Bitmap.Config.ARGB_8888)
                .resetViewBeforeLoading(true).build();
    }

}
