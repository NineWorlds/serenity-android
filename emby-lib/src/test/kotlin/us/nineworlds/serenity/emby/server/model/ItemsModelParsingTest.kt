package us.nineworlds.serenity.emby.server.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import us.nineworlds.serenity.emby.moshi.LocalDateJsonAdapter
import java.time.LocalDateTime

class ItemsModelParsingTest {

    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        moshi = Moshi.Builder()
            .add(LocalDateTime::class.java, LocalDateJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Test
    fun testParseItemsQueryResult() {
        val inputStream = javaClass.classLoader?.getResourceAsStream("mock-data/emby_Users_a99be67f778f4ebd82d22ae04153520e_Items.json")
        assertNotNull("Mock file not found", inputStream)
        val json = inputStream!!.bufferedReader().use { it.readText() }

        val adapter = moshi.adapter(QueryResult::class.java)
        val result = adapter.fromJson(json)

        assertNotNull(result)
        assertEquals(37, result!!.items.size)

        val firstItem = result.items[0]
        assertEquals("Dune: Part Two", firstItem.name)
        assertEquals("247", firstItem.id)
        assertEquals("mkv", firstItem.container)
        assertEquals("PG-13", firstItem.officialRating)
        assertTrue(firstItem.overview!!.startsWith("Follow the mythic journey"))

        // Test Genres
        assertNotNull(firstItem.genres)
        assertTrue(firstItem.genres!!.contains("Science Fiction"))
        assertTrue(firstItem.genres!!.contains("Adventure"))

        // Test Studios
        assertNotNull(firstItem.studios)
        assertEquals(1, firstItem.studios!!.size)
        assertEquals("Legendary Pictures", firstItem.studios!![0].name)
        assertEquals("2407", firstItem.studios!![0].id)

        // Test MediaSources
        assertNotNull(firstItem.mediaSources)
        assertEquals(1, firstItem.mediaSources!!.size)
        val source = firstItem.mediaSources!![0]
        assertEquals("mediasource_247", source.id)
        assertTrue(source.directPlay == true)
        assertTrue(source.directStream == true)
        assertTrue(source.transcoding == true)
        assertEquals(5577434246L, source.size)
        assertEquals(4484842L, source.bitrate)

        // Test MediaStreams in MediaSource
        assertNotNull(source.mediaStreams)
        assertEquals(3, source.mediaStreams!!.size)
        val videoStream = source.mediaStreams!![0]
        assertEquals("Video", videoStream.type)
        assertEquals("h264", videoStream.codec)
        assertEquals(1920, videoStream.width)
        assertEquals(804, videoStream.height)

        val audioStream = source.mediaStreams!![1]
        assertEquals("Audio", audioStream.type)
        assertEquals("aac", audioStream.codec)
        assertEquals(8, audioStream.channels)
    }
}
