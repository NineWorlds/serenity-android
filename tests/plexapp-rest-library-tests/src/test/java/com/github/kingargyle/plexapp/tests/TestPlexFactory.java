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

package com.github.kingargyle.plexapp.tests;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;

import static org.junit.Assert.*;

import com.github.kingargyle.plexapp.tests.utils.NanoHTTPD;

/**
 * @author dcarver
 *
 */
public class TestPlexFactory {
	
	NanoHTTPD server = null;
	IConfiguration config = null;
	
	@Before
	public void setUp() throws Exception {
		config = new MockConfig();
		URL url = this.getClass().getResource("/");
		File rootfile = new File(url.getPath());
		server = new NanoHTTPD(Integer.parseInt(config.getPort()), rootfile);
	}
	
	@After
	public void tearDown() throws Exception {
		server.stop();
	}
	
	@Test
	public void testRetrieveLibrary() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveLibrary();
		assertNotNull(mediaContainer);
		assertEquals(3, mediaContainer.getSize());
		assertEquals(3, mediaContainer.getDirectories().size());
	}
	
	@Test
	public void testRetrieveRoot() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveRootData();
		assertNotNull(mediaContainer);
		assertEquals(10, mediaContainer.getSize());
	}
	
	@Test
	public void testRetrieveSections() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveSections();
		assertNotNull(mediaContainer);
		assertEquals(2, mediaContainer.getSize());
	}
	
	@Test
	public void testRetrieveSectionByKeyMissing() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		try {
			factory.retrieveSections("5");
			fail("Should not get to this point");
		} catch (Exception ex) {
			
		}		
	}
	
	@Test
	public void testRetrieveSectionByKeyMovies() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveSections("4");
		assertNotNull(mediaContainer);
		assertEquals(19, mediaContainer.getSize());
	}
	
	@Test
	public void testRetrieveSectionByKeyTVShows() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveSections("6");
		assertNotNull(mediaContainer);
		assertEquals(15, mediaContainer.getSize());
	}
	
	@Test
	public void testRetrieveAllTVShows() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveSections("6", "all");
		List<Directory> directories = mediaContainer.getDirectories();
		assertEquals(6, directories.size());
	}
	
	@Test
	public void testRetrieveSeasonsForTVShow() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveSeasons("/library/metadata/209/children/");
		assertEquals(5, mediaContainer.getSize());
	}

	@Test
	public void testRetrieveSeasonsForTVShowBackgroundArt() throws Exception {
		PlexappFactory factory = PlexappFactory.getInstance(config);
		MediaContainer mediaContainer = factory.retrieveSeasons("/library/metadata/209/children/");
		assertEquals("/library/metadata/209/art/1354460830",mediaContainer.getArt());
	}
	
	
	
	private class MockConfig implements IConfiguration {

		/* (non-Javadoc)
		 * @see com.github.kingargyle.plexapp.config.IConfiguration#getHost()
		 */
		public String getHost() {
			return "localhost";
		}

		/* (non-Javadoc)
		 * @see com.github.kingargyle.plexapp.config.IConfiguration#setHost(java.lang.String)
		 */
		public void setHost(String host) {
			
		}

		/* (non-Javadoc)
		 * @see com.github.kingargyle.plexapp.config.IConfiguration#getPort()
		 */
		public String getPort() {
			return "32400";
		}

		/* (non-Javadoc)
		 * @see com.github.kingargyle.plexapp.config.IConfiguration#setPort(java.lang.String)
		 */
		public void setPort(String port) {
			
		}
	}
}
