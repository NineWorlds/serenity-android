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
import org.jetbrains.uast.UMethod

class PredictiveBackDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes(): List<Class<out org.jetbrains.uast.UElement>> = listOf(UMethod::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler = object : UElementHandler() {
        override fun visitMethod(node: UMethod) {
            if (node.name == "onBackPressed" && node.parameterList.parametersCount == 0) {
                val evaluator = context.evaluator
                if (evaluator.isOverride(node, true)) {
                    context.report(
                        ISSUE,
                        node,
                        context.getNameLocation(node),
                        "onBackPressed() is deprecated for Predictive Back. Use OnBackPressedDispatcher instead."
                    )
                }
            }
        }
    }

    companion object {
        val ISSUE = Issue.create(
            id = "PredictiveBackMigration",
            briefDescription = "onBackPressed() override detected",
            explanation = """
                With the introduction of Predictive Back, onBackPressed() is deprecated. 
                You should migrate to using OnBackPressedDispatcher with an OnBackPressedCallback 
                registered in onCreate() or similar lifecycle methods.
            """,
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                PredictiveBackDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
