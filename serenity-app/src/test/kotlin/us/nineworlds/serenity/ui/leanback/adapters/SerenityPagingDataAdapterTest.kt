package us.nineworlds.serenity.ui.leanback.adapters

import android.os.Looper
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.Presenter
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isTrue
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config
class SerenityPagingDataAdapterTest {

    private val mockPresenter: Presenter = mockk(relaxed = true)
    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
    }

    private lateinit var adapter: SerenityPagingDataAdapter<String>

    @Before
    fun setUp() {
        adapter = SerenityPagingDataAdapter(mockPresenter, diffCallback)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun size_returnsInternalAdapterSize() {
        assertThat(adapter.size()).isEqualTo(0)
    }

    @Test
    fun get_returnsNull_whenPositionIsOutOfBounds() {
        assertThat(adapter.get(0)).isNull()
    }

    @Test
    fun constructor_withPresenter_createsPresenterSelector() {
        val presenter = adapter.presenterSelector.getPresenter("test")
        assertThat(presenter).isEqualTo(mockPresenter)
    }

    @Test
    fun submitData_updatesSizeAndItems() = runTest(UnconfinedTestDispatcher()) {
        val pagingData = PagingData.from(listOf("item1", "item2"))

        backgroundScope.launch {
            adapter.submitData(pagingData)
        }

        advanceUntilIdle()
        shadowOf(Looper.getMainLooper()).idle()

        assertThat(adapter.size()).isEqualTo(2)
        assertThat(adapter.get(0)).isEqualTo("item1")
        assertThat(adapter.get(1)).isEqualTo("item2")
    }

    @Test
    fun dataObserver_triggersNotifyChanged() = runTest(UnconfinedTestDispatcher()) {
        var notified = false
        val observer = object : ObjectAdapter.DataObserver() {
            override fun onChanged() {
                notified = true
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                notified = true
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                notified = true
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                notified = true
            }
        }
        adapter.registerObserver(observer)

        val pagingData = PagingData.from(listOf("item1"))
        backgroundScope.launch {
            adapter.submitData(pagingData)
        }

        advanceUntilIdle()
        shadowOf(Looper.getMainLooper()).idle()

        assertThat(notified).isTrue()
    }
}
