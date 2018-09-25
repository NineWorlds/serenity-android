package us.nineworlds.serenity.ui.activity.login

import android.view.View
import android.widget.Toast
import net.ganin.darv.DpadAwareRecyclerView
import net.ganin.darv.DpadAwareRecyclerView.OnItemClickListener

class LoginUserOnItemClickListner(val presenter: LoginUserPresenter) : OnItemClickListener {

  override fun onItemClick(recyclerView: DpadAwareRecyclerView?, view: View?, position: Int, p3: Long) {
    val adapter: LoginUserAdapter = recyclerView?.adapter as LoginUserAdapter
    val user = adapter.getItemAt(position)

    presenter.loadUser(user!!)
  }
}