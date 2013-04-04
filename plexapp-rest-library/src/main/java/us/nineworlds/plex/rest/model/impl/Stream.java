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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * @author dcarver
 *
 */
@Root(name="Stream")
public class Stream {
	
	@Attribute(name="id",required=false)
	private int id;
	
	@Attribute(name="streamType",required=false)
	private int streamType;
	
	@Attribute(name="codec",required=false)
	private String codec;
	
	@Attribute(name="index",required=false)
	private String index;
	
	@Attribute(name="channels",required=false)
	private String channels;

	@Attribute(name="duration",required=false)
	private String duration;
	
	@Attribute(name="bitrate",required=false)
	private String bitrate;
	
	@Attribute(name="bitrateMode",required=false)
	private String bitrateMode;
	
	@Attribute(name="profile",required=false)
	private String profile;
	
	@Attribute(name="optimizedForStreaming",required=false)
	private String optimizedForStreaming;
	
	@Attribute(name="format",required=false)
	private String format;
	
	@Attribute(name="key",required=false)
	private String key;
	
	@Attribute(name="language",required=false)
	private String language;

	@Attribute(name="languageCode",required=false)
	private String languageCode;
	
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStreamType() {
		return streamType;
	}

	public void setStreamType(int streamType) {
		this.streamType = streamType;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getBitrate() {
		return bitrate;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public String getBitrateMode() {
		return bitrateMode;
	}

	public void setBitrateMode(String bitrateMode) {
		this.bitrateMode = bitrateMode;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getOptimizedForStreaming() {
		return optimizedForStreaming;
	}

	public void setOptimizedForStreaming(String optimizedForStreaming) {
		this.optimizedForStreaming = optimizedForStreaming;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}
