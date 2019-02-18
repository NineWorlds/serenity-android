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

package us.nineworlds.serenity.core.services;

import android.app.Notification;

import androidx.test.core.app.ApplicationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.TestingModule;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo;
import us.nineworlds.serenity.test.InjectingTest;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.assertj.android.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 25)
public class RecommendAsyncTaskTest extends InjectingTest {

	RecommendAsyncTask recommendTask;

	@Test
	public void createsValidNotificationForSeries() {
		VideoContentInfo video = mock(VideoContentInfo.class);
		when(video.id()).thenReturn("1");
		when(video.getTitle()).thenReturn("Show Title");
		when(video.getSeriesTitle()).thenReturn("Series Title");
		when(video.getSeason()).thenReturn("Season 1");
		when(video.getEpisode()).thenReturn("Episode 1");
		when(video.getImageURL()).thenReturn("http://www.example.com/");
		when(video.getBackgroundURL()).thenReturn("http://www.example.com/");

		recommendTask = new RecommendAsyncTask(video, getApplicationContext());

		Notification notification = (Notification) recommendTask.doInBackground();
		assertThat(notification).isNotNull().hasPriority(Notification.PRIORITY_LOW);
	}

	@Test
	public void createsValidNotificationForMovies() {
		VideoContentInfo video = mock(VideoContentInfo.class);
		when(video.id()).thenReturn("1");
		when(video.getTitle()).thenReturn("Spiderman");
		when(video.getSummary()).thenReturn("This is some description.");
		when(video.getImageURL()).thenReturn("http://www.example.com/");
		when(video.getBackgroundURL()).thenReturn("http://www.example.com/");

		recommendTask = new RecommendAsyncTask(video, getApplicationContext());

		Notification notification = (Notification) recommendTask
				.doInBackground();

		assertThat(notification).isNotNull().hasPriority(
				Notification.PRIORITY_LOW);
	}

	@Test
	public void moviePriorityIsSetToMax() {
		VideoContentInfo video = mock(VideoContentInfo.class);
		when(video.id()).thenReturn("1");
		when(video.getTitle()).thenReturn("Spiderman");
		when(video.getSummary()).thenReturn("This is some description.");
		when(video.getImageURL()).thenReturn("http://www.example.com/");
		when(video.getBackgroundURL()).thenReturn("http://www.example.com/");
		when(video.viewedPercentage()).thenReturn(0.81f);
		recommendTask = new RecommendAsyncTask(video, getApplicationContext());

		Notification notification = (Notification) recommendTask
				.doInBackground();

		assertThat(notification).isNotNull().hasPriority(
				Notification.PRIORITY_MAX);
	}

	@Test
	public void moviePriorityIsSetToHigh() {
		VideoContentInfo video = mock(VideoContentInfo.class);
		when(video.id()).thenReturn("1");
		when(video.getTitle()).thenReturn("Spiderman");
		when(video.getSummary()).thenReturn("This is some description.");
		when(video.getImageURL()).thenReturn("http://www.example.com/");
		when(video.getBackgroundURL()).thenReturn("http://www.example.com/");
		when(video.viewedPercentage()).thenReturn(0.45f);

		recommendTask = new RecommendAsyncTask(video, getApplicationContext());

		Notification notification = (Notification) recommendTask
				.doInBackground();

		assertThat(notification).isNotNull().hasPriority(
				Notification.PRIORITY_HIGH);

	}

	@Test
	public void moviePriorityIsSetToDefault() {
		VideoContentInfo video = mock(VideoContentInfo.class);
		when(video.id()).thenReturn("1");
		when(video.getTitle()).thenReturn("Spiderman");
		when(video.getSummary()).thenReturn("This is some description.");
		when(video.getImageURL()).thenReturn("http://www.example.com/");
		when(video.getBackgroundURL()).thenReturn("http://www.example.com/");
		when(video.viewedPercentage()).thenReturn(0.11f);
		recommendTask = new RecommendAsyncTask(video, getApplicationContext());

		Notification notification = (Notification) recommendTask
				.doInBackground();

		assertThat(notification).isNotNull().hasPriority(
				Notification.PRIORITY_DEFAULT);
	}

	@Test
	public void notificationHasPendingIntent() {
		MoviePosterInfo moviePoster = new MoviePosterInfo();
		moviePoster.setId("1");
		moviePoster.setTitle("Spiderman");
		moviePoster.setSummary("A simple Summary");
		moviePoster.setImageURL("http://www.example.com");
		moviePoster.setBackgroundURL("http://www.example.com");
		recommendTask = new RecommendAsyncTask(moviePoster,
				getApplicationContext());

		Notification notification = (Notification) recommendTask
				.doInBackground();

		assertThat(notification.contentIntent).isNotNull();
	}

	@Override public void installTestModules() {
		scope.installTestModules(new TestingModule());
	}
}
