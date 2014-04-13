package us.nineworlds.serenity.core.externalplayer;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import us.nineworlds.serenity.core.externalplayer.ExternalPlayer;
import us.nineworlds.serenity.core.externalplayer.ExternalPlayerFactory;
import us.nineworlds.serenity.core.externalplayer.MXPlayer;
import us.nineworlds.serenity.core.externalplayer.MXPlayerPro;
import us.nineworlds.serenity.core.externalplayer.ViMuPlayer;

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

/**
 * @author dcarver
 * 
 */
public class ExternalPlayerFactoryTest {

	private ExternalPlayerFactory player;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		player = new ExternalPlayerFactory(null, null);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		player = null;
	}

	/**
	 * Test method for
	 * {@link us.nineworlds.serenity.core.externalplayer.ExternalPlayerFactory#createExternalPlayer(java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateExternalPlayer() {
		assertThat(player).isNotNull();
	}

	@Test
	public void testCreateMXPlayer() {
		ExternalPlayer p = player.createExternalPlayer("mxplayer");
		assertThat(p).isNotNull().isInstanceOf(MXPlayer.class);
	}

	@Test
	public void testCreateMXPlayerPro() {
		ExternalPlayer p = player.createExternalPlayer("mxplayerpro");
		assertThat(p).isNotNull().isInstanceOf(MXPlayerPro.class);
	}

	@Test
	public void testCreateVimu() {
		ExternalPlayer p = player.createExternalPlayer("vimu");
		assertThat(p).isNotNull().isInstanceOf(ViMuPlayer.class);
	}

}
