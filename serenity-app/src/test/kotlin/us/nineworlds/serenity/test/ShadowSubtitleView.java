package us.nineworlds.serenity.test;


import androidx.media3.ui.CaptionStyleCompat;
import androidx.media3.ui.SubtitleView;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowViewGroup;

@Implements(SubtitleView.class)
public class ShadowSubtitleView extends ShadowViewGroup {

  @RealObject private SubtitleView subtitleView;

  boolean setUserDefaultTextSize = false;

  @Implementation public void setUserDefaultTextSize() {
    setUserDefaultTextSize = true;
  }

  public boolean isSetUserDefaultTextSizeCalled() {
    return setUserDefaultTextSize;
  }

  @Implementation public void setUserDefaultStyle() {
    subtitleView.setStyle(CaptionStyleCompat.DEFAULT);
  }
}
