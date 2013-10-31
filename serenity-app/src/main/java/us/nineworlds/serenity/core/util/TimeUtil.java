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

package us.nineworlds.serenity.core.util;

import java.util.concurrent.TimeUnit;

/**
 * @author dcarver
 *
 */
public class TimeUtil {

	private static final int MILLISECONDS_PER_MINUTE = 60000;
	private static final int MILLISECONDS_PER_HOUR = 3600000;
	
	/**
	 * Return a formated duration in hh:mm:ss format.
	 * 
	 * @param duration
	 *            number of milliseconds that have passed.
	 * @return formatted string
	 */
	public static String formatDuration(long duration) {
		long tempdur = duration;
		long hours = TimeUnit.MILLISECONDS.toHours(duration);

		tempdur = tempdur - (hours * MILLISECONDS_PER_HOUR);

		long minutes = tempdur / MILLISECONDS_PER_MINUTE;
		tempdur = tempdur - (minutes * MILLISECONDS_PER_MINUTE);

		long seconds = tempdur / 1000;
		if (hours > 0) {
			return String.format("%2d:%02d:%02d", hours, minutes, seconds);
		}	
		return String.format("%02d:%02d", minutes, seconds);
	}
	
	public static String formatDurationHoursMinutes(long duration) {
		long tempdur = duration;
		long hours = TimeUnit.MILLISECONDS.toHours(duration);

		tempdur = tempdur - (hours * MILLISECONDS_PER_HOUR);

		long minutes = tempdur / MILLISECONDS_PER_MINUTE;
		tempdur = tempdur - (minutes * MILLISECONDS_PER_MINUTE);

		if (hours > 0) {
			return String.format("%2dH %02dM", hours, minutes);
		}	
		return String.format("%02dM", minutes);
	}
}
