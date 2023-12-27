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

package us.nineworlds.serenity.ui.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.serenity.core.menus.MenuDrawerItem;
import us.nineworlds.serenity.core.menus.MenuDrawerItemImpl;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
public class MenuDrawerAdapterTest {

	MenuDrawerAdapter menuDrawerAdapter;

	@Before
	public void setUp() {
		List<MenuDrawerItem> menuDrawerItems = new ArrayList<MenuDrawerItem>();
		MenuDrawerItem menuDrawerItem = new MenuDrawerItemImpl("test");
		menuDrawerItem.setImageResourceID(0);
		menuDrawerItems.add(menuDrawerItem);

		Activity mockActivity = Robolectric.buildActivity(Activity.class)
				.create().get();

		menuDrawerAdapter = new MenuDrawerAdapter(mockActivity, menuDrawerItems);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void assertThatItemCountIsGreaterThanZero() {
		assertThat(menuDrawerAdapter.getCount()).isGreaterThan(0);
	}

	@Test
	public void getTheFirstMenuItem() {
		assertThat(menuDrawerAdapter.getItem(0)).isInstanceOf(
				MenuDrawerItem.class);
	}

	@Test
	public void viewReusesTextViewAndPopulatesItWithNewValues() {
		TextView textView = new TextView(application);
		textView.setText("test text");

		View newTextView = menuDrawerAdapter.getView(0, textView, null);
		assertThat(newTextView).isInstanceOf(TextView.class);
		TextView newTextView2 = (TextView) newTextView;
		assertThat(newTextView2.getText()).isEqualTo(textView.getText());
	}

	@Test
	public void createsNewViewAndPopulatesItWithValues() {
		FrameLayout frameLayout = new FrameLayout(application);
		View newView = menuDrawerAdapter.getView(0, frameLayout, null);
		TextView textView = (TextView) newView;
		assertThat(textView.getText()).isEqualTo("test");
	}

}
