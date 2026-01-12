/**
 * The MIT License (MIT)
 * Copyright (c) 2012-2014 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package us.nineworlds.serenity

import android.app.Application
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.danlew.android.joda.JodaTimeAndroid
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import toothpick.Toothpick
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.android.mediacodec.MediaCodecInfoUtil
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.services.UnWatchVideoJob
import us.nineworlds.serenity.core.services.UpdateProgressRequestJob
import us.nineworlds.serenity.core.services.WatchedVideoJob
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.emby.server.EmbyServer
import us.nineworlds.serenity.emby.server.EmbyServerDiscover
import us.nineworlds.serenity.injection.ForMediaServers
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.LoginModule
import us.nineworlds.serenity.injection.modules.MainPresenterModule
import us.nineworlds.serenity.injection.modules.SerenityModule

/**
 * Global manager for the Serenity application
 *
 * @author dcarver
 */
open class SerenityApplication : Application() {
    @Inject
    @field:ForMediaServers
    lateinit var servers: MutableMap<String, Server>

    @Inject
    lateinit var androidHelper: AndroidHelper

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var localBroadcastManager: LocalBroadcastManager

    lateinit var eventBus: EventBus
    private fun init() {
        inject()
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            JodaTimeAndroid.init(this@SerenityApplication)
        }
        sendStartedApplicationEvent()
        eventBus = EventBus.getDefault()
        eventBus.register(this)
        logger.initialize()
    }

    protected open fun inject() {
        val scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
        scope.installModules(
            AndroidModule(this),
            SerenityModule(),
            LoginModule(),
            MainPresenterModule()
        )
        Toothpick.inject(this, scope)
    }

    @UnstableApi
    override fun onCreate() {
        super.onCreate()
        init()
        setDefaultPreferences()
        discoverServers()

        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.Default) {
            MediaCodecInfoUtil.logAvailableCodecs()
        }

        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            val leastRecentlyUsedCacheEvictor =
                LeastRecentlyUsedCacheEvictor((200 * 1024 * 1024).toLong())
            val exoDatabaseProvider = ExoDatabaseProvider(this@SerenityApplication)
            simpleCache = SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
        }
    }

    protected open fun setDefaultPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            if (androidHelper.isAndroidTV ||
                androidHelper.isAmazonFireTV ||
                androidHelper.isLeanbackSupported
            ) {
                preferences.edit().putBoolean("serenity_tv_mode", true).apply()
            }
        }
    }

    private fun sendStartedApplicationEvent() {
        val deviceModel = Build.MODEL
        if (enableTracking) {
            val analytics = FirebaseAnalytics.getInstance(this)
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Devices")
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, deviceModel)
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, deviceModel)
            analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
        }
    }

    override fun onTerminate() {
        eventBus.unregister(this)
        WatchedVideoJob.onFinish()
        UpdateProgressRequestJob.onFinish()
        UnWatchVideoJob.onFinish()
        super.onTerminate()
    }

    protected open fun discoverServers() {
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            EmbyServerDiscover().findServers()
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onEmbyServerDiscovery(serverEvent: EmbyServer) {
        serverEvent.serverName?.let {
            servers[it] = serverEvent
        }
    }

    companion object {
        private var enableTracking = true
        fun disableTracking() {
            enableTracking = false
        }

        @UnstableApi
        lateinit var simpleCache: SimpleCache
    }
}
