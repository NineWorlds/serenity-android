package com.github.kingargyle.plexapp.model.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import static org.custommonkey.xmlunit.XMLAssert.*;

//@RunWith(RobolectricTestRunner.class)
public class TestDirectorySerialization extends AbstractSerializationTest {
	
	private Directory directory;
	
	@Override
	@Before
	public void setUp() throws Exception {
		directory = new Directory();
		super.setUp();
	}
	
	@Override
	@After
	public void tearDown() throws Exception {
		directory = null;
	}
		
	@Test
	public void testDirectorySearch() throws Exception {
	  String expected =	"<Directory prompt=\"Search Movies\" search=\"1\" key=\"search?type=1\" title=\"Search...\"/>";
	  directory.setPrompt("Search Movies");
	  directory.setSearch("1");
	  directory.setKey("search?type=1");
	  directory.setTitle("Search...");
	  
	  Serializer serializer = new Persister();
	  serializer.write(directory, byteArrayOut);
	  String result = byteArrayOut.toString();
	  
	  assertXpathExists("Directory[@prompt = 'Search Movies']", result);
	  assertXpathExists("Directory[@search = '1']", result);
	  assertXpathExists("Directory[@key = 'search?type=1']", result);
	  assertXpathExists("Directory[@title = 'Search...']", result);
	  
	}

}
