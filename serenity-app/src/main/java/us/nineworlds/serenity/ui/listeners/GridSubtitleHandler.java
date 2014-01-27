package us.nineworlds.serenity.ui.listeners;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.core.model.impl.SubtitleMediaContainer;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class GridSubtitleHandler extends SubtitleHandler {

	private View view;
	
	/**
	 * @param video
	 * @param c
	 */
	public GridSubtitleHandler(VideoContentInfo video, Activity c, View v) {
		super(video, c);
		view = v;
	}
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.ui.listeners.SubtitleHandler#onResponse(us.nineworlds.plex.rest.model.impl.MediaContainer)
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
		
		infoGraphicInd.setVisibility(View.VISIBLE);
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
