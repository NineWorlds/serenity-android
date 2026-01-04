package us.nineworlds.serenity.injection.modules.providers

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import javax.inject.Inject
import javax.inject.Provider
import us.nineworlds.serenity.SerenityApplication
import us.nineworlds.serenity.common.android.injection.ApplicationContext

class DataSourceFactoryProvider : Provider<DataSource.Factory> {
    @Inject
    @field:ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var httpDataSourceFactory: HttpDataSource.Factory

    @OptIn(UnstableApi::class)
    override fun get(): DataSource.Factory {
        val bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(context)
        val defaultDataSourceFactory =
            DefaultDataSourceFactory(context, bandwidthMeter, httpDataSourceFactory)
        return CacheDataSource.Factory()
            .setCache(SerenityApplication.simpleCache)
            .setUpstreamDataSourceFactory(defaultDataSourceFactory)

    }
}
