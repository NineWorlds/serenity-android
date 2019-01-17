package us.nineworlds.serenity.ui.activity.login

import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import toothpick.Toothpick
import us.nineworlds.serenity.R
import us.nineworlds.serenity.common.annotations.InjectionConstants
import us.nineworlds.serenity.common.rest.SerenityClient
import us.nineworlds.serenity.common.rest.SerenityUser
import javax.inject.Inject

class LoginUserViewHolder(view: View) : ViewHolder(view) {

  @BindView(R.id.profile_image)
  lateinit var profileImage: ImageView
  @BindView(R.id.profile_name)
  lateinit var profileName: TextView

  @Inject
  lateinit var serenityClient: SerenityClient

  init {
    ButterKnife.bind(this, view)
    Toothpick.inject(this, Toothpick.openScope(InjectionConstants.APPLICATION_SCOPE));
  }

  fun loadUser(user: SerenityUser) {
    Glide.with(profileImage.context)
      .load(serenityClient.createUserImageUrl(user, 150, 150))
      .placeholder(VectorDrawableCompat.create(profileImage.resources, R.drawable.ic_generic_user, null))
      .centerCrop()
      .into(profileImage)

    profileName.text = user.userName
  }
}