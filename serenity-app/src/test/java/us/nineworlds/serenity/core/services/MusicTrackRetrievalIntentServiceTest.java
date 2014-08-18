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

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.core.model.impl.AudioTrackContentInfo;
import android.content.Intent;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class MusicTrackRetrievalIntentServiceTest {

	MockMusicTrackRetrievalIntentService service;

	@Before
	public void setUp() {
		service = new MockMusicTrackRetrievalIntentService();
	}

	@Test
	public void assertThatMusicTrackContentIsEmptyWhenBundleExtrasIsNull() {
		Intent intent = Mockito.mock(Intent.class);
		service.onHandleIntent(intent);
		assertThat(service.getTracks()).isEmpty();
	}

	protected class MockMusicTrackRetrievalIntentService extends
			MusicTrackRetrievalIntentService {

		@Override
		public void onHandleIntent(Intent intent) {
			super.onHandleIntent(intent);
		}

		public List<AudioTrackContentInfo> getTracks() {
			return musicContentList;
		}
	}

}
