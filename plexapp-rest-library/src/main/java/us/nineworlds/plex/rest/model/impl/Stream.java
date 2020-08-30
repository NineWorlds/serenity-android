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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import us.nineworlds.serenity.common.media.model.IStream;

@Root(name = "Stream")
public class Stream implements IStream {

  @Attribute(name = "id", required = false) private int id;

  @Attribute(name = "streamType", required = false) private int streamType;

  @Attribute(name = "codec", required = false) private String codec;

  @Attribute(name = "index", required = false) private String index;

  @Attribute(name = "channels", required = false) private String channels;

  @Attribute(name = "duration", required = false) private String duration;

  @Attribute(name = "bitrate", required = false) private String bitrate;

  @Attribute(name = "bitrateMode", required = false) private String bitrateMode;

  @Attribute(name = "profile", required = false) private String profile;

  @Attribute(name = "optimizedForStreaming", required = false) private String optimizedForStreaming;

  @Attribute(name = "format", required = false) private String format;

  @Attribute(name = "key", required = false) private String key;

  @Attribute(name = "language", required = false) private String language;

  @Attribute(name = "languageCode", required = false) private String languageCode;

  @Override public String getLanguage() {
    return language;
  }

  @Override public void setLanguage(String language) {
    this.language = language;
  }

  @Override public String getLanguageCode() {
    return languageCode;
  }

  @Override public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }

  @Override public int getId() {
    return id;
  }

  @Override public void setId(int id) {
    this.id = id;
  }

  @Override public int getStreamType() {
    return streamType;
  }

  @Override public void setStreamType(int streamType) {
    this.streamType = streamType;
  }

  @Override public String getCodec() {
    return codec;
  }

  @Override public void setCodec(String codec) {
    this.codec = codec;
  }

  @Override public String getIndex() {
    return index;
  }

  @Override public void setIndex(String index) {
    this.index = index;
  }

  @Override public String getChannels() {
    return channels;
  }

  @Override public void setChannels(String channels) {
    this.channels = channels;
  }

  @Override public String getDuration() {
    return duration;
  }

  @Override public void setDuration(String duration) {
    this.duration = duration;
  }

  @Override public String getBitrate() {
    return bitrate;
  }

  @Override public void setBitrate(String bitrate) {
    this.bitrate = bitrate;
  }

  @Override public String getBitrateMode() {
    return bitrateMode;
  }

  @Override public void setBitrateMode(String bitrateMode) {
    this.bitrateMode = bitrateMode;
  }

  @Override public String getProfile() {
    return profile;
  }

  @Override public void setProfile(String profile) {
    this.profile = profile;
  }

  @Override public String getOptimizedForStreaming() {
    return optimizedForStreaming;
  }

  @Override public void setOptimizedForStreaming(String optimizedForStreaming) {
    this.optimizedForStreaming = optimizedForStreaming;
  }

  @Override public String getFormat() {
    return format;
  }

  @Override public void setFormat(String format) {
    this.format = format;
  }

  @Override public String getKey() {
    return key;
  }

  @Override public void setKey(String key) {
    this.key = key;
  }

  @Override
  public String getMediaSourceKey() {
    return null;
  }

  @Override
  public void setMediaSourceKey(String key) {

  }
}
