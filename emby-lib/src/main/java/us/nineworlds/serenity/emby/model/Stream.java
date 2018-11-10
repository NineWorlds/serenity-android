package us.nineworlds.serenity.emby.model;

import us.nineworlds.serenity.common.media.model.IStream;

public class Stream implements IStream {

  private int id;
  private int streamType;
  private String codec;
  private String index;
  private String channels;
  private String duration;
  private String bitrate;
  private String bitrateMode;
  private String profile;
  private String optimizedForStreaming;
  private String format;
  private String key;
  private String language;
  private String languageCode;

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
}
