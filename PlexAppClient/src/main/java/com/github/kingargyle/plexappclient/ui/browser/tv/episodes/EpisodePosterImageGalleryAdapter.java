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

import java.util.List;

import com.github.kingargyle.plexapp.model.impl.Media;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.model.impl.Part;
import com.github.kingargyle.plexapp.model.impl.Video;
import com.github.kingargyle.plexappclient.core.model.VideoContentInfo;
import com.github.kingargyle.plexappclient.core.model.impl.EpisodePosterInfo;
import com.github.kingargyle.plexappclient.ui.adapters.AbstractPosterImageGalleryAdapter;
import com.github.kingargyle.plexappclient.ui.views.SerenityPosterImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Implementation of the Poster Image Gallery class for TV Shows.
 *  
 * @author dcarver
 *
 */
public class EpisodePosterImageGalleryAdapter extends AbstractPosterImageGalleryAdapter {
	
	public EpisodePosterImageGalleryAdapter(Context c, String key) {
		super(c, key);
	}
	
	/**
	 * 
	 * @return A media container with episodes or videos
	 * 
	 * @throws Exception
	 */
	protected MediaContainer retrieveVideos() throws Exception {
		MediaContainer mc;
		mc = factory.retrieveEpisodes(key);
		return mc;
	}

	/**
	 * @param mc
	 * @param baseUrl
	 */
	protected void createVideoContent(MediaContainer mc) {
		String baseUrl = factory.baseURL();
		List<Video> videos = mc.getVideos();
		for (Video episode : videos) {
			VideoContentInfo  epi = new EpisodePosterInfo();
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
			
			List<Media> mediacont = episode.getMedias();
			if (mediacont != null && !mediacont.isEmpty()) {
				// We grab the first media container until we know more about why there can be multiples.
				Media media = mediacont.get(0);
				List<Part> parts = media.getVideoPart();
				Part part = parts.get(0);
				epi.setAudioCodec(media.getAudioCodec());
				epi.setVideoCodec(media.getVideoCodec());
				epi.setVideoResolution(media.getVideoResolution());
				epi.setAspectRatio(media.getAspectRatio());
				
				String directPlayUrl = factory.baseURL() + part.getKey().replaceFirst("/", "");
				epi.setDirectPlayUrl(directPlayUrl);
				
			}
			
			String episodeDetails = createVideoDetails(episode, epi);
			epi.setCastInfo(episodeDetails);				
		
			posterList.add(epi);
		}
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		
		VideoContentInfo pi = posterList.get(position);
		SerenityPosterImageView mpiv = new SerenityPosterImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL(), context));
		} else {
			mpiv.setTag(imageTagFactory.build(factory.baseURL() + ":/resources/show-fanart.jpg", context));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new Gallery.LayoutParams(300, android.view.ViewGroup.LayoutParams.FILL_PARENT));
		
		imageManager.getLoader().load(mpiv);
	
		return mpiv;
	}

}
