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

package com.github.kingargyle.plexappclient.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.github.kingargyle.plexapp.config.IConfiguration;

/**
 * We make this a Singleton so there is only ever one currently.
 * Need to change this in the future as could have multiple
 * plex servers that can be connected too.
 * 
 * @author dcarver
 *
 */
public class Config implements IConfiguration{
	
	private static Config config = null;
	
	private String serverdomain;
	private String port;
	
	private Config() throws IOException {
		Properties properties = new Properties();
		InputStream in = this.getClass().getResourceAsStream("/config.properties");
		properties.load(in);
		in.close();
		
		serverdomain = properties.getProperty("server");
		port = properties.getProperty("port");
		
	}
	
	public static Config getInstance() throws IOException {
		if (config == null) {
			config = new Config();
		}
		return config;
	}
	
	public String getHost() {
		return serverdomain;
	}
	
	public String getPort() {
		return port;
	}


	public void setHost(String host) {
		serverdomain = host;
		
	}

	public void setPort(String port) {
		this.port = port;
	}
}
