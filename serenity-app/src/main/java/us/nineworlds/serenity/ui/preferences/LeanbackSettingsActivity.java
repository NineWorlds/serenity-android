package us.nineworlds.serenity.ui.preferences;

import android.os.Bundle;
import androidx.annotation.Nullable;
import us.nineworlds.serenity.MainActivity;
import us.nineworlds.serenity.R;
import us.nineworlds.serenity.injection.InjectingActivity;

public class LeanbackSettingsActivity extends InjectingActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
  }

  @Override public void finish() {
    setResult(MainActivity.MAIN_MENU_PREFERENCE_RESULT_CODE);
    super.finish();
  }

}
