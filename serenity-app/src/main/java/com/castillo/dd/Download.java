package com.castillo.dd;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.TimeZone;

import android.util.Log;

// This class downloads a file from a URL.
public class Download extends Observable implements Runnable {

	// Max size of download buffer.
	private static final int MAX_BUFFER_SIZE = 8192;

	// These are the status names.
	public static final String STATUSES[] = { "Downloading", "Paused",
			"Complete", "Cancelled", "Error" };

	// These are the status codes.
	public static final int START = -1;
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;

	private URL url; // download URL
	private long size; // size of download in bytes
	private long downloaded; // number of bytes downloaded
	private int status; // current status of download
	private String fileName;
	private String destination;

	private int i;
	private long launchTime = 0;
	private long startTime = 0;

	private boolean realUrl = false;

	public String getOrder() {
		String ret = Integer.valueOf(i).toString();
		if (ret.length() == 1)
			ret = "0" + ret;
		return ret;
	}

	// Constructor for Download.
	public Download(URL url, int i) {
		this.url = url;
		this.i = i;
		size = -1;
		downloaded = 0;
		status = START;

		// Begin the download.
		// download();
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	// Get this download's URL.
	public String getUrl() {
		return url.toString();
	}

	// Get this download's size.
	public long getSize() {
		return size;
	}

	// Get this download's progress.
	public float getProgress() {
		return ((float) downloaded / size) * 100;
	}

	// Get this download's status.
	public int getStatus() {
		return status;
	}

	// Get this download's launch Time
	public long getLaunchTime() {
		return launchTime;
	}

	// Get this download's ellapsed time
	public String getEllapsedTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar cal = Calendar.getInstance();
		long elapsed = cal.getTimeInMillis();
		elapsed = elapsed - startTime;
		cal.setTimeInMillis(elapsed);
		return dateFormat.format(cal.getTime());
	}

	// Get this download's speed
	public float getSpeed() {
		float s = downloaded / 1024;
		Calendar cal = Calendar.getInstance();
		long elapsed = cal.getTimeInMillis();
		elapsed = elapsed - startTime;
		elapsed = elapsed / 1000;
		s = s / elapsed;
		return s;
	}

	// Get this download's remaining time
	public String getRemainingTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar cal = Calendar.getInstance();
		float kbs = (size - downloaded) / 1024;
		float seconds = kbs / getSpeed();
		long millis = (long) (seconds * 1000);
		cal.setTimeInMillis(millis);
		return dateFormat.format(cal.getTime());
	}

	// Pause this download.
	public void pause() {
		status = PAUSED;
		stateChanged();
	}

	// Resume this download.
	public void resume() {
		status = DOWNLOADING;
		stateChanged();
		download();
	}

	// Cancel this download.
	public void cancel() {
		status = CANCELLED;
		stateChanged();
	}

	// Mark this download as having an error.
	private void error() {
		status = ERROR;
		Log.e(getClass().getName(),
				"Aborting download due to content legnth issue");
		stateChanged();
	}

	// Start or resume downloading.
	private void download() {
		Thread thread = new Thread(this);
		thread.start();
	}

	// Get file name portion of URL.
	public String getFileName(URL url) {
		fileName = url.getFile();
		fileName = URLDecoder.decode(fileName.substring(fileName
				.lastIndexOf('/') + 1));
		return fileName;
	}

	public String getFileName() {
		if (fileName == null || fileName.length() == 0) {
			fileName = url.getFile();
			fileName = URLDecoder.decode(fileName.substring(fileName
					.lastIndexOf('/') + 1));
		}
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	// Download file.
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;

		try {
			if (launchTime == 0) {
				launchTime = Calendar.getInstance().getTimeInMillis();
				realUrl = false;
			}
			// Open connection to URL.
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setInstanceFollowRedirects(false);

			if (status < ERROR) {
				// Specify what portion of file to download.
				connection.setRequestProperty("Range", "bytes=" + downloaded
						+ "-");

				// Connect to server.
				connection.connect();

				// Check for valid content length.
				String contentLengthValue = connection
						.getHeaderField("Content-Length");
				long contentLength = 0;
				if (contentLengthValue != null) {
					contentLength = Long.parseLong(contentLengthValue);
				}

				if (contentLength < 1) {
					error();
				}

				/*
				 * Set the size for this download if it hasn't been already set.
				 */
				if (size == -1) {
					size = contentLength;
					stateChanged();
				}
				
				// Open file and seek to the end of it.
				fileName = getFileName();
				if (startTime == 0) {
					startTime = Calendar.getInstance().getTimeInMillis();
				}
				file = new RandomAccessFile(destination + "/" + fileName, "rw");
				file.seek(downloaded);

				stream = connection.getInputStream();
				while (status == DOWNLOADING) {
					/*
					 * Size buffer according to how much of the file is left to
					 * download.
					 */
					byte buffer[];
					buffer = new byte[MAX_BUFFER_SIZE];

					// Read from server into buffer.
					int read = stream.read(buffer);
					if (read == -1)
						break;

					// Write buffer to file.
					file.write(buffer, 0, read);
					downloaded += read;
					stateChanged();
				}

				/*
				 * Change status to complete if this point was reached because
				 * downloading has finished.
				 */
				if (status == DOWNLOADING) {
					status = COMPLETE;
					stateChanged();
				}
			}
		} catch (Exception e) {
			error();
		} finally {
			// Close file.
			if (file != null) {
				try {
					file.close();
				} catch (Exception e) {
				}
			}

			// Close connection to server.
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}
			}
		}
	}

	// Notify observers that this download's status has changed.
	private void stateChanged() {
		setChanged();
		notifyObservers();
	}
}