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

package com.github.kingargyle.plexapp.tests.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import com.github.kingargyle.plexapp.model.impl.Directory;

import static org.custommonkey.xmlunit.XMLAssert.*;

public class TestMediaContainerSerialization extends AbstractSerializationTest {
	
	@Test
	public void testSimpleMediaContainer() throws Exception {
		String expectedResult = "<MediaContainer size=\"0\" allowSync=\"0\" mediaTagVersion=\"0\" sortAsc=\"0\" viewMode=\"0\"/>";
		mediaContainer.setSize(0);
		Serializer serializer = new Persister();
		serializer.write(mediaContainer, byteArrayOut);

		assertXMLEqual(expectedResult, byteArrayOut.toString());
	}
	
	@Test
	public void testSimpleMediaContainerDirectory() throws Exception {
		String expectedResult = "<MediaContainer size=\"0\" allowSync=\"0\" mediaTagVersion=\"0\" sortAsc=\"0\" viewMode=\"0\">" +
				"<Directory key=\"sections\" title=\"Test Directory\" refreshing=\"0\" updatedAt=\"0\" createdAt=\"0\"/>" +
				"</MediaContainer>";

		mediaContainer.setSize(0);
		List<Directory> arrayList = new ArrayList();
		Directory directory = new Directory();
		directory.setTitle("Test Directory");
		directory.setKey("sections");
		arrayList.add(directory);
		mediaContainer.setDirectories(arrayList);
		
		Serializer serializer = new Persister();
		serializer.write(mediaContainer, byteArrayOut);
		
		assertXMLEqual(expectedResult, byteArrayOut.toString());
	}
	
	@Test
	public void testMultipleDirectories() throws Exception {
		String expectedResult = "<MediaContainer size=\"0\" allowSync=\"0\" mediaTagVersion=\"0\" sortAsc=\"0\" viewMode=\"0\">" +
              "<Directory key=\"sections\" title=\"Test Directory\" refreshing=\"0\" updatedAt=\"0\" createdAt=\"0\"/>" +
              "<Directory key=\"recentlyAdded\" title=\"Test Directory 2\" refreshing=\"0\" updatedAt=\"0\" createdAt=\"0\"/>" +
              "</MediaContainer>";
		
		mediaContainer.setSize(0);
		List<Directory> arrayList = new ArrayList();
		Directory directory = new Directory();
		directory.setTitle("Test Directory");
		directory.setKey("sections");
		arrayList.add(directory);
		directory = new Directory();
		directory.setTitle("Test Directory 2");
		directory.setKey("recentlyAdded");
		arrayList.add(directory);

		mediaContainer.setDirectories(arrayList);
		
		Serializer serializer = new Persister();
		serializer.write(mediaContainer, byteArrayOut);
		
		assertXMLEqual("Not equal", expectedResult, byteArrayOut.toString());
	
	}

	

}
