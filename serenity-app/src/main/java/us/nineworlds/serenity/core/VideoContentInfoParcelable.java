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

package us.nineworlds.serenity.core;

import us.nineworlds.serenity.core.model.VideoContentInfo;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author dcarver
 * 
 */
public class VideoContentInfoParcelable implements Parcelable {

	VideoContentInfo video;

	public VideoContentInfoParcelable(VideoContentInfo video) {
		this.video = video;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(video);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<VideoContentInfoParcelable> CREATOR = new Parcelable.Creator<VideoContentInfoParcelable>() {
		@Override
		public VideoContentInfoParcelable createFromParcel(Parcel in) {
			return new VideoContentInfoParcelable(in);
		}

		@Override
		public VideoContentInfoParcelable[] newArray(int size) {
			return new VideoContentInfoParcelable[size];
		}
	};

	private VideoContentInfoParcelable(Parcel in) {
		video = (VideoContentInfo) in.readValue(null);
	}
}
