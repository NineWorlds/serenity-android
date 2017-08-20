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

package us.nineworlds.serenity.common.injection

import dagger.ObjectGraph

class SerenityObjectGraph private constructor() {

  var objectGraph: ObjectGraph? = null

  fun inject(injectionPoint: Any) {
    if (objectGraph == null) {
      throw RuntimeException("ObjectGraph has not yet been created. Create a ObjectGraph with Modules.")
    }
    objectGraph!!.inject(injectionPoint)
  }

  /**
   * Creates an entirely new ObjectGraph for the application based on the
   * provides list of modules.

   * @param modules List of Dagger Modules to use to construct the new object
   * * graph.
   */
  fun createObjectGraph(modules: List<Any>) {
    objectGraph = ObjectGraph.create(*modules.toTypedArray())
  }

  companion object {

    private var serenityObjectGraph: SerenityObjectGraph? = null

    val instance: SerenityObjectGraph
      get() {
        if (serenityObjectGraph == null) {
          synchronized(SerenityObjectGraph::class.java) {
            serenityObjectGraph = SerenityObjectGraph()
          }
        }
        return serenityObjectGraph!!
      }
  }
}
