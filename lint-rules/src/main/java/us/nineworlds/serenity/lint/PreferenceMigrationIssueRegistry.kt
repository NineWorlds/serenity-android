package us.nineworlds.serenity.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class PreferenceMigrationIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(LegacyPreferenceDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API

    override val vendor: Vendor = Vendor(
        vendorName = "Serenity for Android",
        identifier = "us.nineworlds.serenity",
        feedbackUrl = "https://github.com/kingargyle/serenity-android/issues"
    )
}
