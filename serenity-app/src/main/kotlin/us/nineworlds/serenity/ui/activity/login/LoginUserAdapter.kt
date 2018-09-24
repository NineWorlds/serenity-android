package us.nineworlds.serenity.ui.activity.login

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.rest.SerenityUser

class LoginUserAdapter: RecyclerView.Adapter<LoginUserViewHolder>() {

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
}