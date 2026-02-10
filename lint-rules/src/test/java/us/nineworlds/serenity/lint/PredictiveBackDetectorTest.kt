package us.nineworlds.serenity.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import org.junit.Test

class PredictiveBackDetectorTest {

    private val activityStub = java(
        """
        package android.app;
        public class Activity {
            public void onBackPressed() {}
        }
        """.trimIndent()
    )

    @Test
    fun testKotlinOnBackPressedOverride() {
        lint().files(
            activityStub,
            kotlin(
                """
                package us.nineworlds.serenity
                import android.app.Activity
                
                class TestActivity : Activity() {
                    override fun onBackPressed() {
                        super.onBackPressed()
                    }
                }
                """.trimIndent()
            )
        )
            .testModes(TestMode.DEFAULT)
            .allowMissingSdk()
            .issues(PredictiveBackDetector.ISSUE)
            .run()
            .expect(
                """
            src/us/nineworlds/serenity/TestActivity.kt:5: Warning: onBackPressed() is deprecated for Predictive Back. Use OnBackPressedDispatcher instead. [PredictiveBackMigration]
                override fun onBackPressed() {
                             ~~~~~~~~~~~~~
            0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testJavaOnBackPressedOverride() {
        lint().files(
            activityStub,
            java(
                """
                package us.nineworlds.serenity;
                import android.app.Activity;
                
                public class TestActivity extends Activity {
                    @Override
                    public void onBackPressed() {
                        super.onBackPressed();
                    }
                }
                """.trimIndent()
            )
        )
            .testModes(TestMode.DEFAULT)
            .allowMissingSdk()
            .issues(PredictiveBackDetector.ISSUE)
            .run()
            .expect(
                """
            src/us/nineworlds/serenity/TestActivity.java:6: Warning: onBackPressed() is deprecated for Predictive Back. Use OnBackPressedDispatcher instead. [PredictiveBackMigration]
                public void onBackPressed() {
                            ~~~~~~~~~~~~~
            0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testNonOverrideNotFlagged() {
        lint().files(
            kotlin(
                """
                package us.nineworlds.serenity
                
                class TestClass {
                    fun onBackPressed() {
                    }
                }
                """.trimIndent()
            )
        )
            .testModes(TestMode.DEFAULT)
            .allowMissingSdk()
            .issues(PredictiveBackDetector.ISSUE)
            .run()
            .expectClean()
    }
}
