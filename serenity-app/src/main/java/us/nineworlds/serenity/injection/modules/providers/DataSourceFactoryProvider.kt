package us.nineworlds.serenity.injection.modules.providers

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import us.nineworlds.serenity.SerenityApplication
import us.nineworlds.serenity.common.android.injection.ApplicationContext
import javax.inject.Inject
import javax.inject.Provider

class DataSourceFactoryProvider : Provider<DataSource.Factory> {
    @Inject
    @field:ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var httpDataSourceFactory: HttpDataSource.Factory
    override fun get(): DataSource.Factory {
        val bandwidthMeter = DefaultBandwidthMeter.getSingletonInstance(context)
        val defaultDataSourceFactory = DefaultDataSourceFactory(context, bandwidthMeter, httpDataSourceFactory)
        return CacheDataSource.Factory()
                .setCache(SerenityApplication.simpleCache)
                .setUpstreamDataSourceFactory(defaultDataSourceFactory)
    }
}
