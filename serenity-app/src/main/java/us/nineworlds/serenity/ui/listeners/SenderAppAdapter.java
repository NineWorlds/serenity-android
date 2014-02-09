package us.nineworlds.serenity.ui.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import us.nineworlds.serenity.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter that shows any apps in the system that respond to Intent.ACTION_SEND intents.  Filters out any apps not in
 * the FILTER_LIST and,
 * @author nlawson
 * https://github.com/nolanlawson/CustomSenderDemo
 * 
 * @dcarver - Changed to only use the apps in the filter set.  
 *
 */
public class SenderAppAdapter extends ArrayAdapter<ResolveInfo> {
	
	public static final Set<String> FILTER_SET = new HashSet<String>(Arrays.asList("com.entertailion.android.remote.StartupActivity", "com.google.android.apps.tvremote.StartupActivity", "com.koushikdutta.cast.CastActivity"));
	
	private Context mContext;
	
	public SenderAppAdapter(Context context) {
		super(context, R.layout.chooser_row, new ArrayList<ResolveInfo>());
		
		mContext = getContext();		
		List<ResolveInfo> items = createItems();
		for (ResolveInfo item : items) {
			add(item);
		}
	}

	public void respondToClick(int position, final String subject, final String body) {

		ResolveInfo launchable = getItem(position);
		ActivityInfo activity=launchable.activityInfo;
			
		ComponentName name= new ComponentName(activity.applicationInfo.packageName,activity.name);
			
		Intent actionSendIntent= createSendIntent(subject, body);
		actionSendIntent.setComponent(name);
	 
		mContext.startActivity(actionSendIntent);	
	}
	
	private List<ResolveInfo> createItems() {
		
		List<ResolveInfo> items = mContext.getPackageManager().queryIntentActivities(createSendIntent("", ""), 0);
				
		filter(items);
		Collections.sort(items, new ResolveInfo.DisplayNameComparator(mContext.getPackageManager())); 		
		
		return items;
	}
	
	 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
			convertView=newView(parent);
		}

		bindView(position, convertView);

		return(convertView);
	}
	
	private View newView(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(R.layout.chooser_row, parent, false);
	}

	private void bindView(int position, View row) {
		
		PackageManager packageManager = mContext.getPackageManager();
		ResolveInfo item = getItem(position);
		
		TextView label=(TextView)row.findViewById(android.R.id.title);

		label.setText(item.loadLabel(packageManager));

		ImageView iconImageView=(ImageView)row.findViewById(android.R.id.icon);

		Drawable drawableIcon = item.loadIcon(packageManager);
		
		if (drawableIcon != null) {
		
			Bitmap iconBitmap = resizeBitmap(drawableIcon);
			
			iconImageView.setImageBitmap(iconBitmap);
			iconImageView.setVisibility(View.VISIBLE);
		
		} else {
			iconImageView.setVisibility(View.INVISIBLE);
		}
	}
	
	private void filter(List<ResolveInfo> apps) {
		for (Iterator<ResolveInfo> iter = apps.iterator(); iter.hasNext();) {
			ResolveInfo resolveInfo = iter.next();
			
			if (!FILTER_SET.contains(resolveInfo.activityInfo.name)) { 
				iter.remove();
			} 	
		}
	}	
	
	private static Intent createSendIntent(String subject, String body) {
		
		Intent actionSendIntent=new Intent(android.content.Intent.ACTION_SEND);

		actionSendIntent.setType("text/plain");
		actionSendIntent.putExtra(Intent.EXTRA_TEXT, body);
		
		return actionSendIntent;
	}
	
	private Bitmap resizeBitmap(Drawable drawable) {
		// resize the icon bitmap, because sometimes, seemingly randomly, an icon from the PackageManager 
		// will be too large
		
		int iconSize = mContext.getResources().getDimensionPixelSize(android.R.dimen.app_icon_size);
				
		Bitmap bmp = Bitmap.createBitmap(iconSize, iconSize, Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		drawable.setBounds(new Rect(0,0,iconSize,iconSize));
		drawable.draw(c);

		return bmp;
	}

}