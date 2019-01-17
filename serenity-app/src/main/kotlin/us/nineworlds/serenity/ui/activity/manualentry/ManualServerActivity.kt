package us.nineworlds.serenity.ui.activity.manualentry

import android.os.Bundle
import us.nineworlds.serenity.MainActivity
import us.nineworlds.serenity.R
import us.nineworlds.serenity.injection.InjectingActivity

class ManualServerActivity : InjectingActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_manual_server_settings)
  }

  override fun finish() {
    // TO Do Handle either adding a new server entry or launching
    setResult(MainActivity.MAIN_MENU_PREFERENCE_RESULT_CODE)
    super.finish()
  }
}