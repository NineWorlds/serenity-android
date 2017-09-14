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

package us.nineworlds.plex.rest.tests.model.impl;

import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.common.media.model.IDirectory;
import us.nineworlds.serenity.common.media.model.IGenre;
import us.nineworlds.serenity.common.media.model.IMedia;
import us.nineworlds.serenity.common.media.model.IPart;
import us.nineworlds.serenity.common.media.model.IVideo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author dcarver
 */
public class TestMetaDataContainerDeserialization {

  private Serializer serializer;

  @Before public void setUp() throws Exception {
    serializer = new Persister();
  }

  @After public void tearDown() throws Exception {
    serializer = null;
  }

  @Test public void testSimpleDeserialization() throws Exception {
    File file = testFile("/samples/root.xml");
    MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);

    assertEquals(10, mediaContainer.getSize());
  }

  File testFile(String path) {
    File file = new File("src/test/resources" + path);
    return file;
  }

  @Test public void testLibraryDeserialization() throws Exception {
    File file = testFile("/samples/library.xml");
    MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);

    List<IDirectory> directories = mediaContainer.getDirectories();
    assertTrue(directories.size() > 0);
    assertEquals(3, directories.size());
  }

  @Test public void testMoviesDeserialization() throws Exception {
    File file = testFile("/samples/movies.xml");
    MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);

    List<IVideo> videos = mediaContainer.getVideos();
    assertEquals(70, videos.size());
  }

  @Test public void testSectionDeserialization() throws Exception {
    File file = testFile("/samples/sections.xml");
    MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);

    List<IDirectory> directories = mediaContainer.getDirectories();
    assertEquals(2, directories.size());
  }

  @Test public void testMovieSectionDeserialization() throws Exception {
    File file = testFile("/samples/moviesSection.xml");
    MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);

    List<IDirectory> directories = mediaContainer.getDirectories();
    assertTrue(directories.size() > 0);
    assertNull("Found unexpected location", directories.get(0).getLocations());
  }

  @Test public void testAllMoviesDeserialization() throws Exception {
    File file = testFile("/samples/allmovies.xml");
    MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);

    List<IVideo> videos = mediaContainer.getVideos();
    assertEquals(184, videos.size());

    IVideo video = videos.get(0);
    List<IGenre> genres = video.getGenres();
    assertEquals(2, genres.size());
  }

  @Test public void testMoviesPartDeserialization() throws Exception {
    File file = testFile("/samples/allmovies.xml");
    MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);
    List<IVideo> videos = mediaContainer.getVideos();
    IVideo video = videos.get(0);
    List<IMedia> medias = video.getMedias();
    assertFalse(medias.isEmpty());
    IMedia media = medias.get(0);
    List<IPart> parts = media.getVideoPart();
    assertNotNull(parts);
    assertTrue(parts.size() > 0);
    IPart vp = parts.get(0);
    assertEquals("/library/parts/132/file.avi", vp.getKey());
  }

  @Test public void testMalformedTVShows() {
    File file = testFile("/samples/issue9_tvshows_bad.xml");
    try {
      MediaContainer mediaContainer = serializer.read(MediaContainer.class, file, false);
      fail("Parsing was allowed to occur, check the file to make sure it is still bad.");
    } catch (Exception ex) {

    }
  }
}
