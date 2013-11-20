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

package us.nineworlds.serenity.ui.activity;

import android.os.Bundle;

/**
 * A activity that handles the indicator of whether toggling
 * between Grid and Detail view should occur.  Views
 * that need to support Detail and Grid view should extend
 * this view.
 * 
 * @author dcarver
 *
 */
public abstract class SerenityMultiViewVideoActivity extends
		SerenityVideoActivity {

	protected boolean gridViewActive = false;
	
	protected boolean posterLayoutActive = false;
	
	protected String savedCategory;

	protected String savedSelectedGenreCategory;
	
	@Override
	public void finish() {
		super.finish();
		savedCategory = null;
	}

    public boolean isGridViewActive() {
    	return gridViewActive;
    }
    
    /**
     * Used to indicate whether posters or banners are shown.
     * 
     * @return
     */
    public boolean isPosterLayoutActive() {
    	return posterLayoutActive;
    }
    
    @Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		
		savedCategory = savedInstanceState.getString("savedCategory");
		savedSelectedGenreCategory = savedInstanceState.getString("savedSelectedGenreCategory");
	}
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		if (savedCategory != null) {
			outState.putString("savedCategory", savedCategory );
		}
		if (savedSelectedGenreCategory != null) {
			outState.putString("savedSelectedGenreCategory", savedSelectedGenreCategory);
			
		}
	}

	public void setGridViewEnabled(boolean sw) {
    	gridViewActive = sw;
    }

	public void setPosterLayoutActive(boolean sw) {
    	posterLayoutActive = sw;
    }

	public void setSavedCategory(String category) {
		savedCategory = category;
	}
	
	public void setSavedSelectedGenreCategory(String category) {
		savedSelectedGenreCategory = category;
	}
	
	public String retrieveSavedSelectedGenreCategory() {
		return savedSelectedGenreCategory;
	}
    
}
