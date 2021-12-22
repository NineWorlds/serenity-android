package us.nineworlds.serenity.ui.activity.leanback.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import us.nineworlds.serenity.databinding.ActivityLeanbackDetailsBinding

class DetailsActivity: AppCompatActivity() {

    private lateinit var binding : ActivityLeanbackDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLeanbackDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailsIntent = intent
        val itemId = detailsIntent.getStringExtra("itemId")
        val type = detailsIntent.getStringExtra("videoType")

        if (type == "tvshows") {
            val fragment = binding.fragmentLeanbackContainer.getFragment<DetailsFragment>()
            fragment.setup(itemId!!, type)
        }
    }

}