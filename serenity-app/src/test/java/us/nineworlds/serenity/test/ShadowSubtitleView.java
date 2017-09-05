package us.nineworlds.serenity.test;

import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.ui.SubtitleView;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowView;

@Implements(SubtitleView.class)
public class ShadowSubtitleView extends ShadowView {

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
