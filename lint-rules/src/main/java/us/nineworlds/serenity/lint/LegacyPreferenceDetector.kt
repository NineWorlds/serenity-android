package us.nineworlds.serenity.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UFile
import org.jetbrains.uast.UImportStatement
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.getParentOfType

class LegacyPreferenceDetector :
    Detector(),
    SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UImportStatement::class.java,
        UCallExpression::class.java,
        USimpleNameReferenceExpression::class.java
    )

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        val packageName = context.uastFile?.packageName
        if (packageName != null && (packageName.startsWith("android.") || packageName.startsWith("androidx."))) {
            return null
        }

        return object : UElementHandler() {
            override fun visitImportStatement(node: UImportStatement) {
                val importName = node.importReference?.asSourceString() ?: return
                if (importName == "android.content.SharedPreferences" ||
                    importName == "android.preference.PreferenceManager" ||
                    importName == "androidx.preference.PreferenceManager"
                ) {
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "Legacy SharedPreferences usage detected. Please migrate to SettingsRepository."
                    )
                }
            }

            override fun visitSimpleNameReferenceExpression(node: USimpleNameReferenceExpression) {
                // Avoid double reporting if it's part of an import
                if (node.getParentOfType<UImportStatement>(true) != null) return

                val name = node.identifier
                if (name == "PreferenceManager") {
                    // Avoid double reporting if this is the receiver of a getDefaultSharedPreferences call
                    var p: UElement? = node.uastParent
                    while (p != null && p !is UFile) {
                        if (p is UCallExpression && p.methodName == "getDefaultSharedPreferences") {
                            return
                        }
                        if (p is UQualifiedReferenceExpression) {
                            val selector = p.selector
                            if (selector is UCallExpression && selector.methodName == "getDefaultSharedPreferences") {
                                return
                            }
                        }
                        p = p.uastParent
                    }

                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "PreferenceManager usage detected. Please migrate to SettingsRepository."
                    )
                } else if (name == "SharedPreferences") {
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "Legacy SharedPreferences usage detected. Please migrate to SettingsRepository."
                    )
                }
            }

            override fun visitCallExpression(node: UCallExpression) {
                val methodName = node.methodName
                if (methodName == "getSharedPreferences") {
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "Context.getSharedPreferences() detected. Please migrate to SettingsRepository."
                    )
                } else if (methodName == "getDefaultSharedPreferences") {
                    context.report(
                        ISSUE,
                        node,
                        context.getLocation(node),
                        "PreferenceManager usage detected. Please migrate to SettingsRepository."
                    )
                }
            }
        }
    }

    companion object {
        val ISSUE = Issue.create(
            id = "LegacyPreferenceUsage",
            briefDescription = "Legacy SharedPreferences usage detected",
            explanation = """
                The project is migrating to DataStore via SettingsRepository. 
                Direct usage of SharedPreferences or PreferenceManager should be avoided.
            """,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                LegacyPreferenceDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
