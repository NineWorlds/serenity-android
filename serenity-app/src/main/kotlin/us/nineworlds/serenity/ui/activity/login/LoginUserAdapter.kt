package us.nineworlds.serenity.ui.activity.login

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.com.tvrecyclerview.TvRecyclerView.OnItemStateListener
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.rest.SerenityUser

class LoginUserAdapter(val presenter: LoginUserPresenter) : androidx.recyclerview.widget.RecyclerView.Adapter<LoginUserViewHolder>(),
  OnItemStateListener {

  val users: ArrayList<SerenityUser> = ArrayList<SerenityUser>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoginUserViewHolder {
    val loginUserView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_profile, parent, false)
    return LoginUserViewHolder(loginUserView)
  }

  override fun getItemCount(): Int = users.size

  fun getItemAt(postion: Int): SerenityUser? {
    if (users.isEmpty() || postion < 0) {
      return null
    }
    return users[postion]
  }

  override fun onBindViewHolder(holder: LoginUserViewHolder, position: Int) {
    holder.loadUser(users[position])
  }

  fun loadUsers(newUsers: List<SerenityUser>) {
    users.clear()
    users.addAll(newUsers)

    notifyDataSetChanged()
  }

  override fun onItemViewClick(p0: View?, p1: Int) {
    val user = users[p1]

    presenter.loadUser(user)
  }

  override fun onItemViewFocusChanged(p0: Boolean, p1: View?, p2: Int) {
  }
}