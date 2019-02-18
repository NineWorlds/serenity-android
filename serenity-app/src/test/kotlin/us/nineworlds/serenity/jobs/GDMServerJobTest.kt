package us.nineworlds.serenity.jobs

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.nhaarman.mockito_kotlin.*
import org.assertj.android.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness.STRICT_STUBS
import us.nineworlds.serenity.TestingModule
import us.nineworlds.serenity.test.InjectingTest
import us.nineworlds.serenity.testrunner.PlainAndroidRunner
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException
import javax.inject.Inject

@RunWith(PlainAndroidRunner::class)
class GDMServerJobTest : InjectingTest() {

  @Rule
  @JvmField
  public val rule = MockitoJUnit.rule().strictness(STRICT_STUBS)

  @Mock
  lateinit var mockDatagramPacket: DatagramPacket

  @Mock
  lateinit var mockDatagramSocket: DatagramSocket

  @Mock
  lateinit var mockInetAddress: InetAddress

  @Inject
  lateinit var mockLocalBroadcastManager: LocalBroadcastManager

  lateinit var job: GDMServerJob

  @Before
  override fun setUp() {
    super.setUp()
    job = GDMServerJob()
  }

  @Test
  fun plexServerAcknowledgeResponsePopulatesExpectedBroadcastExtras() {
    doReturn(EXPECTED_VALID_PACKET_RESPONSE.toByteArray()).whenever(mockDatagramPacket).data
    doReturn(mockInetAddress).whenever(mockDatagramPacket).address
    doReturn("127.0.0.1").whenever(mockInetAddress).toString()

    job.listening = true

    val result = job.isReceivingPackets(mockDatagramSocket, mockDatagramPacket)

    assertThat(result).isTrue()

    argumentCaptor<Intent>().apply {
      verify(mockLocalBroadcastManager).sendBroadcast(capture())

      val intent = firstValue
      Assertions.assertThat(intent).hasExtra("data").hasExtra("ipaddress")
      assertThat(intent.getStringExtra("data")).isEqualTo(EXPECTED_VALID_PACKET_RESPONSE)
      assertThat(intent.getStringExtra("ipaddress")).isEqualTo("127.0.0.1")
    }

    verify(mockDatagramSocket).receive(mockDatagramPacket)
  }

  @Test()
  fun whenSocketTimeOutExceptionIsThrownListeningIsSetToFalse() {
    doThrow(SocketTimeoutException("Oops")).whenever(mockDatagramSocket).receive(mockDatagramPacket)
    job.listening = true
    val result = job.isReceivingPackets(mockDatagramSocket, mockDatagramPacket)

    assertThat(result).isFalse()

    verify(mockLocalBroadcastManager).sendBroadcast(any<Intent>())
    verify(mockDatagramSocket).receive(mockDatagramPacket)
  }

  @Test
  fun userMultiCastReturnsExpectedAddress() {
    val result = job.useMultiCastAddress()
    assertThat(result.hostName).isEqualTo("239.0.0.250")
  }

  override fun installTestModules() {
    scope.installTestModules(TestingModule())
  }

  companion object {
    const val EXPECTED_VALID_PACKET_RESPONSE = "HTTP/1.0 200 OK"
  }

}
