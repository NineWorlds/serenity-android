package us.nineworlds.serenity.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val baselineProfileRule = BaselineProfileRule()

    @Test
    fun generate() {
        baselineProfileRule.collect(
            packageName = "us.nineworlds.serenity",
            maxIterations = 3,
            stableIterations = 2
        ) {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            
            // Navigate home and launch
            device.pressHome()
            startActivityAndWait()
            
            // Wait for the app to load
            device.wait(Until.hasObject(By.pkg("us.nineworlds.serenity").depth(0)), 15000)
            device.waitForIdle()

            // Perform some common UI interactions to trigger code paths
            // We'll simulate a slight delay to allow the app to settle
            Thread.sleep(2000)
            
            // If there's a common element like a list or menu, interact with it:
            // device.findObject(By.res("us.nineworlds.serenity:id/main_menu_list")).click()
            // device.waitForIdle()
        }
    }
}
