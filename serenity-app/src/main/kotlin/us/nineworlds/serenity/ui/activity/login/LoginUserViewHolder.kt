package us.nineworlds.serenity.ui.activity.login

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import toothpick.Toothpick
import us.nineworlds.serenity.GlideApp
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import javax.inject.Inject

class LoginUserViewHolder(view: View) : ViewHolder(view) {

  private val profileImage: ImageView = view.findViewById(R.id.profile_image)
  val profileName: TextView = view.findViewById(R.id.profile_name)

  @Inject
  lateinit var serenityClient: SerenityClient

  init {
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE))
  }

  fun loadUser(user: SerenityUser) {

    val placeHolder = ContextCompat.getDrawable(profileImage.context, R.drawable.ic_generic_user)
    DrawableCompat.setTint(placeHolder!!, ContextCompat.getColor(profileImage.context, R.color.white))

    GlideApp.with(profileImage.context)
      .asDrawable()
      .load(serenityClient.createUserImageUrl(user, 150, 150))
      .placeholder(placeHolder)
      .centerCrop()
      .into(profileImage)

    profileName.text = user.userName
  }

  fun getItemView(): View = itemView
}