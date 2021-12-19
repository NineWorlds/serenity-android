package us.nineworlds.serenity.fragments

import android.os.Bundle
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.ui.leanback.search.CardPresenter
import android.app.Activity
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.CategoryVideoInfo
import us.nineworlds.serenity.core.model.VideoCategory
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo
import us.nineworlds.serenity.ui.leanback.presenters.CategoryVideoPresenter
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils
import javax.inject.Inject


class VideoContentVerticalGridFragment : RowsSupportFragment() {

    @Inject
    lateinit var vpUtils: VideoPlayerIntentUtils

    private val videoContentRowsPresenter = ListRowPresenter()
    private val videoContentAdapter = ArrayObjectAdapter(videoContentRowsPresenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
        super.onCreate(savedInstanceState)
        adapter = videoContentAdapter

        setOnItemViewClickedListener { _, item, _, _ ->
          val videoCategory = item as VideoCategory

          if (videoCategory.type == Types.MOVIES) {
              val video = videoCategory.item
              vpUtils.playVideo(requireActivity(), video, false)
          }
        }
    }

    fun clearGallery() = videoContentAdapter.clear()

    fun updateCategory(categoryInfo: CategoryInfo, contentList: List<VideoCategory>) {
        val rows = videoContentAdapter.unmodifiableList<ListRow>()
        val row = rows.find { row ->
            row.headerItem.name == categoryInfo.category &&
                    row.adapter.size() == 0
        }
        row?.let { row ->
            val adapter = row.adapter as ArrayObjectAdapter
            adapter.setItems(contentList, object : DiffCallback<VideoCategory>() {
                override fun areItemsTheSame(oldItem: VideoCategory, newItem: VideoCategory): Boolean {
                    return oldItem.item.id() == newItem.item.id()
                }

                override fun areContentsTheSame(oldItem: VideoCategory, newItem: VideoCategory): Boolean {
                    return oldItem.equals(newItem)
                }

            } )
        }
    }

    fun setupGallery(categories: CategoryVideoInfo) {
        for (category in categories.categories) {
            val listContentRowAdapter = ArrayObjectAdapter(CategoryVideoPresenter())
            listContentRowAdapter.addAll(0, emptyList<VideoCategory>())

            val header = HeaderItem(category.category)
            val imageListRow = ListRow(header, listContentRowAdapter)

            videoContentAdapter.add(imageListRow)
        }
    }
}