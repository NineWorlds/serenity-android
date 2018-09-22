package us.nineworlds.serenity.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import us.nineworlds.serenity.AndroidTV
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.Server
import us.nineworlds.serenity.injection.ForMediaServers
import us.nineworlds.serenity.injection.InjectingActivity
import javax.inject.Inject

class ServerSelectionActivity : InjectingActivity() {

  @field:[Inject ForMediaServers]
  lateinit var servers: MutableMap<String, Server>

  @BindView(R.id.server_container)
  lateinit var serverContainer: LinearLayout

  @BindView(R.id.server_loading_progress)
  lateinit var serverLoadingProgressBar: ProgressBar

  lateinit var serverDisplayHandler: Handler

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_server_selection)
    ButterKnife.bind(this)

    serverDisplayHandler = Handler()
    serverDisplayHandler.postDelayed({
      serverLoadingProgressBar.visibility = GONE
      servers.forEach { key, serverInfo -> displayServers(key, serverInfo) }
      if (serverContainer.childCount > 0) {
        serverContainer.getChildAt(0).requestFocus()
      }
      serverContainer.visibility = VISIBLE
    }, 4000L)
  }

  fun displayServers(key: String, serverInfo: Server) {
    if (serverInfo.discoveryProtocol() == "Emby") {
      createServerView(R.layout.item_server_select_emby, serverInfo)
    } else {
      createServerView(R.layout.item_server_select_plex, serverInfo)
    }
  }

  fun createServerView(layoutId: Int, serverInfo: Server) {
    val serverView = LayoutInflater.from(this).inflate(layoutId, serverContainer, false) as FrameLayout?
    val serverNameText = serverView!!.findViewById<TextView>(R.id.server_name)
    serverNameText?.text = serverInfo.serverName
    serverContainer.addView(serverView)
    serverView.setOnClickListener { view -> startNextActivity(serverInfo) }
  }

  fun startNextActivity(serverInfo: Server) {
    val intent = Intent(this, AndroidTV::class.java)
    startActivity(intent)
    finish()
  }
}