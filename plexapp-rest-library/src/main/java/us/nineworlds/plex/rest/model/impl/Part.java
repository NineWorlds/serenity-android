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

package us.nineworlds.plex.rest.model.impl;

import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import us.nineworlds.serenity.common.media.model.IPart;
import us.nineworlds.serenity.common.media.model.IStream;

@Root(name = "Part")
public class Part implements IPart {

  @Attribute(name = "key", required = true) private String key;

  @Attribute(name = "file", required = false) private String filename;

  @Attribute(name = "container", required = false) private String container;

  @ElementList(inline = true, required = false, type = Stream.class) private List<IStream> streams;

  @Override public List<IStream> getStreams() {
    return streams;
  }

  @Override public void setStreams(List<IStream> streams) {
    this.streams = streams;
  }

  @Override public String getContainer() {
    return container;
  }

  @Override public void setContainer(String container) {
    this.container = container;
  }

  @Override public String getKey() {
    return key;
  }

  @Override public void setKey(String key) {
    this.key = key;
  }

  @Override public String getFilename() {
    return filename;
  }

  @Override public void setFilename(String filename) {
    this.filename = filename;
  }
}
