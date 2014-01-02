package us.nineworlds.serenity.ui.listeners;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class GridSubtitleHandler extends Handler {

	private VideoContentInfo video;
	private Activity context;
	private View view;

	public GridSubtitleHandler(VideoContentInfo video, Activity c, View v) {
		this.video = video;
		context = c;
		view = v;
	}

	@Override
	public void handleMessage(Message msg) {
		List<Subtitle> subtitles = (List<Subtitle>) msg.obj;
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
	}

}
