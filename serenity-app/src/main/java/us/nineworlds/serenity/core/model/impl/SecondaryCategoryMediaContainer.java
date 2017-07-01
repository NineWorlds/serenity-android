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

package us.nineworlds.serenity.core.model.impl;

import java.util.ArrayList;
import java.util.List;

import us.nineworlds.plex.rest.model.impl.Directory;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.serenity.core.model.SecondaryCategoryInfo;

public class SecondaryCategoryMediaContainer extends AbstractMediaContainer {

    private List<SecondaryCategoryInfo> categories = new ArrayList<SecondaryCategoryInfo>();

    protected String parentCategoryKey;

    public SecondaryCategoryMediaContainer(MediaContainer mc,
                                           String parentCategoryKey) {
        super(mc);
        this.parentCategoryKey = parentCategoryKey;
    }

    public List<SecondaryCategoryInfo> createCategories() {
        populateSecondaryCategories();
        return categories;
    }

    protected void populateSecondaryCategories() {
        List<Directory> dirs = mc.getDirectories();
        categories = new ArrayList<SecondaryCategoryInfo>();
        for (Directory dir : dirs) {
            SecondaryCategoryInfo category = new SecondaryCategoryInfo();
            category.setCategory(dir.getKey());
            category.setCategoryDetail(dir.getTitle());
            category.setParentCategory(parentCategoryKey);
            categories.add(category);
        }
    }

}
