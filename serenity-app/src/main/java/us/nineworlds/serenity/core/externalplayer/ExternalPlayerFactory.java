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
import us.nineworlds.serenity.core.model.VideoContentInfo;

/**
 * A factory that handles returning the selected video
 * player.
 * 
 * @author dcarver
 *
 */
public class ExternalPlayerFactory {

	private VideoContentInfo videoContent;
	private Activity activity;
	
	/**
	 * 
	 */
	public ExternalPlayerFactory(VideoContentInfo vc, Activity ac) {
		videoContent = vc;
		activity = ac;
	}
	
	public ExternalPlayer createExternalPlayer(String identifier) {
		if ("mxplayer".equals(identifier)) {
			return new MXPlayer(videoContent, activity);
		}
		
		if ("mxplayerpro".equals(identifier)) {
			return new MXPlayerPro(videoContent, activity);
		}
		
		if ("vimu".equals(identifier)) {
			return new ViMuPlayer(videoContent, activity);
		}
		
		return new SystemDefaultPlayer(videoContent, activity);
	}
}
