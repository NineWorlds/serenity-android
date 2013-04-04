/**
 * The MIT License (MIT)
 * Copyright (c) 2010 Rafael C
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

package com.castillo.dd;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class DownloadService extends Service {

	private List<Download> downloads = new ArrayList<Download>();
	private int currentPosition;

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	private final DSInterface.Stub mBinder = new DSInterface.Stub() {

		public void downloadFile(int position) throws DeadObjectException {
			try {
				currentPosition = position;
				Download download = downloads.get(currentPosition);
				download.resume();

			} catch (IndexOutOfBoundsException e) {
				Log.e(getClass().getName(), e.getMessage(), e);
			}
		}

		public void addFileDownloadlist(String url, int position)
				throws DeadObjectException {
			try {
				downloads.add(new Download(new URL(url),
						position));
			} catch (Exception e) {
				Log.e(getClass().getName(), e.getMessage(), e);
			}
		}

		public void clearDownloadlist() throws DeadObjectException {
			Download download = downloads.get(currentPosition);
			download.cancel();
			downloads.clear();
		}

		public void pause() throws DeadObjectException {
			Download download = downloads.get(currentPosition);
			download.pause();
		}

		public void resume() throws DeadObjectException {
			Download download = downloads.get(currentPosition);
			download.resume();
		}

		public int getDownloadStatus(int position) throws RemoteException {
			Download download = downloads.get(position);
			return download.getStatus();
		}

		public int getDownloadProgress(int position) throws RemoteException {
			Download download = downloads.get(position);
			return (int) download.getProgress();
		}

		public int getDownloadlistSize() throws RemoteException {
			return downloads.size();
		}

		public String getDownloadFilename(int position) throws RemoteException {
			Download download = downloads.get(position);
			return download.getFileName();
		}

		public String getDownloadEllapsedTime(int position)
				throws RemoteException {
			Download download = downloads.get(position);
			return (String) download.getEllapsedTime();
		}

		public String getDownloadRemainingTime(int position)
				throws RemoteException {
			Download download = downloads.get(position);
			return (String) download.getRemainingTime();
		}

		public float getDownloadSpeed(int position) throws RemoteException {
			Download download = downloads.get(position);
			return (float) download.getSpeed();
		}

		public long getDownloadLaunchTime(int position) throws RemoteException {
			Download download = downloads.get(position);
			return (long) download.getLaunchTime();
		}

	};

}
