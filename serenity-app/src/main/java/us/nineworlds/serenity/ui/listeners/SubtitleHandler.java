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

public class SubtitleHandler extends Handler {

	private VideoContentInfo video;
	private Activity context;

	public SubtitleHandler(VideoContentInfo video, Activity c) {
		this.video = video;
		context = c;
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
