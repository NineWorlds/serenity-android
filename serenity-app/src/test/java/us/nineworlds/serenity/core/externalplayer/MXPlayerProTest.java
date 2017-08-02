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

package us.nineworlds.serenity.core.externalplayer;

import android.app.Activity;
import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import us.nineworlds.serenity.BuildConfig;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.Subtitle;

import static org.assertj.android.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MXPlayerProTest {

	MXPlayerPro mxplayer;

	@Mock
	VideoContentInfo videoInfo;

	@Mock
	Subtitle subtitle;

	@Mock
	Activity activity;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(videoInfo.getDirectPlayUrl()).thenReturn("http://www.example.com");
		doNothing().when(activity).startActivityForResult(any(Intent.class),
				anyInt());
		mxplayer = new MXPlayerPro(videoInfo, activity);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void createsExpectedIntentExtraTitle() {
		Intent intent = mxplayer.createIntent();
		assertThat(intent).hasExtra("title");
	}

	@Test
	public void hasDecodeMode() {
		mxplayer.enableHardwareDecodinging();
		Intent intent = mxplayer.createIntent();
		assertThat(intent).hasExtra("decode_mode");
	}

	@Test
	public void hadReturnResult() {
		Intent intent = mxplayer.createIntent();
		assertThat(intent).hasExtra("return_result");
	}

	@Test
	public void hasPositionWhenVideoIsPartiallyWatched() {
		when(videoInfo.getResumeOffset()).thenReturn(10);
		when(videoInfo.isPartiallyWatched()).thenReturn(true);
		Intent intent = mxplayer.createIntent();
		assertThat(intent).hasExtra("position");
	}

	@Test
	public void subtitleSupportIsEnabled() {
		when(subtitle.getFormat()).thenReturn("srt");
		when(subtitle.getKey()).thenReturn("http://www.example.com/");
		when(videoInfo.getSubtitle()).thenReturn(subtitle);

		Intent intent = mxplayer.createIntent();
		assertThat(intent).hasExtra("subs");
		assertThat(intent).hasExtra("subs.enable");
	}

	@Test
	public void supportsResume() {
		assertThat(mxplayer.supportsResume()).isTrue();
	}

	@Test
	public void supportsPlaybackPosition() {
		assertThat(mxplayer.supportsPlaybackPosition()).isTrue();
	}

	@Test
	public void supportsSubtitleUrls() {
		assertThat(mxplayer.supportsSubtitleUrls()).isTrue();
	}

	@Test
	public void hasTitleSupport() {
		assertThat(mxplayer.hasTitleSupport()).isTrue();
	}

	@Test
	public void hasHardwareDecoding() {
		assertThat(mxplayer.hasHardwareDecodingSupport()).isTrue();
	}

	@Test
	public void disabledHardWareEncoding() {
		mxplayer.enableHardwareDecodinging();
		mxplayer.disableHadwareDecoding();
		Intent intent = mxplayer.createIntent();
		assertThat(intent.getIntExtra("decode_mode", 0)).isEqualTo(0);
	}

	@Test
	public void activityIsLaunched() {
		mxplayer.launch();
		verify(activity).startActivityForResult(any(Intent.class), anyInt());
	}

}
