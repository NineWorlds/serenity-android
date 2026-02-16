package us.nineworlds.serenity.emby.server.api

import okhttp3.Interceptor
import okhttp3.Response
import okio.buffer
import okio.sink
import java.io.File

class EmbyResponseCaptureInterceptor(private val outputDir: File) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessful) {
            val body = response.peekBody(Long.MAX_VALUE)
            val fileName = request.url.encodedPath.trim('/').replace('/', '_') + ".json"
            val file = File(outputDir, fileName)
            
            file.parentFile?.mkdirs()
            file.sink().buffer().use { it.writeAll(body.source()) }
        }

        return response
    }
}
