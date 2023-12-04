package id.co.pspmobile.ui.HomeBottomNavigation.information

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.infonews.Content
import id.co.pspmobile.databinding.ActivityInfoDetailBinding
import id.co.pspmobile.ui.HomeBottomNavigation.message.CarouselRvAdapter
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.dialog.DialogYesNo

@AndroidEntryPoint
class InfoDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent?.extras
        val c = intent?.getString("content")
        val baseUrl = intent?.getString("baseUrl")
        val content = c?.let { Gson().fromJson(it, Content::class.java) }

        val date = content?.updateTime?.split("T")?.get(0) ?: ""
        val time = content?.updateTime?.split("T")?.get(1) ?: ""
        binding.txtInfoMessageDate.text = "$date $time"
        binding.txtInfoMessageTitle.text = content?.title
        binding.txtInfoMessageSubtitle.text = content?.subtitle
        binding.txtInfoMessageDescription.text = content?.description

        val viewPager = binding.viewPagerInfo
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

        if (content == null){
            return
        }
        val imagesList = mutableListOf<String>()
        if (content.image.isNotEmpty()){
            imagesList.add(baseUrl + "/main_a/image/get/" + content.image + "/pas")
        }

        for (i in content.imageOpt){
            imagesList.add("$baseUrl/main_a/image/get/$i/pas")
        }

        if (content.videoUrl.isNotEmpty()){
            imagesList.add(getThumbnail(content.videoUrl))
        }

        if(imagesList.isNotEmpty()){
            val x = imagesList as ArrayList<String>
            viewPager.adapter = CarouselRvAdapter(x as List<String>)
        } else {
            viewPager.visible(false)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        if (getUrlFromString(content.description).isNotEmpty()){
            binding.txtUrl.visible(true)
        } else {
            binding.txtUrl.visible(false)
        }
        binding.txtUrl.setOnClickListener {
            val dialogYesNo = DialogYesNo(
                resources.getString(R.string.title_information),
                "${resources.getString(R.string.buka_url_terkait)}\n(${getUrlFromString(content.description)})",
                resources.getString(R.string.open),
                resources.getString(R.string.close),
                yesListener = {
                    val url = getUrlFromString(content.description)
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                },
                noListener = {

                }
            )
            dialogYesNo.show(supportFragmentManager, "dialogYesNo")
        }

        if (content.videoUrl != ""){
            binding.txtVideoUrl.visible(true)
        } else {
            binding.txtVideoUrl.visible(false)
        }

        binding.txtVideoUrl.setOnClickListener {
            val dialogYesNo = DialogYesNo(
                resources.getString(R.string.title_information),
                "${resources.getString(R.string.buka_video_terkait)}\n(${content.videoUrl})",
                resources.getString(R.string.open),
                resources.getString(R.string.close),
                yesListener = {
                    val url = content.videoUrl
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                },
                noListener = {

                }
            )
            dialogYesNo.show(supportFragmentManager, "dialogYesNo")
        }
    }


    fun getUrlFromString(text: String): String {
        // "it is example of string that contain url https://teknologikartu.com, you should get the url from this string"
        val regex = """(https?://\S+)""".toRegex(RegexOption.MULTILINE)
        val matchResult = regex.find(text)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    fun getYoutubeCode(str: String): String {
        val regex = """(\/|%3D|v=)([0-9A-z-_]{11})([%#?&]|$)""".toRegex(RegexOption.MULTILINE)
        val matches = regex.findAll(str).map { it.value }.distinct().joinToString("")
        return matches.replace("/", "").replace("v=", "").replace("?", "").replace("#", "")
    }

    fun getThumbnail(str: String): String {
        var url = str
        url = if (url.contains("embed")) {
            val arr = url.split("/")
            "https://img.youtube.com/vi/${arr.last()}/sddefault.jpg"
        } else if (url.contains("watch")) {
            val arr = url.split("=")
            "https://img.youtube.com/vi/${arr.last()}/sddefault.jpg"
        } else {
            "https://img.youtube.com/vi/${getYoutubeCode(str)}/sddefault.jpg"
        }

        return url
    }


}