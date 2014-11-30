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

package us.nineworlds.serenity.fragments;

import us.nineworlds.serenity.GalleryOnItemClickListener;
import us.nineworlds.serenity.GalleryOnItemSelectedListener;
import us.nineworlds.serenity.MainMenuTextViewAdapter;
import us.nineworlds.serenity.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;

public class MainMenuFragment extends Fragment {

	Gallery mainGallery;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_menu_view, container);
		setupGallery(view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void setupGallery(View view) {
		mainGallery = (Gallery) view.findViewById(R.id.mainGalleryMenu);

		mainGallery.setAdapter(new MainMenuTextViewAdapter(getActivity()));
		mainGallery
		.setOnItemSelectedListener(new GalleryOnItemSelectedListener());
		mainGallery.setOnItemClickListener(new GalleryOnItemClickListener(
				getActivity()));
		mainGallery.setCallbackDuringFling(false);
		mainGallery.requestFocusFromTouch();
	}

	@Override
	public void onResume() {
		mainGallery.requestFocusFromTouch();
		super.onResume();
	}
}
