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

package us.nineworlds.serenity.core.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.model.impl.Stream;
import us.nineworlds.serenity.SerenityApplication;

/**
 * @author dcarver
 * 
 */
public class SubtitleMediaContainer extends AbstractMediaContainer {

	/**
	 * @param mc
	 */
	public SubtitleMediaContainer(MediaContainer mc) {
		super(mc);
	}

	public List<Subtitle> createSubtitle() {
		PlexappFactory factory = SerenityApplication.getPlexFactory();
		List<Stream> streams = mc.getVideos().get(0).getMedias().get(0)
				.getVideoPart().get(0).getStreams();
		List<Subtitle> subtitles = new ArrayList<Subtitle>();
		if (streams == null) {
			return Collections.emptyList();
		}
		for (Stream stream : streams) {
			if ("srt".equals(stream.getFormat())
					|| "ass".equals(stream.getFormat())) {

				Subtitle subtitle = new Subtitle();
				subtitle = new Subtitle();
				subtitle.setFormat(stream.getFormat());
				subtitle.setLanguageCode(stream.getLanguageCode());
				if (stream.getKey() == null) {
					continue;
				}
				subtitle.setKey(factory.baseURL()
						+ stream.getKey().replaceFirst("/", ""));
				if (stream.getLanguage() == null) {
					subtitle.setDescription("Unknown (" + stream.getFormat()
							+ ")");
				} else {
					subtitle.setDescription(stream.getLanguage() + " ("
							+ stream.getFormat() + ")");
				}
				subtitles.add(subtitle);
			}
		}
		return subtitles;

	}

}
