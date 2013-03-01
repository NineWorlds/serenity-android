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

package us.nineworlds.plex.rest.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.nineworlds.plex.rest.ResourcePaths;
import us.nineworlds.plex.rest.config.IConfiguration;


import static org.junit.Assert.*;

/**
 * @author dcarver
 *
 */
public class TestResourcePaths {

	private IConfiguration config;
	private ResourcePaths path;
	@Before
	public void setUp() throws Exception {
		
		config = new MockConfiguration();
		path = new ResourcePaths(config);
	}
	
	@After
	public void tearDown() throws Exception {
		config = null;
		path = null;
		
	}
	
	@Test
	public void testLibraryPath() throws Exception {
		String result = path.getLibraryURL();
		
		assertEquals("http://localhost:32400/library/", result);
	}
	
	@Test
	public void testSectionsPath() throws Exception {
		String result = path.getSectionsURL();
		
		assertEquals("http://localhost:32400/library/sections/", result);
	}
	
	@Test
	public void testSectionPathWithKey() throws Exception {
		String result = path.getSectionsURL("4");
		
		assertEquals("http://localhost:32400/library/sections/4/", result);
	}
	
	@Test
	public void testSectionPathWithKeyTVShows() throws Exception {
		String result = path.getSectionsURL("6");
		
		assertEquals("http://localhost:32400/library/sections/6/", result);
	}
	
	@Test
	public void testWatched() throws Exception {
		String result = path.getWatchedUrl("131");
		assertEquals("http://localhost:32400/:/scrobble?key=131&identifier=com.plexapp.plugins.library", result);
	}

	@Test
	public void testUnwatched() throws Exception {
		String result = path.getUnwatchedUrl("131");
		assertEquals("http://localhost:32400/:/unscrobble?key=131&identifier=com.plexapp.plugins.library", result);
	}


	
	private class MockConfiguration implements IConfiguration {

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
