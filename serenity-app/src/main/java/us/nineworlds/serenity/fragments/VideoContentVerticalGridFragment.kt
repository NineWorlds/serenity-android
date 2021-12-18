package us.nineworlds.serenity.fragments

import android.os.Bundle
import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
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

        //setupGallery(sampleCategories)

    }

    fun setupGallery(categories: Map<String, List<VideoContentInfo>>) {
        videoContentAdapter.clear()
        for ((category, contentList) in categories) {
            val listContentRowAdapter = ArrayObjectAdapter(CardPresenter(requireContext()))

            listContentRowAdapter.addAll(0, contentList)

            val header = HeaderItem(category)
            val imageListRow = ListRow(header, listContentRowAdapter)

            videoContentAdapter.add(imageListRow)
        }
    }
}