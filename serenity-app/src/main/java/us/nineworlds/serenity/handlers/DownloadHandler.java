package us.nineworlds.serenity.handlers;

import java.util.List;

import android.support.v4.app.NotificationCompat;
import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.SerenityApplication;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.castillo.dd.DSInterface;
import com.castillo.dd.Download;
import com.castillo.dd.PendingDownload;

public class DownloadHandler extends Handler {
	boolean downloadsCancelled;
	private int downloadIndex;
	private DSInterface dsInterface;
	private final Context context;
	private final NotificationManager notificationManager;

	private final DownloadServiceConnection downloadServiceConnection;

	private static DownloadHandler instance;

	public static DownloadHandler getInstance(Context context) {
		if (instance == null) {
			instance = new DownloadHandler(context);
		}
		return instance;
	}

	private DownloadHandler(Context context) {
		downloadServiceConnection = new DownloadServiceConnection();
		this.context = context;
		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public DownloadServiceConnection getDownloadService() {
		return downloadServiceConnection;
	}

	public DSInterface getDownloadServiceInterface() {
		return dsInterface;
	}

	@Override
	public void handleMessage(Message msg) {

		if ((msg.what == SerenityApplication.PROGRESS) && (!downloadsCancelled)) {
			List<PendingDownload> pendingDownloads = SerenityApplication
					.getPendingDownloads();
			for (int i = 0; i < pendingDownloads.size(); i++) {
				if (i == downloadIndex) {
					try {
						int status = dsInterface.getDownloadStatus(i);
						pendingDownloads.get(i).setStatus(status);
						if (status == Download.START) {
							dsInterface.downloadFile(i);
							notification(pendingDownloads.get(i).getFilename()
									+ " has started.", "Downloading "
											+ pendingDownloads.get(i).getFilename());
							pendingDownloads.get(i).setLaunchTime(
									dsInterface.getDownloadLaunchTime(i));

						} else if (status == Download.COMPLETE) {
							Toast.makeText(
									context,
									pendingDownloads.get(i).getFilename()
											+ " has completed.",
									Toast.LENGTH_LONG).show();

							downloadIndex++;
							if (downloadIndex >= pendingDownloads.size()
									|| pendingDownloads.size() == 0) {
								notificationManager.cancel(1);
							}
						}
						if (status != Download.COMPLETE) {
							pendingDownloads.get(i).setProgress(
									dsInterface.getDownloadProgress(i));
							pendingDownloads.get(i).setEllapsedTime(
									dsInterface.getDownloadEllapsedTime(i));
							pendingDownloads.get(i).setRemainingTime(
									dsInterface.getDownloadRemainingTime(i));
							pendingDownloads.get(i).setSpeed(
									dsInterface.getDownloadSpeed(i));
						} else {
							pendingDownloads.get(i).setProgress(100);
						}
					} catch (Exception e) {
						Log.e(getClass().getName(), Log.getStackTraceString(e));
					}
				}
			}
			sendMessageDelayed(obtainMessage(SerenityApplication.PROGRESS), 50);
		}
	}

	protected void notification(String tickerText, String expandedText) {
		int icon = R.drawable.serenity_bonsai_logo;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context);
		String expandedTitle = "Serenity Download";
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent launchIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		Notification notification = nBuilder.setSmallIcon(icon)
				.setTicker(tickerText)
				.setWhen(when)
				.setContentIntent(launchIntent)
				.setContentText(expandedText)
				.setContentTitle(expandedTitle)
				.build();
		int notificationRef = 1;
		notificationManager.notify(notificationRef, notification);
	}

	public class DownloadServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			dsInterface = DSInterface.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			dsInterface = null;
		}

	}

}
