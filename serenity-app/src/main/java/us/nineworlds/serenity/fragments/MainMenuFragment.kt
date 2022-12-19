/**
 * The MIT License (MIT)
 * Copyright (c) 2012, 2021 David Carver
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
package us.nineworlds.serenity.fragments

import us.nineworlds.serenity.fragments.mainmenu.MainMenuView
import javax.inject.Inject
import us.nineworlds.serenity.fragments.mainmenu.MainMenuPresenter
import us.nineworlds.serenity.R
import androidx.leanback.widget.HorizontalGridView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentContainerView
import us.nineworlds.serenity.MainMenuTextViewAdapter
import us.nineworlds.serenity.core.menus.MenuItem
import us.nineworlds.serenity.events.MainMenuEvent
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer
import java.util.ArrayList
import javax.inject.Provider
import moxy.ktx.moxyPresenter
import us.nineworlds.serenity.MainActivity
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.CategoryVideoInfo
import us.nineworlds.serenity.core.model.VideoCategory
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.injection.InjectingMvpFragment

class MainMenuFragment : InjectingMvpFragment(), MainMenuView {

    internal val presenter by moxyPresenter {  presenterProvider.get()  }

    @Inject
    lateinit var presenterProvider: Provider<MainMenuPresenter>

    lateinit var mainGallery: HorizontalGridView

    internal var menuItems: List<MenuItem> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_menu_view, container, false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        fetchData()
    }

    private fun fetchData() {
        val activity: Activity = requireActivity()
        val dataLoadingContainer = activity.findViewById<FrameLayout>(R.id.data_loading_container)
        if (dataLoadingContainer != null) {
            dataLoadingContainer.visibility = View.VISIBLE
        }
        presenter.retrieveMainMenu()
    }

    private fun setupGallery() {
        mainGallery = requireActivity().findViewById<HorizontalGridView>(R.id.mainGalleryMenu)
        val adapter = MainMenuTextViewAdapter(presenter)
        mainGallery.adapter = adapter
        mainGallery.visibility = View.VISIBLE
        adapter.updateMenuItems(menuItems)
        val activity: Activity = requireActivity()
        val dataLoadingContainer = activity.findViewById<FrameLayout>(R.id.data_loading_container)
        if (dataLoadingContainer != null) {
            dataLoadingContainer.visibility = View.GONE
        }
    }

    override fun loadMenu(event: MainMenuEvent) {
        menuItems = MenuMediaContainer(event.mediaContainer).createMenuItems()
        setupGallery()
        mainGallery.visibility = View.VISIBLE
        mainGallery.requestFocusFromTouch()
    }

    override fun loadCategories(categories: CategoryVideoInfo) {
        val container = requireActivity().findViewById<FragmentContainerView>(R.id.video_content_fragment)

        val videoContainer = container.getFragment<VideoContentVerticalGridFragment>()

        videoContainer.clearGallery()

        videoContainer.setupGallery(categories)
    }

    override fun updateCategories(category: CategoryInfo, items: List<VideoCategory>) {
        val container = requireActivity().findViewById<FragmentContainerView>(R.id.video_content_fragment)

        val videoContainer = container.getFragment<VideoContentVerticalGridFragment>()

        videoContainer.updateCategory(category, items)
    }

    override fun clearCategories() {
        val container = requireActivity().findViewById<FragmentContainerView>(R.id.video_content_fragment)

        val videoContainer = container.getFragment<VideoContentVerticalGridFragment>()

        videoContainer.clearGallery()
    }

    override fun showLoading() {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.dataLoadingContainer.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.dataLoadingContainer.visibility = View.GONE
    }

}
