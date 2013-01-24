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

package com.github.kingargyle.plexappclient.ui.browser.tv.episodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.PlexappFactory;
import com.github.kingargyle.plexapp.model.impl.Director;
import com.github.kingargyle.plexapp.model.impl.Genre;
import com.github.kingargyle.plexapp.model.impl.Media;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Part;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexapp.model.impl.Writer;
import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.SerenityApplication;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTagFactory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioGroup.LayoutParams;

/**
 * 
 * @author dcarver
 *
 */
public class EpisodePosterImageGalleryAdapter extends BaseAdapter {
	
	private List<EpisodePosterInfo> posterList = null;
	private Activity context;
	
	private ImageManager imageManager;
	private ImageTagFactory imageTagFactory;
	private static final int SIZE_HEIGHT = 400;
	private static final int SIZE_WIDTH = 200;
	private PlexappFactory factory;
	private String key;
	
	
	public EpisodePosterImageGalleryAdapter(Context c, String key) {
		context = (Activity)c;
		this.key = key;
		
		posterList = new ArrayList<EpisodePosterInfo>();
		
		imageManager = SerenityApplication.getImageManager();
		imageTagFactory = ImageTagFactory.newInstance(SIZE_WIDTH, SIZE_HEIGHT, R.drawable.default_video_cover);
		imageTagFactory.setErrorImageId(R.drawable.default_error);
		imageTagFactory.setSaveThumbnail(true);
				
		createPosters();
	}
	
	
	
	private void createPosters() {
		
		MediaContainer mc = null;
		String baseUrl = null;
		try {
			factory = SerenityApplication.getPlexFactory();
			mc = factory.retrieveEpisodes(key);
			baseUrl = factory.baseURL();
		} catch (IOException ex) {
			Log.w("Unable to talk to server: ", ex);
		} catch (Exception e) {
			Log.w("Oops.", e);
		}
		
		if (mc.getSize() > 0) {
			List<Video> videos = mc.getVideos();
			for (Video episode : videos) {
				EpisodePosterInfo  epi = new EpisodePosterInfo();
				epi.setPlotSummary(episode.getSummary());
				
				String burl = factory.baseURL() + ":/resources/show-fanart.jpg"; 
				if (mc.getArt() != null) {
					burl = baseUrl + mc.getArt().replaceFirst("/", "");
				}
				epi.setBackgroundURL(burl);
				
				String turl = "";
				if (episode.getThumbNailImageKey() != null) {
					turl = baseUrl + episode.getThumbNailImageKey().replaceFirst("/", "");
				}
				
				epi.setPosterURL(turl);
				epi.setTitle(episode.getTitle());
				
				epi.setContentRating(episode.getContentRating());
				
				Media media = episode.getMedia();
				Part part = media.getVideoPart();
				epi.setAudioCodec(media.getAudioCodec());
				epi.setVideoCodec(media.getVideoCodec());
				epi.setVideoResolution(media.getVideoResolution());
				
				String directPlayUrl = factory.baseURL() + part.getKey().replaceFirst("/", "");
				epi.setDirectPlayUrl(directPlayUrl);
				
				String episodeDetails = "";
				
				if (episode.getYear() != null) {
					epi.setYear(episode.getYear());
					episodeDetails = "Year: " + episode.getYear();
					episodeDetails = episodeDetails + "\r\n";
				}
				
				if (episode.getGenres() != null && episode.getGenres().size() > 0) {
					ArrayList<String> g = new ArrayList<String>();
					for (Genre genre : episode.getGenres()) {
						g.add(genre.getTag());
						episodeDetails = episodeDetails + genre.getTag() + "/";
					}
					epi.setGenres(g);
					episodeDetails = episodeDetails.substring(0, episodeDetails.lastIndexOf("/"));
					episodeDetails = episodeDetails + "\r\n";
				}
				
				
				if (episode.getWriters() != null && episode.getWriters().size() > 0) {
					episodeDetails = episodeDetails + "Writer(s): ";
					ArrayList<String> w = new ArrayList<String>();
					for (Writer writer : episode.getWriters()) {
						w.add(writer.getTag());
						episodeDetails = episodeDetails + writer.getTag() + ", ";
					}
					epi.setWriters(w);
					episodeDetails = episodeDetails.substring(0, episodeDetails.lastIndexOf(","));
					episodeDetails = episodeDetails + "\r\n";
				}
				
				if (episode.getDirectors() != null && episode.getDirectors().size() > 0) {
					episodeDetails = episodeDetails + "Director(s): ";
					ArrayList<String> d = new ArrayList<String>();
					for (Director director : episode.getDirectors()) {
						d.add(director.getTag());
						episodeDetails = episodeDetails + director.getTag() + ", ";
					}
					epi.setDirectors(d);
					episodeDetails = episodeDetails.substring(0, episodeDetails.lastIndexOf(","));
					episodeDetails = episodeDetails + "\r\n";
				}
				
				epi.setCastInfo(episodeDetails);				
			
				posterList.add(epi);
			}
		}		
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		
		return posterList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		
		return posterList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		EpisodePosterInfo pi = posterList.get(position);
		EpisodePosterImageView mpiv = new EpisodePosterImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL(), context));
		} else {
			mpiv.setTag(imageTagFactory.build(factory.baseURL() + ":/resources/movie-fanart.jpg", context));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new Gallery.LayoutParams(300, LayoutParams.FILL_PARENT));
		
		imageManager.getLoader().load((ImageView) mpiv);
		
		//imDownload.download(pi.getPosterURL(), mpiv);
	
		return mpiv;
	}

}
