// Copyright 2013 Google Inc. All Rights Reserved.

package us.nineworlds.serenity.ui.activity;

import us.nineworlds.serenity.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class OverscanSetupActivity extends Activity implements
		View.OnKeyListener, View.OnClickListener {

	private View centerView;
	private boolean topLeft = true;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.overscan_setup);
		centerView = findViewById(R.id.center);
		final View swapView = findViewById(R.id.swap);

		final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) centerView
				.getLayoutParams();
		layoutParams.setMargins(prefs.getInt("overscan_left", 50),
				prefs.getInt("overscan_top", 50),
				prefs.getInt("overscan_right", 50),
				prefs.getInt("overscan_bottom", 50));
		centerView.requestLayout();
		swapView.setOnKeyListener(this);
		swapView.setOnClickListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() != KeyEvent.ACTION_DOWN) {
			return false;
		}
		final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) centerView
				.getLayoutParams();
		int top = layoutParams.topMargin;
		int bottom = layoutParams.bottomMargin;
		int left = layoutParams.leftMargin;
		int right = layoutParams.rightMargin;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			if (topLeft) {
				top--;
			} else {
				bottom++;
			}
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (topLeft) {
				top++;
			} else {
				bottom--;
			}
			break;

		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (topLeft) {
				left--;
			} else {
				right++;
			}
			break;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (topLeft) {
				left++;
			} else {
				right--;
			}
			break;

		default:
			return false;
		}
		prefs.edit().putInt("overscan_top", top).putInt("overscan_left", left)
				.putInt("overscan_bottom", bottom)
				.putInt("overscan_right", right).apply();

		layoutParams.setMargins(left, top, right, bottom);
		centerView.requestLayout();
		return true;
	}

	@Override
	public void onClick(View v) {
		topLeft = !topLeft;
	}
}
