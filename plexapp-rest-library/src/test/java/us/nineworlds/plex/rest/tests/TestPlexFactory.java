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

import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import us.nineworlds.plex.rest.PlexappFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.plex.rest.tests.utils.NanoHTTPD;
import us.nineworlds.serenity.common.media.model.IDirectory;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.media.model.ITrack;
import us.nineworlds.serenity.common.rest.SerenityClient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author dcarver
 */
public class TestPlexFactory {

  NanoHTTPD server = null;
  IConfiguration config = null;

  @Before public void setUp() throws Exception {
    config = new MockConfig();
    //URL url = this.getClass().getResource("/");

    File rootfile = new File("src/test/resources");
    server = new NanoHTTPD(Integer.parseInt(config.getPort()), rootfile);
  }

  @After public void tearDown() throws Exception {
    server.stop();
  }

  @Test public void testRetrieveLibrary() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveLibrary();
    assertNotNull(mediaContainer);
    assertEquals(3, mediaContainer.getSize());
    assertEquals(3, mediaContainer.getDirectories().size());
  }

  @Test public void testRetrieveRoot() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveRootData();
    assertNotNull(mediaContainer);
    assertEquals(10, mediaContainer.getSize());
  }

  @Test public void testRetrieveSections() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSections();
    assertNotNull(mediaContainer);
    assertEquals(2, mediaContainer.getSize());
  }

  @Test public void testRetrieveSectionByKeyMovies() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSections("4");
    assertNotNull(mediaContainer);
    assertEquals(19, mediaContainer.getSize());
  }

  @Test public void testRetrieveSectionByKeyTVShows() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSections("6");
    assertNotNull(mediaContainer);
    assertEquals(15, mediaContainer.getSize());
  }

  @Test public void testRetrieveSectionByKeyMusic() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSections("3");
    assertNotNull(mediaContainer);
    assertEquals(11, mediaContainer.getSize());
  }

  @Test public void testRetrieveAllTVShows() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSections("6", "all");
    List<IDirectory> directories = mediaContainer.getDirectories();
    assertEquals(6, directories.size());
  }

  @Test public void testRetrieveAllMusic() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSections("3", "all");
    List<IDirectory> directories = mediaContainer.getDirectories();
    assertEquals(4, directories.size());
  }

  @Test public void testRetrieveSeasonsForTVShow() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSeasons("/library/metadata/209/children/");
    assertEquals(5, mediaContainer.getSize());
  }

  @Test public void testRetrieveMusicMetaData() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSeasons("/library/metadata/101/children/");
    assertEquals(1, mediaContainer.getSize());
  }

  @Test public void testRetrieveMusicTrackMetaData() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSeasons("/library/metadata/102/children/");
    assertEquals(21, mediaContainer.getSize());
    assertNotNull(mediaContainer.getTracks());
    ITrack track = mediaContainer.getTracks().get(0);
    assertNotNull(track);
    assertEquals("Arabian Nights", track.getTitle());
  }

  @Test public void testRetrieveSeasonsForTVShowBackgroundArt() throws Exception {
    SerenityClient factory = PlexappFactory.getInstance(config);
    IMediaContainer mediaContainer = factory.retrieveSeasons("/library/metadata/209/children/");
    assertEquals("/library/metadata/209/art/1354460830", mediaContainer.getArt());
  }

  private class MockConfig implements IConfiguration {

    @Override public String getHost() {
      return "localhost";
    }

    @Override public void setHost(String host) {

    }

    @Override public String getPort() {
      return "33400";
    }

    @Override public void setPort(String port) {

    }
  }
}
