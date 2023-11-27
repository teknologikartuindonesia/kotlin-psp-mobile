package id.co.pspmobile.ui.HomeBottomNavigation.message

import android.content.res.Resources
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.activebroadcast.ContentX
import id.co.pspmobile.databinding.ActivityBroadcastDetailBinding
import id.co.pspmobile.ui.Utils.visible

class BroadcastDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBroadcastDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBroadcastDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent?.extras
        val c = intent?.getString("content")
        val content = c?.let { Gson().fromJson(it, ContentX::class.java) }

        val viewPager = findViewById<ViewPager2>(R.id.viewPagerBroadcast)

        viewPager.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }

        viewPager.setPageTransformer(compositePageTransformer)
        viewPager.setPageTransformer(compositePageTransformer)

        if(content?.imagesFirebase?.isNotEmpty() == true){
            viewPager.adapter = CarouselRvAdapter(content.imagesFirebase)
        } else {
            viewPager.visible(false)
        }

        binding.txtBroadcastMessageTitle.text = content?.title
        binding.txtBroadcastMessageSubtitle.text = content?.subtitle
        binding.txtBroadcastMessageDescription.text = Html.fromHtml(content?.message)
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

}