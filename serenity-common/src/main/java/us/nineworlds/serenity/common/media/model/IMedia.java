package us.nineworlds.serenity.common.media.model;

import java.util.List;

public interface IMedia {
  String getContainer();

  void setContainer(String container);

  String getAudioChannels();

  void setAudioChannels(String audioChannels);

  List<IPart> getVideoPart();

  void setVideoPart(List<IPart> videoParts);

  String getAspectRatio();

  void setAspectRatio(String aspectRatio);

  String getAudioCodec();

  void setAudioCodec(String audioCodec);

  String getVideoCodec();

  void setVideoCodec(String videoCodec);

  String getVideoResolution();

  void setVideoResolution(String videoResolution);
}
