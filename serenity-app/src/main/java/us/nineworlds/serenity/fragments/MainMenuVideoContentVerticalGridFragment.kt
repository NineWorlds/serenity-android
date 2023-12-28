package us.nineworlds.serenity.fragments

import android.os.Bundle
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import us.nineworlds.serenity.core.model.CategoryInfo
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import toothpick.Toothpick
import us.nineworlds.serenity.GlideApp
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.Types
import us.nineworlds.serenity.core.model.CategoryVideoInfo
import us.nineworlds.serenity.core.model.VideoCategory
import us.nineworlds.serenity.ui.activity.leanback.details.DetailsActivity
import us.nineworlds.serenity.ui.leanback.presenters.CategoryVideoPresenter
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils
import javax.inject.Inject


class MainMenuVideoContentVerticalGridFragment : RowsSupportFragment() {

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

            when {
                videoCategory.item.type == Types.EPISODE -> {
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

        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
            item?.let {
                val videoCategory = item as VideoCategory

                val imageView = requireActivity().findViewById<ImageView>(R.id.mainGalleryBackground)

                GlideApp.with(requireActivity()).load(videoCategory.item.backgroundURL).transition(DrawableTransitionOptions.withCrossFade()).fitCenter().into(imageView)
            }
        }
    }

    fun clearGallery() = videoContentAdapter.clear()

    fun updateCategory(categoryInfo: CategoryInfo, contentList: List<VideoCategory>) {
        val rows = videoContentAdapter.unmodifiableList<ListRow>()
        val row = rows.find { row ->
            row.headerItem.name == categoryInfo.categoryDetail
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

            val header = HeaderItem(category.categoryDetail)
            val imageListRow = ListRow(header, listContentRowAdapter)

            videoContentAdapter.add(imageListRow)
        }
    }
}