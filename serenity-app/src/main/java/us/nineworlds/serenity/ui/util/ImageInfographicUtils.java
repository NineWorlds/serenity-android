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
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.util;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.ui.browser.tv.episodes.EpisodePosterOnItemSelectedListener;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;

/**
 * @author dcarver
 * 
 */
public class ImageInfographicUtils {

	private int width;
	private int height;

	public ImageInfographicUtils(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public ImageView createAudioCodecImage(String codec, Context context) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(width, (Activity)v.getContext());
		int h = ImageUtils.getDPI(height, (Activity)v.getContext());
		v.setLayoutParams(new LayoutParams(w, h));

		if ("aac".equals(codec)) {
			v.setImageResource(R.drawable.aac);
			return v;
		}

		if ("ac3".equals(codec)) {
			v.setImageResource(R.drawable.dolbydigital);
			return v;
		}

		if ("aif".equals(codec)) {
			v.setImageResource(R.drawable.aif);
			return v;
		}

		if ("aifc".equals(codec)) {
			v.setImageResource(R.drawable.aifc);
			return v;
		}

		if ("aiff".equals(codec)) {
			v.setImageResource(R.drawable.aiff);
			return v;
		}

		if ("ape".equals(codec)) {
			v.setImageResource(R.drawable.ape);
			return v;
		}

		if ("avc".equals(codec)) {
			v.setImageResource(R.drawable.avc);
			return v;
		}

		if ("cdda".equals(codec)) {
			v.setImageResource(R.drawable.cdda);
			return v;
		}

		if ("dca".equals(codec)) {
			v.setImageResource(R.drawable.dca);
			return v;
		}

		if ("dts".equals(codec)) {
			v.setImageResource(R.drawable.dts);
			return v;
		}

		if ("eac3".equals(codec)) {
			v.setImageResource(R.drawable.eac3);
			return v;
		}

		if ("flac".equals(codec)) {
			v.setImageResource(R.drawable.flac);
			return v;
		}

		if ("mp1".equals(codec)) {
			v.setImageResource(R.drawable.mp1);
			return v;
		}

		if ("mp2".equals(codec)) {
			v.setImageResource(R.drawable.mp2);
			return v;
		}

		if ("mp3".equals(codec)) {
			v.setImageResource(R.drawable.mp3);
			return v;
		}

		if ("ogg".equals(codec)) {
			v.setImageResource(R.drawable.ogg);
			return v;
		}

		if ("wma".equals(codec)) {
			v.setImageResource(R.drawable.wma);
			return v;
		}

		return null;

	}

	public ImageView createVideoResolutionImage(String res, Context context) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(width, (Activity)v.getContext());
		int h = ImageUtils.getDPI(height, (Activity)v.getContext());
		v.setLayoutParams(new LayoutParams(w, h));
		
		if ("sd".equalsIgnoreCase(res) || "480".equalsIgnoreCase(res)
				|| "540".equalsIgnoreCase(res) || "576".equalsIgnoreCase(res)) {
			v.setImageResource(R.drawable.sd);
			return v;
		}

		if ("720".equalsIgnoreCase(res)) {
			v.setImageResource(R.drawable.res720);
			return v;
		}

		if ("1080".equalsIgnoreCase(res)) {
			v.setImageResource(R.drawable.res1080);
			return v;
		}

