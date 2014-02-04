// Copyright 2013 Google Inc. All Rights Reserved.

package us.nineworlds.serenity.ui.browser.tv;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.jess.ui.TwoWayAdapterView;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Video;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import us.nineworlds.serenity.core.model.SeriesContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo;
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils;

import java.util.List;

public class TVShowGridOnKeyListener implements View.OnKeyListener {
	final PlexappFactory factory = SerenityApplication.getPlexFactory();
	private final Activity activity;

	public TVShowGridOnKeyListener(Activity activity) {
		this.activity = activity;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode != KeyEvent.KEYCODE_MEDIA_PLAY || event.getAction() == KeyEvent.ACTION_DOWN) {
			return false;
		}

		final SeriesContentInfo info = getSelectedSeries(v);
		new FindUnwatchedAsyncTask(activity).execute(info);
		return true;
	}

	/**
	 * @param v
	 * @return
	 */
	protected SeriesContentInfo getSelectedSeries(View v) {
		final TwoWayAdapterView gridView = (TwoWayAdapterView) v;
		final SeriesContentInfo info = (SeriesContentInfo) gridView.getSelectedItem();
		return info;
	}

}
