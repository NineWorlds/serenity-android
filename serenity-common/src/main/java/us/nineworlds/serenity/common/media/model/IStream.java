package us.nineworlds.serenity.common.media.model;

public interface IStream {
  String getLanguage();

  void setLanguage(String language);

  String getLanguageCode();

  void setLanguageCode(String languageCode);

  int getId();

  void setId(int id);

  int getStreamType();

  void setStreamType(int streamType);

  String getCodec();

  void setCodec(String codec);

  String getIndex();

  void setIndex(String index);

  String getChannels();

  void setChannels(String channels);

  String getDuration();

  void setDuration(String duration);

  String getBitrate();

  void setBitrate(String bitrate);

  String getBitrateMode();

  void setBitrateMode(String bitrateMode);

  String getProfile();

  void setProfile(String profile);

  String getOptimizedForStreaming();

  void setOptimizedForStreaming(String optimizedForStreaming);

  String getFormat();

  void setFormat(String format);

  String getKey();

  void setKey(String key);

  String getMediaSourceKey();

  void setMediaSourceKey(String key);
}
