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
package us.nineworlds.serenity.fragments

import us.nineworlds.serenity.fragments.mainmenu.MainMenuView
import javax.inject.Inject
import us.nineworlds.serenity.fragments.mainmenu.MainMenuPresenter
import butterknife.Unbinder
import butterknife.BindView
import us.nineworlds.serenity.R
import androidx.leanback.widget.HorizontalGridView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import butterknife.ButterKnife
import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import us.nineworlds.serenity.MainMenuTextViewAdapter
import us.nineworlds.serenity.core.menus.MenuItem
import us.nineworlds.serenity.events.MainMenuEvent
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer
import java.util.ArrayList
import javax.inject.Provider
import moxy.ktx.moxyPresenter
import us.nineworlds.serenity.injection.InjectingMvpFragment

class MainMenuFragment : InjectingMvpFragment(), MainMenuView {

    internal val presenter by moxyPresenter {  presenterProvider.get()  }

    @Inject
    lateinit var presenterProvider: Provider<MainMenuPresenter>

    private lateinit var unbinder: Unbinder
    lateinit var mainGallery: HorizontalGridView

    internal var menuItems: List<MenuItem> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main_menu_view, container, false)
        unbinder = ButterKnife.bind(this, view)
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
        //mainGallery.windowAlignment = HorizontalGridView.WINDOW_ALIGN_BOTH_EDGE
        val adapter = MainMenuTextViewAdapter()
        mainGallery.adapter = adapter
        mainGallery.visibility = View.VISIBLE
        adapter.updateMenuItems(menuItems)
        val activity: Activity = requireActivity()
        val dataLoadingContainer = activity.findViewById<FrameLayout>(R.id.data_loading_container)
        if (dataLoadingContainer != null) {
            dataLoadingContainer.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    override fun loadMenu(event: MainMenuEvent) {
        menuItems = MenuMediaContainer(event.mediaContainer).createMenuItems()
        setupGallery()
        mainGallery.visibility = View.VISIBLE
        mainGallery.requestFocusFromTouch()
    }

    init {
        retainInstance = false
    }
}