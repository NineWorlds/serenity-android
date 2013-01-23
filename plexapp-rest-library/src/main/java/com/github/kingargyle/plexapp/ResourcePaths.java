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

import com.github.kingargyle.plexapp.config.IConfiguration;

/**
 * @author dcarver
 *
 */
public class ResourcePaths {

	public static final String LIBRARY_PATH = "/library/";
	public static final String SECTIONS_PATH = LIBRARY_PATH + "sections/";
	public static final String ROOT_PATH = "/";
	
	private IConfiguration config;
	
	public ResourcePaths(IConfiguration configuration) {
		config = configuration;
	}
	
	public String getRoot() {
		return getHostPort() + ROOT_PATH;
	}
	
	public String getLibraryURL() {
		return getHostPort() + LIBRARY_PATH;
	}
	
	public String getSectionsURL() {
		return getHostPort() + SECTIONS_PATH;
	}
	
	public String getSectionsURL(String key) {
		return getHostPort() + SECTIONS_PATH + key + "/";
	}
	
	public String getSectionsURL(String key, String category) {
		return getHostPort() + SECTIONS_PATH + key + "/" + category + "/";
	}
	
	
	protected String getHostPort() {
		return "http://" + config.getHost() + ":" + config.getPort();
	}
	
	public String getSeasonsURL(String key) {
		return getHostPort() + key; 
	}
}
