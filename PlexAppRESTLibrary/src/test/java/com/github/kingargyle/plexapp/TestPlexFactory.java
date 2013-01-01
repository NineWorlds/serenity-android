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

package com.github.kingargyle.plexapp;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.github.kingargyle.plexapp.config.IConfiguration;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexapp.utils.NanoHTTPD;

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
		PlexappFactory factory = new PlexappFactory(config);
		MediaContainer mediaContainer = factory.retrieveLibrary();
		assertNotNull(mediaContainer);
		assertEquals(3, mediaContainer.getSize());
		assertEquals(3, mediaContainer.getDirectories().size());
	}
	
	@Test
	public void testRetrieveRoot() throws Exception {
		PlexappFactory factory = new PlexappFactory(config);
		MediaContainer mediaContainer = factory.retrieveRootData();
		assertNotNull(mediaContainer);
		assertEquals(10, mediaContainer.getSize());
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
