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

/**
 * @author dcarver
 *
 */
public class ViMuPlayer extends AbstractExternalPlayer implements ExternalPlayer{

	/**
	 * @param vc
	 * @param ac
	 */
	public ViMuPlayer(VideoContentInfo vc, Activity ac) {
		super(vc, ac);
	}

	@Override
	public void launch() {
		Intent vpIntent = createIntent();
		
		vpIntent.putExtra("forcename", videoContent.getTitle());
		vpIntent.putExtra("forcedirect", true);
		vpIntent.putExtra("startfrom", videoContent.getResumeOffset());
		if (videoContent.getSubtitle() != null ) {
			Subtitle subtitle = videoContent.getSubtitle();
			if (!"none".equals(subtitle.getFormat())) {
				vpIntent.putExtra("forcedsrt", subtitle.getKey());
			}			
		}
		
		setClassAndPackagename(vpIntent);		
		launchActivity(vpIntent);
	}

	@Override
	public boolean supportsResume() {
		return true;
	}

	@Override
	public boolean supportsPlaybackPosition() {
		return false;
	}

	@Override
	public boolean supportsSubtitleUrls() {
		return true;
	}

	@Override
	public boolean hasTitleSupport() {
		return true;
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.externalplayer.ExternalPlayer#hasHardwareDecodingSupport()
	 */
	@Override
	public boolean hasHardwareDecodingSupport() {
		return true;
	}

	@Override
	public void enableHardwareDecodinging() {
		
	}

	@Override
	public void disableHadwareDecoding() {
		
	}

	@Override
	protected void setClassAndPackagename(Intent vpIntent) {
		vpIntent.setPackage("net.gtvbox.videoplayer");
		vpIntent.setClassName("net.gtvbox.videoplayer", "net.gtvbox.videoplayer.PlayerActivity");
	}

}
