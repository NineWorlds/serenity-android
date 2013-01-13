package com.github.kingargyle.plexappclient;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TVBrowserShowActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tvbrowser_show);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tvbrowser_show, menu);
		return true;
	}

}
