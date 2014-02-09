package us.nineworlds.serenity.ui.listeners;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.YouTubeVideoContentInfo;
import android.app.Activity;
import android.os.Message;
import android.view.View;

public class TrailerGridHandler extends TrailerHandler {
	
	private View posterView;
	
	/**
	 * @param mpi
	 * @param c
	 * @param view The view that holds the current poster information
	 */
	public TrailerGridHandler(VideoContentInfo mpi, Activity c, View view) {
		super(mpi, c);
		posterView = view;
	}
	
	@Override
	public void handleMessage(Message msg) {
		YouTubeVideoContentInfo yt = (YouTubeVideoContentInfo) msg.obj;
		View trailerIndicator = posterView.findViewById(R.id.trailerIndicator);
		View infoGraphicMeta = posterView.findViewById(R.id.infoGraphicMeta);
		if (yt.id() == null) {
			infoGraphicMeta.setVisibility(View.GONE);
			return;
		}
		
		createMetaData(yt);
				
		trailerIndicator.setVisibility(View.VISIBLE);
		infoGraphicMeta.setVisibility(View.VISIBLE);
		
		video.setTrailer(true);
		video.setTrailerId(yt.id());
	}
	
}
