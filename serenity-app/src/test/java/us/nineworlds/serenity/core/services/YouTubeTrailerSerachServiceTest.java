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

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import us.nineworlds.serenity.SerenityRobolectricTestRunner;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import android.content.Intent;

/**
 * @author dcarver
 * 
 */
@RunWith(SerenityRobolectricTestRunner.class)
public class YouTubeTrailerSerachServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link us.nineworlds.serenity.core.services.GDMService#onHandleIntent(android.content.Intent)}
	 * .
	 */
	@Test
	public void testOnHandleIntentIntent() throws Exception {

		MockYouTubeSearchService service = new MockYouTubeSearchService();
		Intent intent = new Intent();
		intent.putExtra("videoTitle", "Die Hard");
		intent.putExtra("year", "1988");
		service.onHandleIntent(intent);
		assertThat(service.getYouTubeVideoInfo()).isNotNull();
		assertThat(service.getYouTubeVideoInfo().id()).isNotNull();
	}

	public class MockYouTubeSearchService extends
			YouTubeTrailerSearchIntentService {

		@Override
		public void onHandleIntent(Intent intent) {
			super.onHandleIntent(intent);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * us.nineworlds.serenity.core.services.YouTubeTrailerSearchIntentService
		 * #sendMessageResults(android.content.Intent)
		 */
		@Override
		public void sendMessageResults(Intent intent) {

		}

		public VideoContentInfo getYouTubeVideoInfo() {
			return videoInfo;
		}

	}

}
