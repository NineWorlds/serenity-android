package us.nineworlds.serenity.volley;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;
import us.nineworlds.serenity.core.model.impl.SubtitleMediaContainer;
import us.nineworlds.serenity.ui.listeners.SubtitleSpinnerOnItemSelectedListener;

import com.android.volley.Response;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class SubtitleVolleyResponseListener implements Response.Listener<MediaContainer> {

	protected VideoContentInfo video;
	protected Activity context;

	public SubtitleVolleyResponseListener(VideoContentInfo video, Activity c) {
		this.video = video;
		context = c;
	}


	@Override
	public void onResponse(MediaContainer response) {
		SubtitleMediaContainer subtitleMC = new SubtitleMediaContainer(response);
		List<Subtitle >subtitles = subtitleMC.createSubtitle();
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
