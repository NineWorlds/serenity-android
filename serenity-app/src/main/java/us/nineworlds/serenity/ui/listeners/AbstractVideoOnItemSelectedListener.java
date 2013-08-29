/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

package us.nineworlds.serenity.ui.listeners;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.core.services.MovieMetaDataRetrievalIntentService;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityAdapterView.OnItemSelectedListener;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Abstract class for handling video selection information. This can either be a
 * movie or a tv episode. This is primarily used in a detail view browsing
 * scenario.
 * 
 * @author dcarver
 * 
 */
public abstract class AbstractVideoOnItemSelectedListener implements
		OnItemSelectedListener {

	public static final String CRLF = "\r\n";
	public static final int WATCHED_VIEW_ID = 1000;
	public static final double WATCHED_PERCENT = 0.98;
	public static Activity context;
	public Handler subtitleHandler;
	private Animation shrink;
	private Animation fadeIn;
	private View previous;
	private ImageLoader imageLoader;

	public AbstractVideoOnItemSelectedListener(Activity c) {
		context = c;
		shrink = AnimationUtils.loadAnimation(context, R.anim.shrink);
		fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
		imageLoader = SerenityApplication.getImageLoader();

	}

	protected abstract void createVideoDetail(SerenityPosterImageView v);

	protected void createVideoMetaData(SerenityPosterImageView v) {
		SerenityPosterImageView videoView = v;
		VideoContentInfo videoInfo = videoView.getPosterInfo();

		fetchSubtitle(videoInfo);

		TextView ty = (TextView) context.findViewById(R.id.video_year);
		TextView tg = (TextView) context.findViewById(R.id.video_genre);
		TextView tw = (TextView) context.findViewById(R.id.video_writers);
		TextView td = (TextView) context.findViewById(R.id.video_directors);

		ty.setText(context.getString(R.string.unknown));
		tg.setText(context.getString(R.string.unknown));
		tw.setText(context.getString(R.string.unknown));
		td.setText(context.getString(R.string.unknown));

		if (videoInfo.getYear() != null) {
			ty.setText(videoInfo.getYear());
		}

		if (videoInfo.getGenres() != null) {
			StringBuilder details = new StringBuilder();
			for (String genre : videoInfo.getGenres()) {
				details.append(genre);
				details.append(CRLF);
			}
			tg.setText(details.toString());
		}

		if (videoInfo.getWriters() != null) {
			StringBuilder details = new StringBuilder();
			for (String writers : videoInfo.getWriters()) {
				details.append(writers);
				details.append(CRLF);
			}
			tw.setText(details.toString());
		}

		if (videoInfo.getDirectors() != null) {
			StringBuilder details = new StringBuilder();
			for (String directors : videoInfo.getDirectors()) {
				details.append(directors);
				details.append(CRLF);
			}
			td.setText(details.toString());
		}

	}

	/**
	 * Create the images representing info such as sound, ratings, etc based on
	 * the currently selected movie poster.
	 * 
	 * @param position
	 */
	protected void createInfographicDetails(SerenityPosterImageView v) {
		LinearLayout infographicsView = (LinearLayout) context
				.findViewById(R.id.movieInfoGraphicLayout);
		infographicsView.removeAllViews();
		VideoContentInfo mpi = v.getPosterInfo();

		watchedStatus(infographicsView, mpi);

		ImageInfographicUtils imageUtilsNormal = new ImageInfographicUtils(100,
				58);
		ImageInfographicUtils imageUtilsAudioChannel = new ImageInfographicUtils(90,
				58);

		ImageView resv = imageUtilsNormal.createVideoCodec(mpi.getVideoCodec(), v.getContext());
		if (resv != null) {
			infographicsView.addView(resv);
		}
		
		ImageView resolution = imageUtilsNormal.createVideoResolutionImage(mpi.getVideoResolution(), v.getContext());
		if (resolution != null) {
			infographicsView.addView(resolution);
		}
		
		ImageView aspectv = imageUtilsNormal.createAspectRatioImage(
				mpi.getAspectRatio(), context);
		if (aspectv != null) {
			infographicsView.addView(aspectv);
		}
		

		ImageView acv = imageUtilsNormal.createAudioCodecImage(
				mpi.getAudioCodec(), context);
		if (acv != null) {
			infographicsView.addView(acv);
		}

		ImageView achannelsv = imageUtilsAudioChannel.createAudioChannlesImage(
				mpi.getAudioChannels(), v.getContext());
		if (achannelsv != null) {
			infographicsView.addView(achannelsv);
		}

		if (mpi.getRating() != 0) {
			RatingBar ratingBar = new RatingBar(context, null,
					android.R.attr.ratingBarStyleIndicator);
			ratingBar.setMax(4);
			ratingBar.setIsIndicator(true);
			ratingBar.setStepSize(0.1f);
			ratingBar.setNumStars(4);
			ratingBar.setPadding(0, 0, 0, 0);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.rightMargin = 15;
			ratingBar.setLayoutParams(params);
			
			double rating = mpi.getRating();
			ratingBar.setRating((float) (rating / 2.5));
			infographicsView.addView(ratingBar);
		}
		
		ImageView studiov = imageUtilsNormal.createStudioImage(mpi.getStudio(),
				context, mpi.getMediaTagIdentifier());
		if (studiov != null) {
			infographicsView.addView(studiov);
		}
		

	}

	/**
	 * Sets the watched icon to either watched, part watched, or unwatched.
	 * @param infographicsView
	 * @param mpi
	 */
	protected void watchedStatus(LinearLayout infographicsView,
			VideoContentInfo mpi) {
		ImageView viewed = new ImageView(context);
		viewed.setScaleType(ScaleType.FIT_XY);
		LinearLayout.LayoutParams viewedlp = new LinearLayout.LayoutParams(60,
				38);
		viewedlp.setMargins(10, 0, 5, 5);
		viewedlp.gravity = Gravity.CENTER_VERTICAL;
		viewed.setLayoutParams(viewedlp);
		viewed.setId(WATCHED_VIEW_ID);
		
		if (mpi.getViewCount() > 0 && mpi.getDuration() > 0) {
			double percentWatched = mpi.getResumeOffset() / mpi.getDuration();
			if (mpi.getResumeOffset() != 0 && percentWatched < WATCHED_PERCENT) {
			    viewed.setImageResource(R.drawable.partwatched);
			} else {
				viewed.setImageResource(R.drawable.watched_small);
			}
		} else {
			viewed.setImageResource(R.drawable.unwatched_small);
		}

		infographicsView.addView(viewed);
	}

	public void fetchSubtitle(VideoContentInfo mpi) {
		subtitleHandler = new SubtitleHandler(mpi);
		Messenger messenger = new Messenger(subtitleHandler);
		Intent intent = new Intent(context,
				MovieMetaDataRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", mpi.id());
		context.startService(intent);
	}

	@Override
	public void onItemSelected(SerenityAdapterView<?> av, View v, int position,
			long id) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean shouldShrink = preferences.getBoolean(
				"animation_shrink_posters", false);

		if (previous != null) {
			previous.setPadding(0, 0, 0, 0);
			previous.setBackgroundColor(Color.BLACK);
			if (shouldShrink) {
				previous.setAnimation(shrink);
			}
		}

		previous = v;

		v.setBackgroundColor(Color.CYAN);
		v.setPadding(5, 5, 5, 5);
		v.clearAnimation();

		createVideoDetail((SerenityPosterImageView) v);
		createVideoMetaData((SerenityPosterImageView) v);
		createInfographicDetails((SerenityPosterImageView) v);
		changeBackgroundImage(v);

	}

	/**
	 * Change the background image of the activity.
	 * 
	 * @param v
	 */
	public void changeBackgroundImage(View v) {
		SerenityPosterImageView epiv = (SerenityPosterImageView) v;
		VideoContentInfo ei = epiv.getPosterInfo();

		if (ei.getBackgroundURL() == null) {
			return;
		}

		ImageView fanArt = (ImageView) context.findViewById(R.id.fanArt);
		imageLoader.displayImage(ei.getBackgroundURL(), fanArt,
				SerenityApplication.getMovieOptions(), new AnimationImageLoaderListener());

	}

	public static class SubtitleHandler extends Handler {

		private VideoContentInfo video;

		public SubtitleHandler(VideoContentInfo video) {
			this.video = video;
		}

		@Override
		public void handleMessage(Message msg) {
			List<Subtitle> subtitles = (List<Subtitle>) msg.obj;
			if (subtitles == null || subtitles.isEmpty()) {
				return;
			}

			TextView subtitleText = (TextView) context
					.findViewById(R.id.subtitleFilter);
			subtitleText.setVisibility(View.VISIBLE);
			Spinner subtitleSpinner = (Spinner) context
					.findViewById(R.id.videoSubtitle);
			View metaData = context.findViewById(R.id.metaDataRow);
			if (metaData.getVisibility() == View.GONE || metaData.getVisibility() == View.INVISIBLE) {
				metaData.setVisibility(View.VISIBLE);
			}

			ArrayList<Subtitle> spinnerSubtitles = new ArrayList<Subtitle>();
			Subtitle noSubtitle = new Subtitle();
			noSubtitle.setDescription("None");
			noSubtitle.setFormat("none");
			noSubtitle.setKey(null);

			spinnerSubtitles.add(noSubtitle);
			spinnerSubtitles.addAll(subtitles);

			ArrayAdapter<Subtitle> subtitleAdapter = new ArrayAdapter<Subtitle>(
					context, R.layout.serenity_spinner_textview,
					spinnerSubtitles);
			subtitleAdapter
					.setDropDownViewResource(R.layout.serenity_spinner_textview_dropdown);
			subtitleSpinner.setAdapter(subtitleAdapter);
			subtitleSpinner
					.setOnItemSelectedListener(new SubtitleSpinnerOnItemSelectedListener(
							video, context));
			subtitleSpinner.setVisibility(View.VISIBLE);
		}

	}
	
	private class AnimationImageLoaderListener extends SimpleImageLoadingListener {
		
		/* (non-Javadoc)
		 * @see com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener#onLoadingComplete(java.lang.String, android.view.View, android.graphics.Bitmap)
		 */
		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(context);
			boolean shouldFadeIn = preferences.getBoolean(
					"animation_background_fadein", false);
			if (shouldFadeIn) {
				view.startAnimation(fadeIn);
			}
		}
	}

}
