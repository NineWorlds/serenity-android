package us.nineworlds.serenity.test;

import com.google.android.exoplayer2.ui.CaptionStyleCompat;
import com.google.android.exoplayer2.ui.SubtitleView;
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
