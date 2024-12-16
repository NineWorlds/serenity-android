package us.nineworlds.serenity

import android.content.Context
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.birbit.android.jobqueue.JobManager
import io.mockk.mockk
import toothpick.config.Module
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.util.AndroidHelper

class MockkTestingModule : Module() {
    companion object {
        val mockJobManager: JobManager = mockk(relaxed = true)
        val mockPlexAppFactory: SerenityClient = mockk(relaxed = true)
        val mockLocalBroadcastManager: LocalBroadcastManager = mockk(relaxed = true)
        var mockLogger: Logger = mockk(relaxed = true)
        var mockAndroidHelper: AndroidHelper = mockk(relaxed = true)
    }

    init {
        bind(JobManager::class.java).toInstance(mockJobManager)
        bind(SerenityClient::class.java).toInstance(mockPlexAppFactory)
        bind(LocalBroadcastManager::class.java).toInstance(mockLocalBroadcastManager)
        bind(Logger::class.java).toInstance(mockLogger)
        bind(AndroidHelper::class.java).toInstance(mockAndroidHelper)
    }
}