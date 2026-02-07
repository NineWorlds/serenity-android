package us.nineworlds.serenity.ui.activity.leanback.details

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.SparseArrayObjectAdapter
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.ktx.moxyPresenter
import toothpick.Toothpick
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.core.model.ContentInfo
import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo
import us.nineworlds.serenity.core.model.impl.TVShowSeriesInfo
import us.nineworlds.serenity.ui.activity.leanback.details.DetailsActivity
import us.nineworlds.serenity.ui.leanback.adapters.SerenityPagingDataAdapter
import us.nineworlds.serenity.ui.leanback.presenters.DetailsOverviewRow
import us.nineworlds.serenity.ui.leanback.presenters.EpisodeVideoPresenter
import us.nineworlds.serenity.ui.leanback.presenters.FullWidthDetailsOverviewRowPresenter
import us.nineworlds.serenity.ui.leanback.presenters.MoviePresenter
import us.nineworlds.serenity.ui.leanback.presenters.SeriesPresenter
import us.nineworlds.serenity.ui.leanback.presenters.VideoContentInfoPresenter
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils

class DetailsFragment :
    DetailsSupportFragment(),
    MvpDelegateHolder,
    DetailsView {

    internal val presenter by moxyPresenter { presenterProvider.get() }

    @Inject
    lateinit var presenterProvider: Provider<DetailsMVPPresenter>

    @Inject
    lateinit var vpUtils: VideoPlayerIntentUtils

    private var stateSaved = false

    private lateinit var mvpDelegate: MvpDelegate<out DetailsFragment>

    private val videoContentDiffCallback = object : DiffUtil.ItemCallback<VideoContentInfo>() {
        override fun areItemsTheSame(oldItem: VideoContentInfo, newItem: VideoContentInfo): Boolean =
            oldItem.id() == newItem.id()

        override fun areContentsTheSame(oldItem: VideoContentInfo, newItem: VideoContentInfo): Boolean =
            oldItem == newItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

        getMvpDelegate().onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        stateSaved = false

        getMvpDelegate().onAttach()
    }

    override fun onResume() {
        super.onResume()

        stateSaved = false

        getMvpDelegate().onAttach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        stateSaved = true

        getMvpDelegate().onSaveInstanceState(outState)
        getMvpDelegate().onDetach()
    }

    override fun onStop() {
        super.onStop()

        getMvpDelegate().onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        getMvpDelegate().onDetach()
        getMvpDelegate().onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        // We leave the screen and respectively all fragments will be destroyed
        if (requireActivity().isFinishing) {
            getMvpDelegate().onDestroy()
            return
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (isStateSaved) {
            stateSaved = false
            return
        }

        var anyParentIsRemoving = false
        var parent: Fragment? = parentFragment
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving
            parent = parent.parentFragment
        }

        if (isRemoving || anyParentIsRemoving) {
            getMvpDelegate().onDestroy()
        }
    }

    fun inject() {
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
    }

    fun setup(itemId: String, type: String) {
        presenter.loadItem(itemId, type)

        setOnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is EpisodePosterInfo) {
                vpUtils.playVideo(requireActivity(), item, false)
            } else if (item is VideoContentInfo) {
                val itemId = item.id()
                val type = "movies"
                val intent = Intent(requireActivity(), DetailsActivity::class.java)
                intent.putExtra("itemId", itemId)
                intent.putExtra("videoType", type)
                requireActivity().startActivity(intent)
            }
        }
    }

    override fun setupPresenter(rowPresenter: Presenter?) = Unit

    override fun getMvpDelegate(): MvpDelegate<*> {
        if (!this::mvpDelegate.isInitialized) {
            mvpDelegate = MvpDelegate(this)
        }
        return mvpDelegate
    }

    private val classPresenterSelector = ClassPresenterSelector()

    fun setupTVShowDetails(videoInfo: ContentInfo) {
        val rowsPresenter = FullWidthDetailsOverviewRowPresenter(SeriesPresenter())
        rowsPresenter.initialState = FullWidthDetailsOverviewRowPresenter.STATE_FULL

        classPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, rowsPresenter)

        val detailsAdapter = ArrayObjectAdapter(classPresenterSelector)
        adapter = detailsAdapter

        val detailsOverViewRow = DetailsOverviewRow(videoInfo)

        detailsAdapter.add(detailsOverViewRow)
    }

    companion object {
        const val PLAY_ACTION = 0L
    }

    fun setupMovieDetails(videoInfo: ContentInfo) {
        val rowsPresenter = FullWidthDetailsOverviewRowPresenter(MoviePresenter())
        rowsPresenter.initialState = FullWidthDetailsOverviewRowPresenter.STATE_FULL

        rowsPresenter.setOnActionClickedListener { action ->
            if (action.id == PLAY_ACTION) {
                vpUtils.playVideo(requireActivity(), videoInfo as VideoContentInfo, false)
            }
        }

        classPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, rowsPresenter)

        val detailsAdapter = ArrayObjectAdapter(classPresenterSelector)
        adapter = detailsAdapter

        val detailsOverViewRow = DetailsOverviewRow(videoInfo)
        val actionsAdapter = SparseArrayObjectAdapter()
        actionsAdapter.apply {
            set(PLAY_ACTION.toInt(), Action(PLAY_ACTION, "Play"))
        }

        detailsOverViewRow.actionsAdapter = actionsAdapter

        detailsAdapter.add(detailsOverViewRow)
    }

    override fun updateDetails(videoInfo: ContentInfo) {
        val imageView = requireActivity().findViewById<ImageView>(R.id.detail_background_image)

        Glide.with(requireActivity()).load(videoInfo.getBackgroundURL()).fitCenter().into(imageView)

        when (videoInfo) {
            is TVShowSeriesInfo -> setupTVShowDetails(videoInfo)
            is VideoContentInfo -> setupMovieDetails(videoInfo)
        }
    }

    override fun addSeasons(videoInfo: List<SeriesContentInfo>) {
        videoInfo.forEach { season ->
            classPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
            val seasonAdapter = SerenityPagingDataAdapter(EpisodeVideoPresenter(), videoContentDiffCallback)

            val seasonHeader = HeaderItem(season.getTitle())
            val seasonRow = ListRow(seasonHeader, seasonAdapter)

            val detailsAdapter = adapter as ArrayObjectAdapter
            detailsAdapter.add(seasonRow)
        }
    }

    override fun updateSeasonEpisodes(season: SeriesContentInfo, pagingData: PagingData<VideoContentInfo>) {
        val detailsAdapter = adapter as ArrayObjectAdapter
        val content = detailsAdapter.unmodifiableList<Row>()
        content.filterIsInstance<ListRow>()
            .filter { listRow -> listRow.headerItem.name == season.getTitle() }
            .forEach { row ->
                val adapter = row.adapter as SerenityPagingDataAdapter<VideoContentInfo>
                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(pagingData)
                }
            }
    }

    override fun addSimilarItems(pagingData: PagingData<VideoContentInfo>) {
        classPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
        val itemsAdapter = SerenityPagingDataAdapter(VideoContentInfoPresenter(), videoContentDiffCallback)

        val header = HeaderItem("Similar")
        val similarRow = ListRow(header, itemsAdapter)

        val detailsAdapter = adapter as ArrayObjectAdapter
        detailsAdapter.add(similarRow)

        viewLifecycleOwner.lifecycleScope.launch {
            itemsAdapter.submitData(pagingData)
        }
    }

    override fun addSimilarSeries(videoInfo: List<SeriesContentInfo>) {
        TODO("Not yet implemented")
    }
}
