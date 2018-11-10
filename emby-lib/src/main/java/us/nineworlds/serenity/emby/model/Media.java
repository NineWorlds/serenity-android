package us.nineworlds.serenity.emby.model;

import java.util.List;
import us.nineworlds.serenity.common.media.model.IMedia;
import us.nineworlds.serenity.common.media.model.IPart;

public class Media implements IMedia {

  private String aspectRatio;
  private String audioCodec;
  private String videoCodec;
  private String videoResolution;
  private String container;
  private String audioChannels;
  private List<IPart> videoParts;

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
