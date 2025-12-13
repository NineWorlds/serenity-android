/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package us.nineworlds.serenity.core.services

import android.content.Intent
import assertk.assertThat
import assertk.assertions.isNullOrEmpty
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.test.InjectingTest

@RunWith(RobolectricTestRunner::class)
class MovieSearchIntentServiceTest : InjectingTest() {
    private lateinit var service: MockMovieSearchIntentService
    private val mockIntent: Intent = mockk<Intent>(relaxed = true)

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        service = MockMovieSearchIntentService()
    }

    @Test
    fun assertThatVideoContentIsEmptyWhenBundleExtrasIsNull() {
        every { mockIntent.extras } returns null
        service.onHandleIntent(mockIntent)
        assertThat(service.videos).isNullOrEmpty()
    }

    inner class MockMovieSearchIntentService : MovieSearchIntentService() {
        public override fun onHandleIntent(intent: Intent?) {
            super.onHandleIntent(intent)
        }

        val videos: MutableList<VideoContentInfo?>?
            get() = videoContentList
    }

    override fun installTestModules() {
        scope.installTestModules(MockkTestingModule())
    }
}