		return null;

	}

	public ImageView createAspectRatioImage(String ratio, Context context) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(width, (Activity)v.getContext());
		int h = ImageUtils.getDPI(height, (Activity)v.getContext());
		v.setLayoutParams(new LayoutParams(w, h));

		if ("1.33".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_33);
			return v;
		}

		if ("1.66".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_66);
			return v;
		}

		if ("1.78".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_78);
			return v;
		}

		if ("1.85".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_1_85);
			return v;
		}

		if ("2.20".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_2_20);
			return v;
		}

		if ("2.35".equals(ratio)) {
			v.setImageResource(R.drawable.aspect_2_35);
			return v;
		}

		return null;

	}

	public ImageView createContentRatingImage(String contentRating,
			Context context) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(width, (Activity)v.getContext());
		int h = ImageUtils.getDPI(height, (Activity)v.getContext());
		v.setLayoutParams(new LayoutParams(w, h));

		if ("G".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_general);
			return v;
		}

		if ("PG".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_pg);
			return v;
		}

		if ("PG-13".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_pg13);
			return v;
		}

		if ("R".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_restricted);
			return v;
		}

		if ("NC-17".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_nc17);
			return v;
		}

		v.setImageResource(R.drawable.mpaa_notrated);
		return v;

	}

	public ImageView createVideoCodec(String codec, Context context) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(width, (Activity)v.getContext());
		int h = ImageUtils.getDPI(height, (Activity)v.getContext());
		v.setLayoutParams(new LayoutParams(w, h));

		if ("divx".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.divx);
			return v;
		}

		if ("vc-1".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.vc_1);
			return v;
		}

		if ("h264".equalsIgnoreCase(codec) || "mpeg4".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.h264);
			return v;
		}

		if ("mpeg2".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.mpeg2video);
			return v;
		}

		if ("mpeg1".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.mpeg1video);
			return v;
		}

		if ("xvid".equalsIgnoreCase(codec)) {
			v.setImageResource(R.drawable.xvid);
			return v;
		}

		return null;

	}

	public ImageView createTVContentRating(String contentRating, Context context) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(width, (Activity)v.getContext());
		int h = ImageUtils.getDPI(height, (Activity)v.getContext());
		v.setLayoutParams(new LayoutParams(w, h));

		if ("G".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_general);
			return v;
		}

		if ("PG".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_pg);
			return v;
		}

		if ("PG-13".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_pg13);
			return v;
		}

		if ("R".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_restricted);
			return v;
		}

		if ("NC-17".equals(contentRating)) {
			v.setImageResource(R.drawable.mpaa_nc17);
			return v;
		}

		v.setImageResource(R.drawable.mpaa_notrated);
		return v;
	}

	public ImageView createAudioChannlesImage(String channels, Context context) {
		ImageView v = new ImageView(context);
		v.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(width, (Activity)v.getContext());
		int h = ImageUtils.getDPI(height, (Activity)v.getContext());
		v.setLayoutParams(new LayoutParams(w, h));

		if ("0".equalsIgnoreCase(channels)) {
			v.setImageResource(R.drawable.audio_0);
			return v;
		}

		if ("1".equalsIgnoreCase(channels)) {
			v.setImageResource(R.drawable.audio_1);
			return v;
		}

		if ("2".equalsIgnoreCase(channels)) {
			v.setImageResource(R.drawable.audio_2);
			return v;
		}

		if ("6".equalsIgnoreCase(channels)) {
			v.setImageResource(R.drawable.audio_6);
			return v;
		}

		if ("8".equalsIgnoreCase(channels)) {
			v.setImageResource(R.drawable.audio_8);
			return v;
		}

		return null;
	}

	public static void setWatchedCount(SerenityPosterImageView epiv, Activity a) {
		ImageView watchedView = (ImageView) a
				.findViewById(EpisodePosterOnItemSelectedListener.WATCHED_VIEW_ID);
		watchedView.setImageResource(R.drawable.watched_small);
		int watchedCount = epiv.getPosterInfo().getViewCount();
		epiv.getPosterInfo().setViewCount(watchedCount + 1);
	}

	public static void setUnwatched(SerenityPosterImageView epiv, Activity a) {
		ImageView watchedView = (ImageView) a
				.findViewById(EpisodePosterOnItemSelectedListener.WATCHED_VIEW_ID);
		watchedView.setImageResource(R.drawable.unwatched_small);
		epiv.getPosterInfo().setViewCount(0);
	}

}
