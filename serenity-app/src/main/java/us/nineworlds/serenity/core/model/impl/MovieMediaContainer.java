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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
public class MovieMediaContainer extends AbstractMediaContainer {

  public MovieMediaContainer(IMediaContainer mc) {
    super(mc);
  }

  public List<VideoContentInfo> createVideos() {
    videoList = new LinkedList<VideoContentInfo>();
    createVideoContent(mc);
    return videoList;
  }

  protected void createVideoContent(IMediaContainer mc) {
    String baseUrl = factory.baseURL();
    List<IVideo> videos = mc.getVideos();
    if (videos == null) {
      return;
    }

    String mediaTagId = Long.valueOf(mc.getMediaTagVersion()).toString();
    StringBuilder sbuild = new StringBuilder();
    sbuild.append(baseUrl);
    sbuild.append(":/resources/movie-fanart.jpg");
    String baseImageResource = sbuild.toString();
    for (IVideo movie : videos) {
      VideoContentInfo mpi = new MoviePosterInfo();
      mpi.setMediaTagIdentifier(mediaTagId);
      mpi.setId(movie.getRatingKey());
      mpi.setStudio(movie.getStudio());
      mpi.setSummary(movie.getSummary());

      mpi.setResumeOffset(Long.valueOf(movie.getViewOffset()).intValue());
      mpi.setDuration(Long.valueOf(movie.getDuration()).intValue());

      mpi.setViewCount(movie.getViewCount());
      mpi.setRating(movie.getRating());

      if (movie.getTagLine() != null) {
        mpi.setTagLine(movie.getTagLine());
      }

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

      List<IMedia> mediacont = movie.getMedias();
      if (mediacont != null && !mediacont.isEmpty()) {
        // We grab the first media container until we know more about
        // why there can be multiples.
        IMedia media = mediacont.get(0);
        mpi.setContainer(media.getContainer());
        List<IPart> parts = media.getVideoPart();
        IPart part = parts.get(0);
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
   */
  protected void createVideoDetails(IVideo video, VideoContentInfo videoContentInfo) {

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
