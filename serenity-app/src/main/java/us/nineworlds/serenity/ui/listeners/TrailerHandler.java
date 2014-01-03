package us.nineworlds.serenity.ui.listeners;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.DBMetaData;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.YouTubeVideoContentInfo;
import us.nineworlds.serenity.core.util.DBMetaDataSource;
import us.nineworlds.serenity.ui.util.ImageUtils;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class TrailerHandler extends Handler {
	
	protected Activity context;
	
	protected VideoContentInfo video;
	
	
	public TrailerHandler(VideoContentInfo mpi, Activity c) {
		video = mpi;
		context = c;
	}
	
	@Override
	public void handleMessage(Message msg) {
		YouTubeVideoContentInfo yt = (YouTubeVideoContentInfo) msg.obj;
		if (yt.id() == null) {
			return;
		}
		
		createMetaData(yt);
		
		LinearLayout infographicsView = (LinearLayout) context
				.findViewById(R.id.movieInfoGraphicLayout);
		ImageView ytImage = new ImageView(context);
		ytImage.setImageResource(R.drawable.yt_social_icon_red_128px);
		ytImage.setScaleType(ScaleType.FIT_XY);
		int w = ImageUtils.getDPI(45, context);
		int h = ImageUtils.getDPI(24, context);
		ytImage.setLayoutParams(new LinearLayout.LayoutParams(w, h));
		LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) ytImage.getLayoutParams();
		p.leftMargin = 5;
		p.gravity = Gravity.CENTER_VERTICAL;
		infographicsView.addView(ytImage);
		video.setTrailer(true);
		video.setTrailerId(yt.id());
	}

	/**
	 * @param yt
	 */
	protected void createMetaData(YouTubeVideoContentInfo yt) {
		DBMetaDataSource datasource = new DBMetaDataSource(context);
		datasource.open();
		DBMetaData metaData = datasource.findMetaDataByPlexId(video.id());
		if (metaData == null ) {
			datasource.createMetaData(yt.id(), video.id());
		}
		datasource.close();
	}
}
