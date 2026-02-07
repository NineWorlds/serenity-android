package us.nineworlds.serenity.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlinx.coroutines.launch
import toothpick.Toothpick
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.CategoryVideoInfo
import us.nineworlds.serenity.core.model.VideoCategory
import us.nineworlds.serenity.ui.activity.leanback.details.DetailsActivity
import us.nineworlds.serenity.ui.leanback.adapters.SerenityPagingDataAdapter
import us.nineworlds.serenity.ui.leanback.presenters.CategoryVideoPresenter
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils

class MainMenuVideoContentVerticalGridFragment : RowsSupportFragment() {

    @Inject
    lateinit var vpUtils: VideoPlayerIntentUtils

    private val videoContentRowsPresenter = ListRowPresenter()
    private val videoContentAdapter = ArrayObjectAdapter(videoContentRowsPresenter)
    private val adapterMap = ConcurrentHashMap<String, SerenityPagingDataAdapter<VideoCategory>>()

    private val videoCategoryDiffCallback = object : DiffUtil.ItemCallback<VideoCategory>() {
        override fun areItemsTheSame(oldItem: VideoCategory, newItem: VideoCategory): Boolean =
            oldItem.item.id() == newItem.item.id()

        override fun areContentsTheSame(oldItem: VideoCategory, newItem: VideoCategory): Boolean =
            oldItem == newItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
        super.onCreate(savedInstanceState)
        adapter = videoContentAdapter

        setOnItemViewClickedListener { _, item, _, _ ->
            if (item == null) {
                return@setOnItemViewClickedListener
            }
            val videoCategory = item as VideoCategory

            when {
                videoCategory.item.getType() == Types.EPISODE -> {
                    vpUtils.playVideo(requireActivity(), videoCategory.item, false)
                }

                videoCategory.type == Types.MOVIES -> {
                    val itemId = videoCategory.item.id()
                    val type = "movies"
                    val intent = Intent(requireActivity(), DetailsActivity::class.java)
                    intent.putExtra("itemId", itemId)
                    intent.putExtra("videoType", type)
                    requireActivity().startActivity(intent)
                }

                videoCategory.type == Types.SERIES -> {
                    val itemId = videoCategory.item.id()
                    val type = "tvshows"
                    val intent = Intent(requireActivity(), DetailsActivity::class.java)
                    intent.putExtra("itemId", itemId)
                    intent.putExtra("videoType", type)
                    requireActivity().startActivity(intent)
                }
            }
        }

        setOnItemViewSelectedListener { _, item, _, _ ->
            item?.let {
                val videoCategory = item as VideoCategory

                val imageView = requireActivity().findViewById<ImageView>(R.id.mainGalleryBackground)

                Glide.with(requireActivity()).load(videoCategory.item.getBackgroundURL()).transition(DrawableTransitionOptions.withCrossFade()).fitCenter().into(imageView)
            }
        }
    }

    fun clearGallery() {
        adapterMap.clear()
        videoContentAdapter.clear()
    }

    fun updateCategory(categoryInfo: CategoryInfo, pagingData: PagingData<VideoCategory>) {
        adapterMap[categoryInfo.categoryDetail]?.let { adapter ->
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(pagingData)
            }
        }
    }

    fun setupGallery(categories: CategoryVideoInfo) {
        adapterMap.clear()
        videoContentAdapter.clear()
        for (category in categories.categories) {
            val listContentRowAdapter = SerenityPagingDataAdapter(CategoryVideoPresenter(), videoCategoryDiffCallback)
            category.categoryDetail?.let { detail ->
                adapterMap[detail] = listContentRowAdapter
            }

            val header = HeaderItem(category.categoryDetail)
            val imageListRow = ListRow(header, listContentRowAdapter)

            videoContentAdapter.add(imageListRow)
        }
    }
}
