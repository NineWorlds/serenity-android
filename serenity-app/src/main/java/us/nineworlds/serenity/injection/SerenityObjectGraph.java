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
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.injection;

import java.util.List;

import dagger.ObjectGraph;

public class SerenityObjectGraph {

    private ObjectGraph objectGraph;

    private static SerenityObjectGraph fusionObjectGraph;

    private SerenityObjectGraph() {
    }

    public static SerenityObjectGraph getInstance() {
        if (fusionObjectGraph == null) {
            synchronized (SerenityObjectGraph.class) {
                fusionObjectGraph = new SerenityObjectGraph();
            }
        }
        return fusionObjectGraph;
    }

    public void inject(Object injectionPoint) {
        if (objectGraph == null) {
            throw new RuntimeException(
                    "ObjectGraph has not yet been created. Create a ObjectGraph with Modules.");
        }
        objectGraph.inject(injectionPoint);
    }

    public void setObjectGraph(ObjectGraph objectGraph) {
        this.objectGraph = objectGraph;
    }

    /**
     * Creates an entirely new ObjectGraph for the application based on the
     * provides list of modules.
     *
     * @param modules
     *            List of Dagger Modules to use to construct the new object
     *            graph.
     */
    public void createObjectGraph(List<Object> modules) {
        objectGraph = ObjectGraph.create(modules.toArray());
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }

}
