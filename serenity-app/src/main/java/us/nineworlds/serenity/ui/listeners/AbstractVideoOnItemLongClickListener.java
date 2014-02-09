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

package us.nineworlds.serenity.ui.listeners;

import java.util.ArrayList;
import java.util.List;

import com.castillo.dd.DSInterface;
import com.castillo.dd.PendingDownload;
import com.google.analytics.tracking.android.Log;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.menus.DialogMenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.ui.dialogs.DirectoryChooserDialog;
import us.nineworlds.serenity.ui.util.ImageInfographicUtils;
import us.nineworlds.serenity.widgets.SerenityAdapterView;
import us.nineworlds.serenity.widgets.SerenityGallery;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A listener that handles long press for video content. Includes displaying a
 * dialog for toggling watched and unwatched status as well for possibly playing
 * on the TV.
 * 
 * @author dcarver
 * 
 */
public class AbstractVideoOnItemLongClickListener {

	protected Dialog dialog;
	protected Activity context;
	protected VideoContentInfo info;
	protected ImageView vciv;

	public boolean onItemLongClick(SerenityAdapterView<?> av, View v,
			int position, long arg3) {

		// Google TV is sending back different results than Nexus 7
		// So we try to handle the different results.

		info = (VideoContentInfo) av.getSelectedItem();

		if (v == null) {
			SerenityGallery g = (SerenityGallery) av;
			vciv = (ImageView) g.getSelectedView().findViewById(
					R.id.posterImageView);
			info = (VideoContentInfo) g.getSelectedItem();
		} else {
			if (v instanceof ImageView) {
				vciv = (ImageView) v;
			} else {
				SerenityGallery g = (SerenityGallery) v;
				vciv = (ImageView) g.getSelectedView().findViewById(
						R.id.posterImageView);
				info = (VideoContentInfo) g.getSelectedItem();
			}
		}

		return onItemLongClick();
	}

	/**
	 * @return
	 */
	protected boolean onItemLongClick() {
		context = (Activity) vciv.getContext();

		dialog = new Dialog(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(
				new ContextThemeWrapper(context,
						android.R.style.Theme_Holo_Dialog));
		builder.setTitle(context.getString(R.string.video_options));

		ListView modeList = new ListView(context);
		modeList.setSelector(R.drawable.menu_item_selector);
		ArrayList<DialogMenuItem> options = addMenuOptions();

		ArrayAdapter<DialogMenuItem> modeAdapter = new ArrayAdapter<DialogMenuItem>(
				context, R.layout.simple_list_item,
				R.id.list_item_text, options);

		modeList.setAdapter(modeAdapter);
		modeList.setOnItemClickListener(getDialogSelectedListener());

		builder.setView(modeList);
		dialog = builder.create();
		dialog.show();

		return true;
	}

	/**
	 * @return
	 */
	protected OnItemClickListener getDialogSelectedListener() {
		return new DialogOnItemSelected();
	}

	/**
	 * @return
	 */
	protected ArrayList<DialogMenuItem> addMenuOptions() {
		ArrayList<DialogMenuItem> options = new ArrayList<DialogMenuItem>();
		options.add(createMenuItemToggleWatchStatus());
		options.add(createMenuItemDownload());
		options.add(createMenuItemAddToQueue());
		
		if (info.hasTrailer()
				&& YouTubeInitializationResult.SUCCESS
						.equals(YouTubeApiServiceUtil
								.isYouTubeApiServiceAvailable(context))) {
			options.add(createMenuItemPlayTrailer());
		}
		
		if (!SerenityApplication.isGoogleTV(context) && hasSupportedCaster()) {
			options.add(createMenuItemFling());
		}
		return options;
	}

	/**
	 * @return
	 */
	protected DialogMenuItem createMenuItemToggleWatchStatus() {
		return createMenuItem(
				context.getString(R.string.toggle_watched_status), 0);
	}

	protected DialogMenuItem createMenuItemDownload() {
		return createMenuItem(
				context.getString(R.string.download_video_to_device), 1);
	}

	protected DialogMenuItem createMenuItemAddToQueue() {
		return createMenuItem(context.getString(R.string.add_video_to_queue), 2);
	}

	protected DialogMenuItem createMenuItemPlayTrailer() {
		return createMenuItem("Play Trailer", 3);
	}

	protected DialogMenuItem createMenuItemFling() {
		return createMenuItem(context.getString(R.string.cast_fling_with_), 4);
	}

	protected DialogMenuItem createMenuItem(String title, int action) {
		DialogMenuItem menuItem = new DialogMenuItem();
		menuItem.setTitle(title);
		menuItem.setMenuDialogAction(action);
		return menuItem;
	}

	/**
	 * @return
	 */
	protected boolean hasSupportedCaster() {
		return hasAbleRemote(context) || hasGoogleTVRemote(context)
				|| hasAllCast(context);
	}

	protected boolean hasAbleRemote(Context context) {
		return hasRemoteByName(context, "com.entertailion.android.remote");
	}

	protected boolean hasGoogleTVRemote(Context context) {
		return hasRemoteByName(context, "com.google.android.apps.tvremote");
	}

	protected boolean hasAllCast(Context context) {
		return hasRemoteByName(context, "com.koushikdutta.cast");
	}

	protected boolean hasRemoteByName(Context context, String remotePackageName) {

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> pkgAppsList = context.getPackageManager()
				.queryIntentActivities(mainIntent, 0);

		for (ResolveInfo resolveInfo : pkgAppsList) {
			String packageName = resolveInfo.activityInfo.packageName;
			if (packageName.contains(remotePackageName)) {
				return true;
			}
		}

		return false;
	}

	protected void performWatchedToggle() {
		View posterLayout = (View) vciv.getParent();
		posterLayout.findViewById(R.id.posterInprogressIndicator)
				.setVisibility(View.INVISIBLE);

		toggleGraphicIndicators(posterLayout);
		info.toggleWatchStatus();
	}

	/**
	 * @param posterLayout
	 */
	protected void toggleGraphicIndicators(View posterLayout) {
		if (info.isPartiallyWatched() || info.isUnwatched()) {
			final float percentWatched = info.viewedPercentage();
			if (percentWatched <= 0.90f) {
				ImageInfographicUtils.setWatchedCount(vciv, context, info);
				ImageView view = (ImageView) posterLayout
						.findViewById(R.id.posterWatchedIndicator);
				view.setImageResource(R.drawable.overlaywatched);
				view.setVisibility(View.VISIBLE);
				return;
			}
		}

		ImageInfographicUtils.setUnwatched(vciv, context, info);
		posterLayout.findViewById(R.id.posterWatchedIndicator).setVisibility(
				View.INVISIBLE);
	}

	protected void performPlayTrailer() {
		if (info.hasTrailer()) {
			if (YouTubeInitializationResult.SUCCESS
					.equals(YouTubeApiServiceUtil
							.isYouTubeApiServiceAvailable(context))) {
				// Intent intent =
				// YouTubeStandalonePlayer.createVideoIntent(context,
				// SerenityConstants.YOUTUBE_BROWSER_API_KEY, info.trailerId(),
				// 0, true, true);
				// context.startActivity(intent);
				Intent youTubei = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://www.youtube.com/watch?v="
								+ info.trailerId()));
				context.startActivity(youTubei);
				return;
			}
			Toast.makeText(context, "YouTube Player not installed",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(context, "No Trailers found for this video.",
					Toast.LENGTH_LONG).show();
		}
	}

