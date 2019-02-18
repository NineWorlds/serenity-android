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

package us.nineworlds.serenity.core.util;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TimeUtilsTest {

	TimeUtil timeUtil;

	@Before
	public void setUp() {
		timeUtil = new TimeUtil();
	}

	@Test
	public void formatDurationFormatsHHMMSS() {
		long duration = 14400000l;
		assertThat(timeUtil.formatDuration(duration)).isEqualTo(" 4:00:00");
	}

	@Test
	public void formatDuartionFormatHHMMSSPopulatesMinutesAndSeconds()
			throws Exception {
		long duration = demandDuration("04:50:10");
		assertThat(timeUtil.formatDuration(duration)).isEqualTo(" 4:50:10");
	}

	@Test
	public void formatDuartionFormatHHMMSSPopulatesOnlySeconds()
			throws Exception {
		long duration = demandDuration("00:00:10");
		assertThat(timeUtil.formatDuration(duration)).isEqualTo("00:10");
	}

	@Test
	public void formatDuartionInHoursMinutes() throws Exception {
		long duration = demandDuration("01:53:10");
		assertThat(timeUtil.formatDurationHoursMinutes(duration)).isEqualTo(" 1H 53M");
	}

	@Test
	public void formatDuartionInMinutes() throws Exception {
		long duration = demandDuration("00:53:10");
		assertThat(timeUtil.formatDurationHoursMinutes(duration)).isEqualTo(
				"53M");
	}

	private long demandDuration(String formatedDuration) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date date = sdf.parse(formatedDuration);
		long duration = date.getTime();
		return duration;
	}

}
