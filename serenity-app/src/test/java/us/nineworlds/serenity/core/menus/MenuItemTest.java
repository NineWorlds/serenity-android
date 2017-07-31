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

package us.nineworlds.serenity.core.menus;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class MenuItemTest {

	MenuItem menuItem;

	@Before
	public void setUp() {
		menuItem = new MenuItem();
	}


	@Test
	public void typeReturnsValueSet() {
		menuItem.setType("movie");
		assertThat(menuItem.getType()).isEqualTo("movie");
	}

	@Test
	public void titleReturnsExpectedValueSet() {
		menuItem.setTitle("Movies");
		assertThat(menuItem.getTitle()).isEqualTo("Movies");
	}

	@Test
	public void sectionReturnsExpectuedValueSet() {
		menuItem.setSection("1234");
		assertThat(menuItem.getSection()).isEqualTo("1234");
	}

	@Test
	public void toStringReturnsNullWhenNoTitleHasBeenSet() {
		assertThat(menuItem.toString()).isEmpty();
	}

	@Test
	public void toStringReturnsGeneratedValue() {
		menuItem.setTitle("To String!");
		assertThat(menuItem.toString()).isEqualTo("To String!");
	}
}
