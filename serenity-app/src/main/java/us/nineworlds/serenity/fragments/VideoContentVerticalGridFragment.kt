package us.nineworlds.serenity.fragments

import android.os.Bundle
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.*
import us.nineworlds.serenity.core.model.CategoryInfo
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.ui.leanback.search.CardPresenter

class VideoContentVerticalGridFragment : RowsSupportFragment() {

    val posterInfo = MoviePosterInfo().apply {
        title = "Movie"
        studio = "WB"
        imageURL = "https://upload.wikimedia.org/wikipedia/commons/b/b4/JPEG_example_JPG_RIP_100.jpg"
    }
    val content = listOf(posterInfo)
    val sampleCategories = mapOf<String, List<VideoContentInfo>>("category1" to content, "category2" to content)

    private val videoContentRowsPresenter = ListRowPresenter()
    private val videoContentAdapter = ArrayObjectAdapter(videoContentRowsPresenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = videoContentAdapter

    }

    fun clearGallery() = videoContentAdapter.clear()

    fun updateCategory(categoryInfo: CategoryInfo, contentList: List<VideoContentInfo>) {
        val rows = videoContentAdapter.unmodifiableList<ListRow>()
        val row = rows.find { row ->
            row.headerItem.name == categoryInfo.category &&
                    row.adapter.size() == 0
        }
        row?.let { row ->
            val adapter = row.adapter as ArrayObjectAdapter
            adapter.setItems(contentList, object : DiffCallback<VideoContentInfo>() {
                override fun areItemsTheSame(oldItem: VideoContentInfo, newItem: VideoContentInfo): Boolean {
                    return oldItem.id() == newItem.id()
                }

                override fun areContentsTheSame(oldItem: VideoContentInfo, newItem: VideoContentInfo): Boolean {
                    return oldItem.equals(newItem)
                }

            } )
        }
    }

    fun setupGallery(categories: Map<String, List<VideoContentInfo>>) {
        for ((category, contentList) in categories) {
            val listContentRowAdapter = ArrayObjectAdapter(CardPresenter(requireContext()))
            listContentRowAdapter.addAll(0, contentList)

            val header = HeaderItem(category)
            val imageListRow = ListRow(header, listContentRowAdapter)

            videoContentAdapter.add(imageListRow)
        }
    }
}