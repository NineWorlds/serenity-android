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
import java.util.List;

import android.content.Context;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.Director;
import us.nineworlds.plex.rest.model.impl.Genre;
import us.nineworlds.plex.rest.model.impl.Media;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Part;
import us.nineworlds.plex.rest.model.impl.Video;
import us.nineworlds.plex.rest.model.impl.Writer;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;

/**
 * @author dcarver
 *
 */
public class EpisodeMediaContainer extends MovieMediaContainer {

	protected Context context;

	/**
	 * @param mc
	 */
	public EpisodeMediaContainer(MediaContainer mc, Context context) {
		super(mc);
		this.context = context;
	}

	/**
	 * @param mc
	 * @param baseUrl
	 */
	@Override
	protected void createVideoContent(MediaContainer mc) {
		String baseUrl = factory.baseURL();
		String parentPosterURL = null;
		if (mc.getParentPosterURL() != null && !mc.getParentPosterURL().contains("show")) {
			parentPosterURL = baseUrl + mc.getParentPosterURL().substring(1);
		}
		List<Video> videos = mc.getVideos();
		for (Video episode : videos) {
			videoList.add(createEpisodeContentInfo(context, factory, mc, baseUrl, parentPosterURL, episode));
		}
	}

	public static EpisodePosterInfo createEpisodeContentInfo(Context context, PlexappFactory factory, MediaContainer mc, String baseUrl, String parentPosterURL, Video episode) {
		EpisodePosterInfo epi = new EpisodePosterInfo();
		if (parentPosterURL != null) {
			epi.setParentPosterURL(parentPosterURL);
		}
		epi.setId(episode.getRatingKey());
		epi.setParentKey(episode.getParentKey());
		epi.setSummary(episode.getSummary());
		epi.setViewCount(episode.getViewCount());
		epi.setResumeOffset(Long.valueOf(episode.getViewOffset())
				.intValue());
		epi.setDuration(Long.valueOf(episode.getDuration()).intValue());

		epi.setOriginalAirDate(episode.getOriginallyAvailableDate());

		if (episode.getParentThumbNailImageKey() != null) {
			epi.setParentPosterURL(baseUrl
					+ episode.getParentThumbNailImageKey().substring(1));
		}

		if (episode.getGrandParentThumbNailImageKey() != null) {
			epi.setGrandParentPosterURL(baseUrl
					+ episode.getGrandParentThumbNailImageKey().substring(1));
		}

		String burl = factory.baseURL() + ":/resources/show-fanart.jpg";
		if (episode.getBackgroundImageKey() != null) {
			burl = baseUrl
					+ episode.getBackgroundImageKey().replaceFirst("/", "");
		} else if (mc.getArt() != null) {
			burl = baseUrl + mc.getArt().replaceFirst("/", "");
		}

		epi.setBackgroundURL(burl);

		String turl = "";
		if (episode.getThumbNailImageKey() != null) {
			turl = baseUrl
					+ episode.getThumbNailImageKey().replaceFirst("/", "");
		}

		epi.setImageURL(turl);
		epi.setTitle(episode.getTitle());

		if (episode.getGrandParentTitle() != null) {
			epi.setSeriesTitle(episode.getGrandParentTitle());
		}

		if (epi.getSeriesTitle() == null) {
			epi.setSeriesTitle(mc.getTitle1());
		}

		epi.setContentRating(episode.getContentRating());

		List<Media> mediacont = episode.getMedias();
		if (mediacont != null && !mediacont.isEmpty()) {
			// We grab the first media container until we know more about
			// why there can be multiples.
			Media media = mediacont.get(0);
			epi.setContainer(media.getContainer());
			List<Part> parts = media.getVideoPart();
			Part part = parts.get(0);

			if (episode.getSeason() != null) {
				String season = context.getString(R.string.season_) + episode.getSeason();
				epi.setSeason(season);
			} else if (mc.getParentIndex() != null) {
				String season = context.getString(R.string.season_) + mc.getParentIndex();
				epi.setSeason(season);
			}

			if (episode.getEpisode() != null) {
				String episodeNum = context.getString(R.string.episode_) + episode.getEpisode();
				epi.setEpisodeNumber(episodeNum);
			}
//				setSeasonEpisode(epi, part);

			epi.setAudioCodec(media.getAudioCodec());
			epi.setVideoCodec(media.getVideoCodec());
			epi.setVideoResolution(media.getVideoResolution());
			epi.setAspectRatio(media.getAspectRatio());
			epi.setAudioChannels(media.getAudioChannels());

			String directPlayUrl = factory.baseURL()
					+ part.getKey().replaceFirst("/", "");
			epi.setDirectPlayUrl(directPlayUrl);

		}

		createVideoDetailsStatic(episode, epi);
		epi.setCastInfo("");

		return epi;
	}


	/**
	 * @param video
	 * @param videoContentInfo
	 * @return
	 */
	@Override
	protected void createVideoDetails(Video video,
									  VideoContentInfo videoContentInfo) {

		createVideoDetailsStatic(video, videoContentInfo);

	}

	private static void createVideoDetailsStatic(Video video, VideoContentInfo videoContentInfo) {
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

	private String getString(int resId) {
		return context.getString(resId);
	}
}