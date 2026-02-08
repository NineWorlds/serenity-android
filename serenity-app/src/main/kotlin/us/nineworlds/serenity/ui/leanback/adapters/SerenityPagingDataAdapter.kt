package us.nineworlds.serenity.ui.leanback.adapters

import androidx.leanback.paging.PagingDataAdapter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil

/**
 * A generic PagingDataAdapter for Serenity that integrates Paging 3 with Leanback.
 * This class wraps androidx.leanback.paging.PagingDataAdapter as it is final and cannot be inherited from.
 */
class SerenityPagingDataAdapter<T : Any>(presenterSelector: PresenterSelector, diffCallback: DiffUtil.ItemCallback<T>) : ObjectAdapter(presenterSelector) {

    private val internalAdapter = PagingDataAdapter(presenterSelector, diffCallback)

    private val dataObserver = object : DataObserver() {
        override fun onChanged() {
            notifyChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            notifyItemRangeRemoved(positionStart, itemCount)
        }
    }

    init {
        internalAdapter.registerObserver(dataObserver)
    }

    constructor(presenter: Presenter, diffCallback: DiffUtil.ItemCallback<T>) :
        this(
            object : PresenterSelector() {
                override fun getPresenter(item: Any?): Presenter = presenter
            },
            diffCallback
        )

    override fun size(): Int = internalAdapter.size()

    override fun get(position: Int): Any? {
        if (position < 0 || position >= size()) {
            return null
        }
        return try {
            internalAdapter.get(position)
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    suspend fun submitData(pagingData: PagingData<T>) {
        internalAdapter.submitData(pagingData)
    }
}
