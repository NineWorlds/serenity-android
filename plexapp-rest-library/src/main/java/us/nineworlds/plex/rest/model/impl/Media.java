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

package us.nineworlds.plex.rest.model.impl;

import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import us.nineworlds.serenity.common.media.model.IMedia;
import us.nineworlds.serenity.common.media.model.IPart;

/**
 * @author dcarver
 */
@Root(name = "Media")
public class Media implements IMedia {

  @Attribute(name = "aspectRatio", required = false) private String aspectRatio;

  @Attribute(name = "audioCodec", required = false) private String audioCodec;

  @Attribute(name = "videoCodec", required = false) private String videoCodec;

  @Attribute(name = "videoResolution", required = false) private String videoResolution;

  @Attribute(name = "container", required = false) private String container;

  @Attribute(name = "audioChannels", required = false) private String audioChannels;

  @ElementList(inline = true, name = "Part", required = false, type = Part.class) private List<IPart> videoParts;

  @Override public String getAudioChannels() {
    return audioChannels;
  }

  @Override public void setAudioChannels(String audioChannels) {
    this.audioChannels = audioChannels;
  }

  @Override public String getContainer() {
    return container;
  }

  @Override public void setContainer(String container) {
    this.container = container;
  }

  @Override public List<IPart> getVideoPart() {
    return videoParts;
  }

  @Override public void setVideoPart(List<IPart> videoParts) {
    this.videoParts = videoParts;
  }

  @Override public String getAspectRatio() {
    return aspectRatio;
  }

  @Override public void setAspectRatio(String aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  @Override public String getAudioCodec() {
    return audioCodec;
  }

  @Override public void setAudioCodec(String audioCodec) {
    this.audioCodec = audioCodec;
  }

  @Override public String getVideoCodec() {
    return videoCodec;
  }

  @Override public void setVideoCodec(String videoCodec) {
    this.videoCodec = videoCodec;
  }

  @Override public String getVideoResolution() {
    return videoResolution;
  }

  @Override public void setVideoResolution(String videoResolution) {
    this.videoResolution = videoResolution;
  }
}
