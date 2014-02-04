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

package us.nineworlds.serenity.core.model.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.Director;
import us.nineworlds.plex.rest.model.impl.Genre;
import us.nineworlds.plex.rest.model.impl.Media;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Part;
import us.nineworlds.plex.rest.model.impl.Video;
import us.nineworlds.plex.rest.model.impl.Writer;
import us.nineworlds.serenity.core.model.VideoContentInfo;

/**
 * @author dcarver
 * 
 */
public class MovieMediaContainer extends AbstractMediaContainer {

	/**
	 * 
	 */
	public MovieMediaContainer(MediaContainer mc) {
		super(mc);
	}

	public List<VideoContentInfo> createVideos() {
		videoList = new LinkedList<VideoContentInfo>();
		createVideoContent(mc);
		return videoList;
	}

	protected void createVideoContent(MediaContainer mc) {
		String baseUrl = factory.baseURL();
		List<Video> videos = mc.getVideos();
		String mediaTagId = Long.valueOf(mc.getMediaTagVersion()).toString();
		StringBuilder sbuild = new StringBuilder();
		sbuild.append(baseUrl);
		sbuild.append(":/resources/movie-fanart.jpg");
		String baseImageResource = sbuild.toString();
		
		for (Video movie : videos) {
			VideoContentInfo mpi = new MoviePosterInfo();
			mpi.setMediaTagIdentifier(mediaTagId);
			mpi.setId(movie.getRatingKey());
			mpi.setStudio(movie.getStudio());
			mpi.setSummary(movie.getSummary());

			mpi.setResumeOffset(Long.valueOf(movie.getViewOffset()).intValue());
			mpi.setDuration(Long.valueOf(movie.getDuration()).intValue());

			mpi.setViewCount(movie.getViewCount());
			mpi.setRating(movie.getRating());

			String burl = baseImageResource;
			if (movie.getBackgroundImageKey() != null) {
				StringBuilder builder = new StringBuilder();
				builder.append(baseUrl);
				builder.append(movie.getBackgroundImageKey().replaceFirst("/", ""));
				burl = builder.toString();
			}
			mpi.setBackgroundURL(burl);

			String turl = "";
			if (movie.getThumbNailImageKey() != null) {
				StringBuilder builder = new StringBuilder();
				builder.append(baseUrl);
				builder.append(movie.getThumbNailImageKey().replaceFirst("/", ""));
				turl = builder.toString();
			}

			mpi.setImageURL(turl);
			mpi.setTitle(movie.getTitle());

			mpi.setContentRating(movie.getContentRating());

			List<Media> mediacont = movie.getMedias();
			if (mediacont != null && !mediacont.isEmpty()) {
				// We grab the first media container until we know more about
				// why there can be multiples.
				Media media = mediacont.get(0);
				mpi.setContainer(media.getContainer());
				List<Part> parts = media.getVideoPart();
				Part part = parts.get(0);
				mpi.setAudioCodec(media.getAudioCodec());
				mpi.setVideoCodec(media.getVideoCodec());
				mpi.setVideoResolution(media.getVideoResolution());
				mpi.setAspectRatio(media.getAspectRatio());
				mpi.setAudioChannels(media.getAudioChannels());
				StringBuilder builder = new StringBuilder();
				builder.append(factory.baseURL());
				builder.append(part.getKey().replaceFirst("/", ""));
				
				String directPlayUrl = builder.toString();
				mpi.setDirectPlayUrl(directPlayUrl);

			}

			createVideoDetails(movie, mpi);

			videoList.add(mpi);
		}

	}

	/**
	 * Create the video meta data around cast, direct, year produced, etc.
	 * 
	 * @param video
	 * @param videoContentInfo
	 * @return
	 */
	protected void createVideoDetails(Video video,
			VideoContentInfo videoContentInfo) {

		if (video.getYear() != null) {
			videoContentInfo.setYear(video.getYear());
		}

		if (video.getGenres() != null && video.getGenres().size() > 0) {
			ArrayList<String> g = new ArrayList<String>();

			for (Genre genre : video.getGenres()) {
				g.add(genre.getTag());
			}
			videoContentInfo.setGenres(g);
		}

		if (video.getWriters() != null && video.getWriters().size() > 0) {
			ArrayList<String> w = new ArrayList<String>();
			for (Writer writer : video.getWriters()) {
				w.add(writer.getTag());
			}
			videoContentInfo.setWriters(w);
		}

		if (video.getDirectors() != null && video.getDirectors().size() > 0) {
			ArrayList<String> d = new ArrayList<String>();
			for (Director director : video.getDirectors()) {
				d.add(director.getTag());
			}
			videoContentInfo.setDirectors(d);
		}
	}

}
