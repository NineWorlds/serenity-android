package us.nineworlds.serenity.ui.activity.leanback.details

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.leanback.app.DetailsSupportFragment
import moxy.MvpDelegateHolder
import moxy.ktx.moxyPresenter
import toothpick.Toothpick
import us.nineworlds.serenity.common.annotations.InjectionConstants
import javax.inject.Inject
import javax.inject.Provider
import moxy.MvpDelegate
import us.nineworlds.serenity.GlideApp
import us.nineworlds.serenity.core.model.ContentInfo

import android.content.res.Resources

import android.view.LayoutInflater
import android.view.ViewGroup.MarginLayoutParams
import androidx.leanback.widget.*

import us.nineworlds.serenity.core.model.SeriesContentInfo
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.EpisodePosterInfo
import us.nineworlds.serenity.ui.leanback.presenters.EpisodeVideoPresenter
import us.nineworlds.serenity.ui.leanback.presenters.SeriesPresenter
import us.nineworlds.serenity.ui.util.VideoPlayerIntentUtils

import androidx.leanback.widget.ItemAlignmentFacet.ItemAlignmentDef
import us.nineworlds.serenity.R
import us.nineworlds.serenity.ui.leanback.presenters.DetailsOverviewRow
import us.nineworlds.serenity.ui.leanback.presenters.FullWidthDetailsOverviewRowPresenter


class DetailsFragment: DetailsSupportFragment(), MvpDelegateHolder, DetailsView {

    internal val presenter by moxyPresenter {  presenterProvider.get()  }

    @Inject
    lateinit var presenterProvider: Provider<DetailsMVPPresenter>

    @Inject
    lateinit var vpUtils: VideoPlayerIntentUtils

    private var stateSaved = false

    private lateinit var mvpDelegate: MvpDelegate<out DetailsFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()

        getMvpDelegate().onCreate(savedInstanceState);
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

        //We leave the screen and respectively all fragments will be destroyed
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
        Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
    }

    fun setup(itemId: String, type: String) {
        presenter.loadItem(itemId, type)

        setOnItemViewClickedListener { itemViewHolder, item, rowViewHolder, row ->
            if (item is EpisodePosterInfo) {
                vpUtils.playVideo(requireActivity(), item, false)
            }
        }
    }

    override fun setupPresenter(rowPresenter: Presenter?) {
//        if (rowPresenter is us.nineworlds.serenity.ui.leanback.presenters.FullWidthDetailsOverviewRowPresenter) {
//            setupDetailsOverviewRowPresenter(rowPresenter as us.nineworlds.serenity.ui.leanback.presenters.FullWidthDetailsOverviewRowPresenter?)
//        }
    }

    fun setupDetailsOverviewRowPresenter(presenter: us.nineworlds.serenity.ui.leanback.presenters.FullWidthDetailsOverviewRowPresenter?) {
//        presenter!!.setFacet(ItemAlignmentFacet::class.java, facet)
    }

    override fun getMvpDelegate(): MvpDelegate<*> {
        if (!this::mvpDelegate.isInitialized) {
            mvpDelegate = MvpDelegate(this)
        }
        return mvpDelegate
    }

    private val classPresenterSelector = ClassPresenterSelector()

    override fun updateDetails(videoInfo: ContentInfo) {
        val imageView = requireActivity().findViewById<ImageView>(R.id.detail_background_image)

        GlideApp.with(requireActivity()).load(videoInfo.backgroundURL).fitCenter().into(imageView)

        val rowsPresenter = FullWidthDetailsOverviewRowPresenter(SeriesPresenter())
        rowsPresenter.initialState = FullWidthDetailsOverviewRowPresenter.STATE_SMALL

        classPresenterSelector.addClassPresenter(DetailsOverviewRow::class.java, rowsPresenter)

        val detailsAdapter = ArrayObjectAdapter(classPresenterSelector)
        adapter = detailsAdapter

        val detailsOverViewRow = DetailsOverviewRow(videoInfo)

        detailsAdapter.add(detailsOverViewRow)
    }

    override fun addSeasons(videoInfo: List<SeriesContentInfo>) {
        videoInfo.forEach { season ->
            classPresenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
            val seasonAdapter = ArrayObjectAdapter(EpisodeVideoPresenter())
            seasonAdapter.addAll(0, emptyList<VideoContentInfo>())

            val seasonHeader = HeaderItem(season.title)
            val seasonRow = ListRow(seasonHeader, seasonAdapter)

            val detailsAdapter = adapter as ArrayObjectAdapter
            detailsAdapter.add(seasonRow)
        }
    }

    override fun updateSeasonEpisodes(season: SeriesContentInfo, episodes: List<VideoContentInfo>) {
        val detailsAdapter = adapter as ArrayObjectAdapter
        val content = detailsAdapter.unmodifiableList<Row>()
        content.filterIsInstance<ListRow>()
                .filter { listRow -> listRow.headerItem.name == season.title }
                .forEach { row ->
                    val adapter = row.adapter as ArrayObjectAdapter
                    adapter.setItems(episodes, object : DiffCallback<VideoContentInfo>() {
                        override fun areItemsTheSame(oldItem: VideoContentInfo, newItem: VideoContentInfo): Boolean {
                            return oldItem.season == newItem.season &&
                                   oldItem.seasonNumber == newItem.seasonNumber &&
                                   oldItem.parentKey == newItem.parentKey

                        }

                        override fun areContentsTheSame(oldItem: VideoContentInfo, newItem: VideoContentInfo): Boolean {
                            return oldItem.equals(newItem)
                        }
                    })
                }
    }

    inner class VideoLogoPresenter : us.nineworlds.serenity.ui.leanback.presenters.DetailsOverviewLogoPresenter() {

        override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder? {
            val imageView = LayoutInflater.from(parent.context)
                    .inflate(us.nineworlds.serenity.R.layout.lb_fullwidth_details_overview_logo, parent, false) as ImageView
            val res: Resources = parent.resources
            val width: Int = res.getDimensionPixelSize(us.nineworlds.serenity.R.dimen.movie_poster_image_width)
            val height: Int = res.getDimensionPixelSize(us.nineworlds.serenity.R.dimen.movie_poster_image_height)
            imageView.layoutParams = MarginLayoutParams(width, height)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            return ViewHolder(imageView)
        }

        override fun isBoundToImage(viewHolder: us.nineworlds.serenity.ui.leanback.presenters.DetailsOverviewLogoPresenter.ViewHolder?, row: DetailsOverviewRow?): Boolean {
            return true
        }

        override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
            val row = item as DetailsOverviewRow
            val videoInfo = row.item as ContentInfo
            val imageView = viewHolder.view as ImageView

            if (row.item is SeriesContentInfo) {
                val item = row.item as SeriesContentInfo
                GlideApp.with(requireContext()).asDrawable().load(item.thumbNailURL).into(imageView)
            } else {
                GlideApp.with(requireContext()).asDrawable().load(videoInfo.imageURL).into(imageView)
            }

            if (isBoundToImage(viewHolder as ViewHolder, row)) {
                viewHolder.parentPresenter.notifyOnBindLogo(viewHolder.parentViewHolder)
            }
        }

        inner class ViewHolder(view: View) : us.nineworlds.serenity.ui.leanback.presenters.DetailsOverviewLogoPresenter.ViewHolder(view) {
            override fun getParentPresenter(): FullWidthDetailsOverviewRowPresenter {
                return mParentPresenter
            }

            override fun getParentViewHolder(): FullWidthDetailsOverviewRowPresenter.ViewHolder {
                return mParentViewHolder
            }
        }
    }
}

