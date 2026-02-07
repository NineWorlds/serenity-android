package us.nineworlds.serenity.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import org.junit.Test

class LegacyPreferenceDetectorTest {

    private val sharedPreferencesStub = java(
        """
        package android.content;
        public interface SharedPreferences {
            interface Editor {
                void apply();
            }
        }
        """.trimIndent()
    )

    private val contextStub = java(
        """
        package android.content;
        import android.content.SharedPreferences;
        public class Context {
            public SharedPreferences getSharedPreferences(String name, int mode) { return null; }
        }
        """.trimIndent()
    )

    private val preferenceManagerStub = java(
        """
        package android.preference;
        import android.content.Context;
        import android.content.SharedPreferences;
        public class PreferenceManager {
            public static SharedPreferences getDefaultSharedPreferences(Context context) { return null; }
        }
        """.trimIndent()
    )

    private val stringPreferenceStub = kotlin(
        """
        package us.nineworlds.serenity.core.util
        class StringPreference(val name: String)
        """.trimIndent()
    )

    @Test
    fun testSharedPreferencesImport() {
        lint().files(
            sharedPreferencesStub,
            kotlin(
                """
                package us.nineworlds.serenity
                import android.content.SharedPreferences
                
                class TestClass {
                    fun test() { }
                }
                """.trimIndent()
            )
        )
            .testModes(TestMode.DEFAULT)
            .allowMissingSdk()
            .issues(LegacyPreferenceDetector.ISSUE)
            .run()
            .expect(
                """
            src/us/nineworlds/serenity/TestClass.kt:2: Warning: Legacy SharedPreferences usage detected. Please migrate to SettingsRepository. [LegacyPreferenceUsage]
            import android.content.SharedPreferences
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testGetSharedPreferencesCall() {
        lint().files(
            sharedPreferencesStub,
            contextStub,
            kotlin(
                """
                package us.nineworlds.serenity
                import android.content.Context
                
                class TestClass(val context: Context) {
                    fun test() {
                        val prefs = context.getSharedPreferences("test", 0)
                    }
                }
                """.trimIndent()
            )
        )
            .testModes(TestMode.DEFAULT)
            .allowMissingSdk()
            .issues(LegacyPreferenceDetector.ISSUE)
            .run()
            .expect(
                """
            src/us/nineworlds/serenity/TestClass.kt:6: Warning: Context.getSharedPreferences() detected. Please migrate to SettingsRepository. [LegacyPreferenceUsage]
                    val prefs = context.getSharedPreferences("test", 0)
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testPreferenceManagerUsage() {
        lint().files(
            sharedPreferencesStub,
            contextStub,
            preferenceManagerStub,
            kotlin(
                """
                package us.nineworlds.serenity
                import android.preference.PreferenceManager
                
                class TestClass {
                    fun test() {
                        val prefs = PreferenceManager.getDefaultSharedPreferences(null)
                    }
                }
                """.trimIndent()
            )
        )
            .testModes(TestMode.DEFAULT)
            .allowMissingSdk()
            .issues(LegacyPreferenceDetector.ISSUE)
            .run()
            .expect(
                """
            src/us/nineworlds/serenity/TestClass.kt:2: Warning: Legacy SharedPreferences usage detected. Please migrate to SettingsRepository. [LegacyPreferenceUsage]
            import android.preference.PreferenceManager
            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            src/us/nineworlds/serenity/TestClass.kt:6: Warning: PreferenceManager usage detected. Please migrate to SettingsRepository. [LegacyPreferenceUsage]
                    val prefs = PreferenceManager.getDefaultSharedPreferences(null)
                                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            0 errors, 2 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testStringPreferenceUsageNotFlagged() {
        lint().files(
            stringPreferenceStub,
            kotlin(
                """
                package us.nineworlds.serenity
                import us.nineworlds.serenity.core.util.StringPreference
                
                class TestClass {
                    fun test() {
                        val pref = StringPreference("test")
                    }
                }
                """.trimIndent()
            )
        )
            .testModes(TestMode.DEFAULT)
            .allowMissingSdk()
            .issues(LegacyPreferenceDetector.ISSUE)
            .run()
            .expectClean()
    }
}
