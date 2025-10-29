package us.nineworlds.serenity.ui.adapters

import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEmpty
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode
import toothpick.config.Module
import us.nineworlds.serenity.MainActivity
import us.nineworlds.serenity.MockkTestingModule
import us.nineworlds.serenity.core.model.VideoContentInfo
import us.nineworlds.serenity.core.model.impl.MoviePosterInfo
import us.nineworlds.serenity.injection.ForVideoQueue
import us.nineworlds.serenity.test.InjectingTest
import java.util.LinkedList

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "large")
@LooperMode(LooperMode.Mode.LEGACY)
class AbstractPosterImageGalleryAdapterTest : InjectingTest() {

  private lateinit var abstractPosterImageGalleryAdapter: AbstractPosterImageGalleryAdapter
  private lateinit var activity: AppCompatActivity

  @Before
  override fun setUp() {
    super.setUp()
    Robolectric.getBackgroundThreadScheduler().pause()
    Robolectric.getForegroundThreadScheduler().pause()

    activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
    abstractPosterImageGalleryAdapter = FakePosterImageGalleryAdapter()
  }

  @After
  fun tearDown() {
    activity.finish()
  }

  @Test
  fun getItemReturnsExpectedInstance() {
    assertThat(abstractPosterImageGalleryAdapter.getItem(0)).isInstanceOf(MoviePosterInfo::class)
  }

  @Test
  fun itemIdReturnsExpectedValueOfZero() {
    assertThat(abstractPosterImageGalleryAdapter.getItemId(0)).isEqualTo(0)
  }

  @Test
  fun getItemsReturnsANonEmptyListOfItems() {
    assertThat(abstractPosterImageGalleryAdapter.getItems()).isNotEmpty()
  }

  inner class FakePosterImageGalleryAdapter : AbstractPosterImageGalleryAdapter() {

    init {
      posterList = listOf<VideoContentInfo>(MoviePosterInfo())
    }

    fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
      return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return mockk()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    fun onItemViewClick(view: View, i: Int) {}

    fun onItemViewFocusChanged(b: Boolean, view: View, i: Int) {}
  }

  override fun installTestModules() {
    scope.installTestModules(MockkTestingModule(), TestModule())
  }

  inner class TestModule : Module() {
    init {
      bind(SharedPreferences::class.java).toInstance(PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext()))
      bind(LinkedList::class.java).withName(ForVideoQueue::class.java).toInstance(LinkedList<Any>())
      bind(Resources::class.java).toInstance(ApplicationProvider.getApplicationContext<android.content.Context>().resources)
    }
  }
}
