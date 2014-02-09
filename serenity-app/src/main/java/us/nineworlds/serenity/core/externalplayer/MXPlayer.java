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
import us.nineworlds.serenity.core.model.impl.Subtitle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * @author dcarver
 *
 */
public class MXPlayer extends AbstractExternalPlayer implements ExternalPlayer {

	private static final String PLAYER_CLASS_NAME = "com.mxtech.videoplayer.ad.ActivityScreen";
	private static final String PLAYER_PACKAGE_NAME = "com.mxtech.videoplayer.ad";
	
	private boolean hardwareDecoding;
	
	public MXPlayer(VideoContentInfo vc, Activity ac) {
		super(vc, ac);
	}
	
	
	
	@Override
	public void launch() {
		Intent vpIntent = populateIntentValues();
		setClassAndPackagename(vpIntent);
		launchActivity(vpIntent);				
	}


	@Override
	protected void setClassAndPackagename(Intent vpIntent) {
		vpIntent.setPackage(PLAYER_PACKAGE_NAME);
		vpIntent.setClassName(PLAYER_PACKAGE_NAME, PLAYER_CLASS_NAME);
	}

	/**
	 * @return
	 */
	protected Intent populateIntentValues() {
		Intent vpIntent = createIntent();
		if (hardwareDecoding) {
			vpIntent.putExtra("decode_mode", 1);
		}
		vpIntent.putExtra("title", videoContent.getTitle());
		vpIntent.putExtra("return_result", true);
		if (videoContent.getSubtitle() != null ) {
			Subtitle subtitle = videoContent.getSubtitle();
			if (!"none".equals(subtitle.getFormat())) {
				Uri[] subt = { Uri.parse(subtitle.getKey()) };
				vpIntent.putExtra("subs", subt);
				vpIntent.putExtra("subs.enable", subt);
			}
		}
		if (videoContent.getResumeOffset() != 0 && videoContent.isPartiallyWatched()) {
			vpIntent.putExtra("position", videoContent.getResumeOffset());
		}
		return vpIntent;
	}


	@Override
	public boolean supportsResume() {
		return true;
	}

	@Override
	public boolean supportsPlaybackPosition() {
		return true;
	}

	@Override
	public boolean supportsSubtitleUrls() {
		return true;
	}

	@Override
	public boolean hasTitleSupport() {
		return true;
	}

	@Override
	public boolean hasHardwareDecodingSupport() {
		return true;
	}

	@Override
	public void enableHardwareDecodinging() {
		hardwareDecoding = true;
	}

	@Override
	public void disableHadwareDecoding() {
		hardwareDecoding = false;
		
	}

}
