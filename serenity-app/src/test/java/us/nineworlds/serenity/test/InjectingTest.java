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

package us.nineworlds.serenity.test;

import org.junit.After;
import org.junit.Before;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;
import us.nineworlds.serenity.common.annotations.InjectionConstants;

public abstract class InjectingTest {

  protected Scope scope;

  @Before public void setUp() throws Exception {
    scope = openScope();
    installTestModules();
    Toothpick.inject(this, scope);
  }

  protected Scope openScope() {
    return Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE);
  }

  @After
  public void closeScope() {
    Toothpick.reset();
  }

  public abstract void installTestModules();
}
