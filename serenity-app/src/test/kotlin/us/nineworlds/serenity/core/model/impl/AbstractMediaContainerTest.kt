package us.nineworlds.serenity.core.model.impl

import android.content.res.Resources
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import io.mockk.clearAllMocks
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import toothpick.Toothpick
import toothpick.config.Module
import us.nineworlds.serenity.common.media.model.IMediaContainer
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.test.InjectingTest

class AbstractMediaContainerTest : InjectingTest() {

  private val mockMediaContainer: IMediaContainer = mockk()
  private val mockSerenityClient: SerenityClient = mockk(relaxed = true)

  private lateinit var mediaContainer: TestMediaContainer

  override fun installTestModules() {
    scope.installTestModules(TestModule())
  }

  @Before
  override fun setUp() {
    super.setUp()
    mediaContainer = TestMediaContainer(mockMediaContainer)
  }

  @After
  fun tearDown() {
    Toothpick.reset()
    clearAllMocks()
  }

  @Test
  fun `videoList is initialized as null`() {
    assertThat(mediaContainer.videoList).isNull()
  }

  @Test
  fun `media container is correctly assigned from constructor`() {
    assertThat(mediaContainer.getMediaContainer()).isEqualTo(mockMediaContainer)
  }

  @Test
  fun `factory is injected successfully`() {
    assertThat(mediaContainer.factory).isNotNull()
    assertThat(mediaContainer.factory).isEqualTo(mockSerenityClient)
  }

  private class TestMediaContainer(mc: IMediaContainer) : AbstractMediaContainer(mc) {
    fun getMediaContainer(): IMediaContainer = mc
  }

  inner class TestModule : Module() {
    init {
      bind(SerenityClient::class.java).toInstance(mockSerenityClient)
      bind(Resources::class.java).toInstance(mockk())
    }
  }
}
