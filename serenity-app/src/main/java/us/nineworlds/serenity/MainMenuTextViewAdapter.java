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
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package us.nineworlds.serenity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import net.ganin.darv.DpadAwareRecyclerView;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer;
import us.nineworlds.serenity.injection.InjectingBaseAdapter;
import us.nineworlds.serenity.ui.views.MainMenuTextView;
import us.nineworlds.serenity.volley.DefaultLoggingVolleyErrorListener;
import us.nineworlds.serenity.volley.VolleyUtils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class MainMenuTextViewAdapter extends InjectingRecyclerViewAdapter {

	/** The parent context */
	private final Context myContext;

	public static List<MenuItem> menuItems = new ArrayList<MenuItem>();

	/** Simple Constructor saving the 'parent' context. */
	public MainMenuTextViewAdapter(Context c) {
		super();
		myContext = c;
	}


	@Override
	public int getItemCount() {
		return menuItems.size();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		FrameLayout mainMenuTextView = (FrameLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainmenu, parent, false);
		return new MainMenuViewHolder(mainMenuTextView);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		MenuItem menuItem = menuItems.get(position);

		MainMenuViewHolder mainMenuViewHolder = (MainMenuViewHolder) holder;
		//createView(mainMenuViewHolder.mainMenuTextView, menuItem);
		setDefaults(menuItem.getTitle(), mainMenuViewHolder.mainMenuTextView);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	/**
	 * Create a Main Menu item view for the corresponding MenuItem. If an
	 * appropriate type can not be found a default MainMenuTextView will be
	 * created.
	 *
	 * @param menuItem
	 * @return
	 */
	protected void createView(MainMenuTextView mainMenuTextView, MenuItem menuItem) {
		if ("movie".equals(menuItem.getType())) {
			mainMenuTextView.setBackgroundId(R.drawable.movies);
			mainMenuTextView.setLibraryKey(menuItem.getSection());
			mainMenuTextView.setActivityType(menuItem.getType());
			return;
		}

		if ("show".equals(menuItem.getType())) {
			mainMenuTextView.setBackgroundId(R.drawable.tvshows);
			mainMenuTextView.setLibraryKey(menuItem.getSection());
			mainMenuTextView.setActivityType(menuItem.getType());
			return;
		}

		if ("artist".equals(menuItem.getType())) {
			mainMenuTextView.setBackgroundId(R.drawable.music);
			mainMenuTextView.setLibraryKey(menuItem.getSection());
			mainMenuTextView.setActivityType(menuItem.getType());
			return;
		}

		if ("settings".equals(menuItem.getType())) {
			mainMenuTextView.setBackgroundId(R.drawable.settings);
			mainMenuTextView.setLibraryKey("0");
			mainMenuTextView.setActivityType(menuItem.getType());
			return;
		}

		if ("options".equals(menuItem.getType())) {
			mainMenuTextView.setBackgroundId(R.drawable.settings);
			mainMenuTextView.setLibraryKey("0");
			mainMenuTextView.setActivityType(menuItem.getType());
			return;
		}

		if ("search".equals(menuItem.getType())) {
			mainMenuTextView.setBackgroundId(R.drawable.search);
			mainMenuTextView.setLibraryKey("0");
			mainMenuTextView.setActivityType(menuItem.getType());
			return;
		}

		mainMenuTextView.setBackgroundId(R.drawable.serenity_bonsai_logo);
	}

	void setDefaults(String title, TextView v) {
		v.setText(title);
		v.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
		v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		v.setGravity(Gravity.CENTER_VERTICAL);
		v.setLines(1);
		v.setHorizontallyScrolling(true);
		v.setEllipsize(TruncateAt.MARQUEE);
		v.setLayoutParams(new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public MenuItem getItemAtPosition(int position) {
		if (position > menuItems.size()) {
			return null;
		}
		return menuItems.get(position);
	}

	public class MainMenuViewHolder extends RecyclerView.ViewHolder {

		public TextView mainMenuTextView;

		public MainMenuViewHolder(View itemView) {
			super(itemView);
			mainMenuTextView = (TextView) itemView.findViewById(R.id.main_menu_item);
		}
	}
}
