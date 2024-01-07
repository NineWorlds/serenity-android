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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.birbit.android.jobqueue.JobManager
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.firebase.analytics.FirebaseAnalytics
import net.danlew.android.joda.JodaTimeAndroid
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import toothpick.Toothpick
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.common.android.mediacodec.MediaCodecInfoUtil
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.logger.Logger
import us.nineworlds.serenity.core.util.AndroidHelper
import us.nineworlds.serenity.emby.server.EmbyServer
import us.nineworlds.serenity.emby.server.EmbyServerJob
import us.nineworlds.serenity.injection.ForMediaServers
import us.nineworlds.serenity.injection.modules.AndroidModule
import us.nineworlds.serenity.injection.modules.LoginModule
import us.nineworlds.serenity.injection.modules.SerenityModule
import javax.inject.Inject

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
    lateinit var jobManager: JobManager

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var localBroadcastManager: LocalBroadcastManager

    lateinit var eventBus: EventBus
    private fun init() {
        inject()
        JodaTimeAndroid.init(this)
        sendStartedApplicationEvent()
        eventBus = EventBus.getDefault()
        eventBus.register(this)
        jobManager.start()
        logger.initialize()
    }

    protected open fun inject() {
        val scope = Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE)
        scope.installModules(AndroidModule(this), SerenityModule(), LoginModule())
        Toothpick.inject(this, scope)
    }

    override fun onCreate() {
        super.onCreate()
        init()
        setDefaultPreferences()
        discoverServers()
        MediaCodecInfoUtil().logAvailableCodecs()

        val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor((200 * 1024 * 1024).toLong())
        val exoDatabaseProvider = ExoDatabaseProvider(this)
        simpleCache = SimpleCache(cacheDir, leastRecentlyUsedCacheEvictor, exoDatabaseProvider)
    }

    protected open fun setDefaultPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preferences, true)
        val editor = preferences.edit()
        if (androidHelper.isAndroidTV
                || androidHelper.isAmazonFireTV
                || androidHelper.isLeanbackSupported) {
            editor.putBoolean("serenity_tv_mode", true)
            editor.apply()
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
        jobManager.stop()
        super.onTerminate()
    }

    protected open fun discoverServers() {
        jobManager.addJobInBackground(EmbyServerJob())
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

        lateinit var simpleCache: SimpleCache
    }
}
