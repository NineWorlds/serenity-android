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

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.ui.preferences.LeanbackSettingsActivity;

public class GalleryOnItemClickListener {

  private static final String MENU_TYPE_SEARCH = "search";
  private static final String MENU_TYPE_MUSIC = "artist";
  private static final String MENU_TYPE_OPTIONS = "options";

  private MainMenuTextViewAdapter adapter;

  public GalleryOnItemClickListener(MainMenuTextViewAdapter adapter) {
    this.adapter = adapter;
  }

  public void onItemClick(View view, int i) {
    Activity context = (Activity) view.getContext();
    if (context.isDestroyed()) {
      return;
    }

    MenuItem menuItem = adapter.getItemAtPosition(i);
    String librarySection = menuItem.getSection();
    String activityType = menuItem.getType();

    if (MENU_TYPE_SEARCH.equalsIgnoreCase(activityType)) {
      context.onSearchRequested();
      return;
    }

    if (MENU_TYPE_OPTIONS.equalsIgnoreCase(activityType)) {
      context.openOptionsMenu();
      return;
    }

    Intent intent;

    if (MENU_TYPE_MUSIC.equalsIgnoreCase(activityType)) {
      Toast.makeText(context, "Music support has been removed.", Toast.LENGTH_LONG);
      return;
    }

    intent = new Intent(context, LeanbackSettingsActivity.class);
    intent.putExtra("key", librarySection);
    context.startActivityForResult(intent, 0);
  }
}
