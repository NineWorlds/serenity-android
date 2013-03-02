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

package us.nineworlds.serenity.ui.browser.movie;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.services.UnWatchEpisodeAsyncTask;
import us.nineworlds.serenity.core.services.WatchedEpisodeAsyncTask;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.ui.views.SerenityPosterImageView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ListView;

/**
 * @author dcarver
 * 
 */
public class MoviePosterOnItemLongClickListener implements
		OnItemLongClickListener {

	private Dialog dialog;
	private Activity context;
	private VideoContentInfo info;
	private SerenityPosterImageView mpiv;
	
	public boolean onItemLongClick(AdapterView<?> av, View v, int position,
			long arg3) {
		
		// Google TV is sending back different results than Nexus 7
		// So we try to handle the different results.
		
		if (v == null) {
			Gallery g = (Gallery) av;
			mpiv = (SerenityPosterImageView) g.getSelectedView();
		} else {
			if (v instanceof SerenityPosterImageView) {
				mpiv = (SerenityPosterImageView) v;
			} else {
				Gallery g = (Gallery) v;
				mpiv = (SerenityPosterImageView) g.getSelectedView();
			}
		}
	
		info = mpiv.getPosterInfo();
		context = (Activity) mpiv.getContext();

		dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Dialog));
		builder.setTitle("Video Options");

		ListView modeList = new ListView(context);
		ArrayList<String> options = new ArrayList<String>();
		options.add("Toggle Watched Status");
		if (!SerenityApplication.isGoogleTV(context)) {
			options.add("Play on TV");
		}

		ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				options);
		
		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(new DialogOnItemSelected());

		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();

		return true;
	}

	public boolean hasAbleRemote(Context context) {

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> pkgAppsList = context.getPackageManager()
				.queryIntentActivities(mainIntent, 0);

		for (ResolveInfo resolveInfo : pkgAppsList) {
			String packageName = resolveInfo.activityInfo.packageName;
			if (packageName.contains("entertailion.android.remote")) {
				return true;
			}
		}

		return false;
	}

	protected class DialogOnItemSelected implements OnItemClickListener {


		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
		 */
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long arg3) {
			if (position == 0) {
				if (info.getViewCount() > 0) {
					new UnWatchEpisodeAsyncTask().execute(info.id());
					ImageInfographicUtils.setUnwatched(mpiv, context);
				} else {
					new WatchedEpisodeAsyncTask().execute(info.id());
					ImageInfographicUtils.setWatchedCount(mpiv, context);
				}
			} else if (hasAbleRemote(context)) {
				Intent sharingIntent = new Intent();
				sharingIntent.setClassName("com.entertailion.android.remote",
						"com.entertailion.android.remote.MainActivity");
				sharingIntent.setAction("android.intent.action.SEND");
//				String intentString = "intent:#Intent;" +
//				"action=android.intent.action.VIEW;" +
//				"category=android.intent.category.DEFAULT" +
//				"component=us.nineworlds.serenity/.ui.video.player.SerenitySurfaceViewVideoActivity;" +
//				"S.encodedvideoUrl=" + URLEncoder.encode(mpiv.getPosterInfo().getDirectPlayUrl()) + ";" +
//				"S.title=" + URLEncoder.encode(mpiv.getPosterInfo().getTitle()) + ";" +
//				"S.resumeOffset=" + URLEncoder.encode(Integer.toString(mpiv.getPosterInfo().getResumeOffset())) + ";" +
//				"S.posterUrl=" + URLEncoder.encode(mpiv.getPosterInfo().getPosterURL()) + ";" +
//				"S.aspectRatio=" + URLEncoder.encode(mpiv.getPosterInfo().getAspectRatio()) + ";" +
//				"S.videoFormat=" + URLEncoder.encode(mpiv.getPosterInfo().getVideoCodec()) + ";" +
//				"S.videoResolution=" + URLEncoder.encode(mpiv.getPosterInfo().getVideoResolution()) + ";" +
//				"S.audioFormat=" + URLEncoder.encode(mpiv.getPosterInfo().getAudioCodec()) + ";" +
//				"S.audioChannels=" + URLEncoder.encode(mpiv.getPosterInfo().getAudioChannels()) + ";" +
//				"end";
				sharingIntent.putExtra(Intent.EXTRA_TEXT, mpiv.getPosterInfo().getDirectPlayUrl());
//				sharingIntent.putExtra(Intent.EXTRA_TEXT, intentString);
				
				context.startActivity(sharingIntent);
			}
			
			dialog.dismiss();
		}

	}

}
