package us.nineworlds.serenity.volley;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.core.model.impl.SubtitleMediaContainer;
import android.app.Activity;
import android.view.View;

public class GridSubtitleVolleyResponseListener extends
		SubtitleVolleyResponseListener {

	private final View view;

	/**
	 * @param video
	 * @param c
	 */
	public GridSubtitleVolleyResponseListener(VideoContentInfo video,
			Activity c, View v) {
		super(video, c);
		view = v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * us.nineworlds.serenity.ui.listeners.SubtitleHandler#onResponse(us.nineworlds
	 * .plex.rest.model.impl.MediaContainer)
	 */
	@Override
	public void onResponse(MediaContainer response) {
		SubtitleMediaContainer subtitleMC = new SubtitleMediaContainer(response);
		List<Subtitle> subtitles = subtitleMC.createSubtitle();
		processSubtitles(subtitles);
	}

	/**
	 * @param subtitles
	 */
	public void processSubtitles(List<Subtitle> subtitles) {
		View subtitleInd = view.findViewById(R.id.subtitleIndicator);
		View infoGraphicInd = view.findViewById(R.id.infoGraphicMeta);
		if (subtitles == null || subtitles.isEmpty()) {
			subtitleInd.setVisibility(View.GONE);
			return;
		}

		if (infoGraphicInd != null) {
			infoGraphicInd.setVisibility(View.VISIBLE);
		}

		subtitleInd.setVisibility(View.VISIBLE);

		ArrayList<Subtitle> availableSubtitles = new ArrayList<Subtitle>();
		Subtitle noSubtitle = new Subtitle();
		noSubtitle.setDescription("None");
		noSubtitle.setFormat("none");
		noSubtitle.setKey(null);
		availableSubtitles.add(noSubtitle);
		availableSubtitles.addAll(subtitles);
		video.setAvailableSubTitles(availableSubtitles);
	}

}
