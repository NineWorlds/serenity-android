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

package us.nineworlds.serenity.core.menus

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import org.junit.Before
import org.junit.Test
import us.nineworlds.serenity.core.menus.MenuItem

class MenuItemTest {

    private lateinit var menuItem: MenuItem

    @Before
    fun setUp() {
        menuItem = MenuItem()
    }

    @Test
    fun `type returns value set`() {
        menuItem.type = "movie"
        assertThat(menuItem.type).isEqualTo("movie")
    }

    @Test
    fun `title returns expected value set`() {
        menuItem.title = "Movies"
        assertThat(menuItem.title).isEqualTo("Movies")
    }

    @Test
    fun `section returns expected value set`() {
        menuItem.section = "1234"
        assertThat(menuItem.section).isEqualTo("1234")
    }

    @Test
    fun `toString returns empty when no title has been set`() {
        assertThat(menuItem.toString()).isEmpty()
    }

    @Test
    fun `toString returns generated value`() {
        menuItem.title = "To String!"
        assertThat(menuItem.toString()).isEqualTo("To String!")
    }
}
