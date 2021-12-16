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

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.injection.InjectingRecyclerViewAdapter;

import static butterknife.ButterKnife.bind;

public class MainMenuTextViewAdapter extends InjectingRecyclerViewAdapter {

  public static List<MenuItem> menuItems = new ArrayList<>();

  private GalleryOnItemSelectedListener onItemSelectedListener;
  private GalleryOnItemClickListener onItemClickListener;
  public MainMenuTextViewAdapter() {
    super();
    onItemSelectedListener = new GalleryOnItemSelectedListener(this);
    onItemClickListener = new GalleryOnItemClickListener(this);

  }

  @Override public int getItemCount() {
    return menuItems.size();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    FrameLayout mainMenuTextView =
        (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainmenu, parent, false);
    return new MainMenuViewHolder(mainMenuTextView);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    MenuItem menuItem = menuItems.get(position);

    MainMenuViewHolder mainMenuViewHolder = (MainMenuViewHolder) holder;
    setDefaults(menuItem.getTitle(), mainMenuViewHolder.mainMenuTextView, position);
    mainMenuViewHolder.itemView.setOnFocusChangeListener((view, hasFocus) -> onItemSelectedListener.onItemSelected(view, hasFocus, position));
    mainMenuViewHolder.itemView.setOnClickListener((view) -> onItemClickListener.onItemClick(view, position));
  }

  @Override public long getItemId(int position) {
    return position;
  }

  void setDefaults(String title, TextView v, int position) {
    v.setText(title);
    //v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
    //v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
    //v.setGravity(Gravity.CENTER_VERTICAL);
    v.setLines(1);
    v.setHorizontallyScrolling(true);
    v.setEllipsize(TruncateAt.MARQUEE);
    v.setLayoutParams(new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT));
  }

  public MenuItem getItemAtPosition(int position) {
    if (position > menuItems.size()) {
      return null;
    }

    if (position < 0) {
      position = 0;
    }
    return menuItems.get(position);
  }

  public void updateMenuItems(List<MenuItem> menuItems) {
    this.menuItems = menuItems;
    notifyDataSetChanged();
  }

  public class MainMenuViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_menu_item) public TextView mainMenuTextView;

    public MainMenuViewHolder(View itemView) {
      super(itemView);
      bind(this, itemView);
      itemView.setFocusable(true);
      itemView.setFocusableInTouchMode(true);
    }
  }
}
