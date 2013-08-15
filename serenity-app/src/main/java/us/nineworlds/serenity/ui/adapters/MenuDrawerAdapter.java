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

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
import us.nineworlds.serenity.core.services.MusicTrackRetrievalIntentService;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author dcarver
 * 
 */
public class MenuDrawerAdapter extends ArrayAdapter<String> {

	private static List<String> menuOptions;
	private Activity context;
	private String key;

	public MenuDrawerAdapter(Activity context, List<String> menuOptions) {
		super(context, R.layout.serenity_menudrawer_listview_textview, menuOptions);
		this.menuOptions = menuOptions;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView rowView = (TextView) inflater.inflate(R.layout.serenity_menudrawer_listview_textview, parent, false);
		rowView.setBackgroundResource(R.drawable.album_list_view_selector);
		rowView.setText(menuOptions.get(position));
		return rowView;
	}

}
