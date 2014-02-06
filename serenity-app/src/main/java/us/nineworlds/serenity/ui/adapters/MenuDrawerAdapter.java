/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
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

package us.nineworlds.serenity.ui.adapters;

import java.util.List;

import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.R;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * @author dcarver
 * 
 */
public class MenuDrawerAdapter extends BaseAdapter {

	private static List<MenuDrawerItem> menuOptions;
	private LayoutInflater inflater;

	public MenuDrawerAdapter(Activity context, List<MenuDrawerItem> menuOptions) {
		MenuDrawerAdapter.menuOptions = menuOptions;
		inflater = context.getLayoutInflater();

	}
	
	@Override
	public int getCount() {
		return menuOptions.size();
	}

	@Override
	public Object getItem(int position) {
		return menuOptions.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TextView rowView;
		if (convertView instanceof TextView) {
			rowView = (TextView) convertView;
		} else {
			rowView = (TextView) inflater.inflate(R.layout.serenity_menudrawer_listview_textview, parent, false);
		}
		rowView.setBackgroundResource(R.drawable.album_list_view_selector);
		rowView.setText(menuOptions.get(position).getText());
		rowView.setGravity(Gravity.CENTER_VERTICAL);
		rowView.setCompoundDrawablesWithIntrinsicBounds(menuOptions.get(position).getImageResourceID(), 0, 0, 0);
		return rowView;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
