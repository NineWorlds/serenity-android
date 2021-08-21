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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.ui.adapters;

import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.R;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.InjectingRecyclerViewAdapter;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemClickListener;
import us.nineworlds.serenity.ui.listeners.AbstractVideoOnItemSelectedListener;

/**
 * An abstract class for handling the creation of video content for use during
 * browsing. Implementations need to implement the abstract methods to provide
 * functionality for retrieval and display of video content when browsing the
 * episodes.
 *
 * @author dcarver
 */
public abstract class AbstractPosterImageGalleryAdapter extends InjectingRecyclerViewAdapter implements
    View.OnKeyListener {

  protected static List<VideoContentInfo> posterList = null;
  protected Handler handler;
  protected AbstractVideoOnItemClickListener onItemClickListener;
  protected AbstractVideoOnItemSelectedListener onItemSelectedListener;
  protected boolean triggerFocusSelection = true;

  private Animation scaleSmallAnimation;
  private Animation scaleBigAnimation;

  public AbstractPosterImageGalleryAdapter() {
    posterList = new ArrayList<>();
  }

  @Override public int getItemCount() {
    return posterList.size();
  }

  public Object getItem(int position) {
    if (position < posterList.size()) {
      return posterList.get(position);
    }

    return posterList.get(0);
  }

  @Override public long getItemId(int position) {
    return position;
  }


  public List<VideoContentInfo> getItems() {
    return posterList;
  }

  public AbstractVideoOnItemClickListener getOnItemClickListener() {
    return onItemClickListener;
  }

  public void setOnItemClickListener(AbstractVideoOnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  public AbstractVideoOnItemSelectedListener getOnItemSelectedListener() {
    return onItemSelectedListener;
  }

  public void setOnItemSelectedListener(AbstractVideoOnItemSelectedListener onItemSelectedListener) {
    this.onItemSelectedListener = onItemSelectedListener;
  }

  protected void zoomIn(View view) {
    view.clearAnimation();
    if (scaleSmallAnimation == null) {
      scaleSmallAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_scale_small);
    }
    view.startAnimation(scaleSmallAnimation);
  }

  protected void zoomOut(View view) {
    if (scaleBigAnimation == null) {
      scaleBigAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.anim_scale_big);
    }
    view.startAnimation(scaleBigAnimation);
  }

  public boolean onKey(View v, int keyCode, KeyEvent event) {
    triggerFocusSelection = true;

    if (event.getAction() == KeyEvent.ACTION_UP  && isDirectionalPadKeyCode(keyCode)) {
      v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.rounded_transparent_border));
      return true;
    }

    if (event.getRepeatCount() > 1 && isDirectionalPadKeyCode(keyCode)) {
      triggerFocusSelection = false;
    }

    return false;
  }

  protected boolean isDirectionalPadKeyCode(int keyCode) {
    return keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
    keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
    keyCode == KeyEvent.KEYCODE_DPAD_UP ||
    keyCode == KeyEvent.KEYCODE_DPAD_DOWN;
  }
}
