package us.nineworlds.serenity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife.bind
import com.birbit.android.jobqueue.JobManager
import us.nineworlds.serenity.AndroidTV
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.core.util.StringPreference
import us.nineworlds.serenity.emby.server.EmbyServerJob
import us.nineworlds.serenity.injection.ForMediaServers
import us.nineworlds.serenity.injection.InjectingActivity
import us.nineworlds.serenity.injection.ServerClientPreference
import us.nineworlds.serenity.jobs.GDMServerJob
import us.nineworlds.serenity.ui.activity.login.LoginUserActivity
import javax.inject.Inject

class ServerSelectionActivity : InjectingActivity() {

  companion object {
    const val SERVER_DISPLAY_DELAY = 5000L
  }

  @field:[Inject ForMediaServers]
  lateinit var servers: MutableMap<String, Server>

  @field:[Inject ServerClientPreference]
  lateinit var serverClientPreference: StringPreference

  @Inject
  lateinit var jobManager: JobManager;

  @BindView(R.id.server_container)
  internal lateinit var serverContainer: LinearLayout

  @BindView(R.id.server_loading_progress)
  internal lateinit var serverLoadingProgressBar: ProgressBar

  internal lateinit var refreshButton: FrameLayout

  internal lateinit var serverDisplayHandler: Handler

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_server_selection)
    bind(this)

    serverDisplayHandler = Handler()
    serverDisplayHandler.postDelayed({
      createServerList()
    }, SERVER_DISPLAY_DELAY)
  }

  private fun createServerList() {
    serverLoadingProgressBar.visibility = GONE

    for((key, serverInfo) in servers) {
      displayServers(key, serverInfo)
    }
    refreshButton = LayoutInflater.from(this).inflate(R.layout.button_server_refresh, serverContainer, false) as FrameLayout
    refreshButton.setOnClickListener {
      jobManager.addJobInBackground(GDMServerJob())
      jobManager.addJobInBackground(EmbyServerJob())
      recreate()
    }
    serverContainer.addView(refreshButton)

    if (serverContainer.childCount > 0) {
      serverContainer.getChildAt(0).requestFocus()
    }
    serverContainer.visibility = VISIBLE
  }

  private fun displayServers(key: String, serverInfo: Server) {
    if (serverInfo.discoveryProtocol() == "Emby") {
      createServerView(R.layout.item_server_select_emby, serverInfo)
    } else {
      createServerView(R.layout.item_server_select_plex, serverInfo)
    }
  }

  private fun createServerView(layoutId: Int, serverInfo: Server) {
    val serverView = LayoutInflater.from(this).inflate(layoutId, serverContainer, false) as FrameLayout?
    val serverNameText = serverView!!.findViewById<TextView>(R.id.server_name)
    serverNameText?.text = serverInfo.serverName
    serverContainer.addView(serverView)
    serverView.setOnClickListener { view -> startNextActivity(serverInfo) }
  }

  private fun startNextActivity(serverInfo: Server) {
    serverClientPreference.set(serverInfo.discoveryProtocol())

    if (serverInfo.discoveryProtocol() == "Emby") {
      val intent = Intent(this, LoginUserActivity::class.java)
      intent.putExtra("server", serverInfo)
      startActivity(intent)
      finish()
      return
    }

    val intent = Intent(this, AndroidTV::class.java)
    startActivity(intent)
    finish()
  }
}