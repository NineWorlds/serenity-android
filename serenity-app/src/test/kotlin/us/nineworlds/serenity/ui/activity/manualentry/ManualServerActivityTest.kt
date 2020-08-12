package us.nineworlds.serenity.ui.activity.manualentry

import android.app.Application
import android.view.View
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isNotNull
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import org.junit.After

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.MainActivity
import us.nineworlds.serenity.R

@RunWith(RobolectricTestRunner::class)
class ManualServerActivityTest {

  private lateinit var activity: ManualServerActivity

  @Before
  fun setUp() {
    ApplicationProvider.getApplicationContext<Application>().setTheme(R.style.AppTheme)
    activity = spy(Robolectric.buildActivity(ManualServerActivity::class.java).create().get())
  }

  @Test
  fun activityHasManualServerSettingsFragment() {
    assertThat(activity.findViewById<View>(R.id.settingsFragment)).isNotNull()
  }

  @Test
  fun finishSetsResultToMainMenuPreferenceResultCode() {
    activity.finish()
    verify(activity).setResult(MainActivity.MAIN_MENU_PREFERENCE_RESULT_CODE)
  }
}