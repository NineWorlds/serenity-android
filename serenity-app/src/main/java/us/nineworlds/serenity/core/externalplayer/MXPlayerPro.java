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

import us.nineworlds.serenity.core.model.VideoContentInfo;
import android.app.Activity;
import android.content.Intent;

/**
 * @author dcarver
 *
 */
public class MXPlayerPro extends MXPlayer {

	private static final String PLAYER_CLASS_NAME = "com.mxtech.videoplayer.ActivityScreen";
	private static final String PLAYER_PACKAGENAME = "com.mxtech.videoplayer.pro";

	public MXPlayerPro(VideoContentInfo vc, Activity ac) {
		super(vc, ac);
	}
	
	@Override
	protected void setClassAndPackagename(Intent vpIntent) {
		vpIntent.setPackage(PLAYER_PACKAGENAME);
		vpIntent.setClassName(PLAYER_PACKAGENAME, PLAYER_CLASS_NAME);
	}
}