	protected void performGoogleTVSecondScreen() {
		if (hasAbleRemote(context) || hasGoogleTVRemote(context)
				|| hasAllCast(context)) {
			dialog.dismiss();

			final String body = info.getDirectPlayUrl();

			final SenderAppAdapter adapter = new SenderAppAdapter(context);

			new AlertDialog.Builder(context)
					.setTitle(R.string.cast_fling_with_)
					.setCancelable(true)
					.setSingleChoiceItems(adapter, -1,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									adapter.respondToClick(which, "", body);

									dialog.dismiss();

								}
							}).show();
		}
	}

	protected void performAddToQueue() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean extplayer = prefs.getBoolean("external_player", false);
		boolean extplayerVideoQueue = prefs.getBoolean(
				"external_player_continuous_playback", false);
		if (extplayer && !extplayerVideoQueue) {
			Toast.makeText(
					context,
					R.string.external_player_video_queue_support_has_not_been_enabled_,
					Toast.LENGTH_LONG).show();
			return;
		}

		SerenityApplication.getVideoPlaybackQueue().add(info);
	}

	protected class DialogOnItemSelected implements OnItemClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemClick(android.widget.AdapterView<?> arg0, View v,
				int position, long arg3) {

			DialogMenuItem menuItem = (DialogMenuItem) arg0
					.getItemAtPosition(position);

			switch (menuItem.getMenuDialogAction()) {
			case 0:
				performWatchedToggle();
				break;
			case 1:
				startDownload();
				break;
			case 2:
				performAddToQueue();
				break;
			case 3:
				performPlayTrailer();
				break;
			case 4:
				performGoogleTVSecondScreen();
			}
			v.requestFocusFromTouch();
			dialog.dismiss();
		}

	}

	protected void startDownload() {
		directoryChooser();
	}

	protected void startDownload(String destination) {

		List<PendingDownload> pendingDownloads = SerenityApplication
				.getPendingDownloads();
		PendingDownload pendingDownload = new PendingDownload();
		String filename = info.getTitle() + "." + info.getContainer();
		pendingDownload.setFilename(filename);
		pendingDownload.setUrl(info.getDirectPlayUrl());

		pendingDownloads.add(pendingDownload);
		int pos = pendingDownloads.size() - 1;

		try {
			DSInterface downloadService = MainActivity.getDsInterface();
			downloadService.addFileDownloadlist(info.getDirectPlayUrl(),
					destination, filename, pos);
			Toast.makeText(
					context,
					context.getString(R.string.starting_download_of_)
							+ info.getTitle(), Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Log.e("Unable to download " + info.getTitle() + "."
					+ info.getContainer());
		}
	}

	protected void directoryChooser() {
		// Create DirectoryChooserDialog and register a callback
		DirectoryChooserDialog directoryChooserDialog = new DirectoryChooserDialog(
				context, new DirectoryChooserDialog.ChosenDirectoryListener() {
					@Override
					public void onChosenDir(String chosenDir) {
						Toast.makeText(
								context,
								context.getString(R.string.chosen_directory_)
										+ chosenDir, Toast.LENGTH_LONG).show();
						startDownload(chosenDir);
					}
				});
		directoryChooserDialog.setNewFolderEnabled(true);
		directoryChooserDialog.chooseDirectory("");
	}
	
	

}
