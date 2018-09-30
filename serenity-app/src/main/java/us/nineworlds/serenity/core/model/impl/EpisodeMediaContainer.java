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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.core.model.impl;

import android.content.res.Resources;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import us.nineworlds.serenity.common.media.model.IDirector;
import us.nineworlds.serenity.common.media.model.IGenre;
import us.nineworlds.serenity.common.media.model.IMedia;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.media.model.IPart;
import us.nineworlds.serenity.common.media.model.IVideo;
import us.nineworlds.serenity.common.media.model.IWriter;
import us.nineworlds.serenity.core.model.VideoContentInfo;

/**
 * @author dcarver
 */
public class EpisodeMediaContainer extends MovieMediaContainer {

  @Inject Resources resources;

  public EpisodeMediaContainer(IMediaContainer mc) {
    super(mc);
  }

  @Override protected void createVideoContent(IMediaContainer mc) {
    String baseUrl = factory.baseURL();
    String parentPosterURL = null;
    if (mc.getParentPosterURL() != null && !mc.getParentPosterURL().contains("show")) {
      parentPosterURL = baseUrl + mc.getParentPosterURL().substring(1);
    }
    List<IVideo> videos = mc.getVideos();
    if (videos != null) {
      for (IVideo episode : videos) {
        videoList.add(createEpisodeContentInfo(mc, baseUrl, parentPosterURL, episode));
      }
    }
  }

  public EpisodePosterInfo createEpisodeContentInfo(IMediaContainer mc, String baseUrl, String parentPosterURL,
      IVideo episode) {
    EpisodePosterInfo epi = new EpisodePosterInfo(resources);
    if (parentPosterURL != null) {
      epi.setParentPosterURL(parentPosterURL);
    }
    epi.setId(episode.getKey());
    epi.setParentKey(episode.getParentKey());
    epi.setSummary(episode.getSummary());
    epi.setViewCount(episode.getViewCount());
    epi.setResumeOffset(Long.valueOf(episode.getViewOffset()).intValue());
    epi.setDuration(Long.valueOf(episode.getDuration()).intValue());

    epi.setOriginalAirDate(episode.getOriginallyAvailableDate());

    if (episode.getParentThumbNailImageKey() != null) {
      epi.setParentPosterURL(baseUrl + episode.getParentThumbNailImageKey().substring(1));
    }

    if (episode.getGrandParentThumbNailImageKey() != null) {
      epi.setGrandParentPosterURL(baseUrl + episode.getGrandParentThumbNailImageKey().substring(1));
    }

    String burl = factory.baseURL() + ":/resources/show-fanart.jpg";
    if (episode.getBackgroundImageKey() != null) {
      burl = baseUrl + episode.getBackgroundImageKey().replaceFirst("/", "");
    } else if (mc.getArt() != null) {
      burl = baseUrl + mc.getArt().replaceFirst("/", "");
    }

    epi.setBackgroundURL(burl);

    String turl = "";
    if (episode.getThumbNailImageKey() != null) {
      turl = baseUrl + episode.getThumbNailImageKey().replaceFirst("/", "");
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

    List<IMedia> mediacont = episode.getMedias();
    if (mediacont != null && !mediacont.isEmpty()) {
      // We grab the first media container until we know more about
      // why there can be multiples.
      IMedia media = mediacont.get(0);
      epi.setContainer(media.getContainer());
      List<IPart> parts = media.getVideoPart();
      if (parts != null) {
        IPart part = parts.get(0);
        String directPlayUrl = factory.baseURL() + part.getKey().replaceFirst("/", "");
        epi.setDirectPlayUrl(directPlayUrl);
      } else {
        epi.setDirectPlayUrl(factory.baseURL() + episode.getDirectPlayUrl());
      }

      final int seasonNumber;
      if (episode.getSeason() != null) {
        seasonNumber = parseInt(episode.getSeason(), 0);
      } else {
        seasonNumber = parseInt(mc.getParentIndex(), 0);
      }
      epi.setSeasonNumber(seasonNumber);

      final int episodeNumber = parseInt(episode.getEpisode(), 0);
      epi.setEpisodeNumber(episodeNumber);

      epi.setAudioCodec(media.getAudioCodec());
      epi.setVideoCodec(media.getVideoCodec());
      epi.setVideoResolution(media.getVideoResolution());
      epi.setAspectRatio(media.getAspectRatio());
      epi.setAudioChannels(media.getAudioChannels());

    }

    createVideoDetailsStatic(episode, epi);
    epi.setCastInfo("");

    return epi;
  }

  private static int parseInt(String numberString, int defaultValue) {
    if (numberString == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(numberString);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * @param video
   * @param videoContentInfo
   * @return
   */
  @Override protected void createVideoDetails(IVideo video, VideoContentInfo videoContentInfo) {

    createVideoDetailsStatic(video, videoContentInfo);
  }

  private void createVideoDetailsStatic(IVideo video, VideoContentInfo videoContentInfo) {
    if (video.getYear() != null) {
      videoContentInfo.setYear(video.getYear());
    }

    if (video.getGenres() != null && video.getGenres().size() > 0) {
      ArrayList<String> g = new ArrayList<String>();
      for (IGenre genre : video.getGenres()) {
        g.add(genre.getTag());
      }
      videoContentInfo.setGenres(g);
    }

    if (video.getWriters() != null && video.getWriters().size() > 0) {
      ArrayList<String> w = new ArrayList<String>();
      for (IWriter writer : video.getWriters()) {
        w.add(writer.getTag());
      }
      videoContentInfo.setWriters(w);
    }

    if (video.getDirectors() != null && video.getDirectors().size() > 0) {
      ArrayList<String> d = new ArrayList<String>();
      for (IDirector director : video.getDirectors()) {
        d.add(director.getTag());
      }
      videoContentInfo.setDirectors(d);
    }
  }
}