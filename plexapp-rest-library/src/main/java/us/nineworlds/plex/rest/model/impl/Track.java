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
import us.nineworlds.serenity.common.media.model.ITrack;

@Root(name = "Track")
public class Track implements ITrack {

  @Attribute(name = "key", required = true) private String key;

  @Attribute(required = false) private String title;

  @Attribute(required = false) private String summary;

  @Attribute(required = false) private String type;

  @Attribute(required = false) private String parentKey;

  @Attribute(required = false) private int index;

  @Attribute(name = "duration", required = false) private long duration;

  @Attribute(name = "addedAt", required = false) private long timeAdded;

  @Attribute(name = "updatedAt", required = false) private long timeUpdated;

  @ElementList(inline = true, name = "Media", required = true, type = Media.class) private List<IMedia> medias;

  @Override public String getKey() {
    return key;
  }

  @Override public void setKey(String key) {
    this.key = key;
  }

  @Override public String getTitle() {
    return title;
  }

  @Override public void setTitle(String title) {
    this.title = title;
  }

  @Override public String getSummary() {
    return summary;
  }

  @Override public void setSummary(String summary) {
    this.summary = summary;
  }

  @Override public String getType() {
    return type;
  }

  @Override public void setType(String type) {
    this.type = type;
  }

  @Override public String getParentKey() {
    return parentKey;
  }

  @Override public void setParentKey(String parentKey) {
    this.parentKey = parentKey;
  }

  @Override public int getIndex() {
    return index;
  }

  @Override public void setIndex(int index) {
    this.index = index;
  }

  @Override public long getDuration() {
    return duration;
  }

  @Override public void setDuration(long duration) {
    this.duration = duration;
  }

  @Override public long getTimeAdded() {
    return timeAdded;
  }

  @Override public void setTimeAdded(long timeAdded) {
    this.timeAdded = timeAdded;
  }

  @Override public long getTimeUpdated() {
    return timeUpdated;
  }

  @Override public void setTimeUpdated(long timeUpdated) {
    this.timeUpdated = timeUpdated;
  }

  @Override public List<IMedia> getMedias() {
    return medias;
  }

  @Override public void setMedias(List<IMedia> medias) {
    this.medias = medias;
  }
}
