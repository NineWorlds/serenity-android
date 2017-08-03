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

package us.nineworlds.serenity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.LinkedList;
import javax.inject.Inject;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.injection.BaseInjector;
import us.nineworlds.serenity.injection.ForVideoQueue;
import us.nineworlds.serenity.widgets.DrawerLayout;

public class MainMenuDrawerOnItemClickedListener extends BaseInjector
    implements OnItemClickListener {

  @Inject @ForVideoQueue LinkedList<VideoContentInfo> videoQueue;
  @Inject Resources resources;

  private static final int ABOUT = 0;
  private static final int CLEAR_CACHE = 1;
  private static final int CLEAR_QUEUE = 2;
  private final DrawerLayout drawerLayout;

  public MainMenuDrawerOnItemClickedListener(DrawerLayout drawerLayout) {
    this.drawerLayout = drawerLayout;
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    toggleMenu();

    switch (position) {
      case CLEAR_CACHE:
        createClearCacheDialog(view.getContext());
        break;
      case ABOUT:
        AboutDialog about = new AboutDialog(view.getContext());
        about.setTitle(R.string.about_title_serenity_for_google_tv);
        about.show();
        break;
      case CLEAR_QUEUE:
        videoQueue.clear();
        Toast.makeText(view.getContext(), resources.getString(R.string.queue_has_been_cleared_),
            Toast.LENGTH_LONG).show();
        break;
    }
  }

  protected void toggleMenu() {
    drawerLayout.closeDrawers();
    return;
  }

  protected void createClearCacheDialog(final Context context) {
    AlertDialog.Builder alertDialogBuilder =
        new AlertDialog.Builder(context, android.R.style.Theme_Holo_Dialog);

    alertDialogBuilder.setTitle(R.string.options_main_clear_image_cache);
    alertDialogBuilder.setMessage(R.string.option_clear_the_image_cache_)
        .setCancelable(true)
        .setPositiveButton(R.string.clear, new DialogInterface.OnClickListener() {

          @Override public void onClick(DialogInterface dialog, int which) {

            Glide.get(context).clearDiskCache();
            Glide.get(context).clearMemory();
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

          @Override public void onClick(DialogInterface dialog, int which) {

          }
        });

    alertDialogBuilder.create();
    alertDialogBuilder.show();
  }
}
