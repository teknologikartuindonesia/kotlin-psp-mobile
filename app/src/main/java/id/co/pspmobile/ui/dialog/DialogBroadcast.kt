package id.co.pspmobile.ui.dialog

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.responses.infonews.ContentX
import id.co.pspmobile.databinding.DialogBroadcastBinding
import id.co.pspmobile.ui.HomeBottomNavigation.message.CarouselRvAdapter
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class DialogBroadcast(
    val broadcast: ContentX,
    val supportFragmentManager: FragmentManager
): DialogFragment() {

    private lateinit var binding: DialogBroadcastBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBroadcastBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = broadcast.title
        binding.tvSubtitle.text = broadcast.subtitle
        binding.tvMessage.text = Html.fromHtml(broadcast.message)
        if (broadcast.message.contains("https://")){
            binding.tvMessage.setOnClickListener {
                val dialog = DialogYesNo(
                    "Konfirmasi",
                    "Apakah anda ingin membuka link ini? ${getUrlFromString(broadcast.message)}",
                    "Ya",
                    "Tidak",
                    {
                        val url = getUrlFromString(broadcast.message)
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(browserIntent)
                    },
                    {}
                )
                dialog.show(supportFragmentManager, "DialogYesNo")
            }
        }

        val viewPager = binding.viewPagerBroadcast
        if (broadcast.imagesFirebase.isEmpty()){
            viewPager.visible(false)
        }
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

        if(broadcast.imagesFirebase?.isNotEmpty() == true){
            viewPager.adapter = CarouselRvAdapter(broadcast.imagesFirebase)
        } else {
            viewPager.visible(false)
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    fun getUrlFromString(text: String): String {
        // <a href=\"https://teknologikartu.com\">https://teknologikartu.com</a></p>
        val regex = Regex("""<a\s+href=["']([^"']+)["'].*?>""")
        val matchResult = regex.find(text)
        return matchResult?.groups?.get(1)?.value ?: ""
    }
}