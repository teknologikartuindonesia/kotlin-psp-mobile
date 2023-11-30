package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.infonews.Content
import id.co.pspmobile.databinding.ItemHomeInfoNewsBinding
import id.co.pspmobile.ui.HomeBottomNavigation.information.InfoDetailActivity
import id.co.pspmobile.ui.Utils.formatDateTime

class InfoNewsAdapter : RecyclerView.Adapter<InfoNewsAdapter.ViewHolder>() {
    private lateinit var infoArray: ArrayList<Content>
    private lateinit var baseURL: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoNewsAdapter.ViewHolder {
        val binding =
            ItemHomeInfoNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(infoArray[position])
    }

    override fun getItemCount(): Int {
        return infoArray.size
    }

    fun setInfoList(infoList: ArrayList<Content>, baseURL: String) {
        this.infoArray = infoList
        this.baseURL = baseURL
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemHomeInfoNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(infoModel: Content) {
            with(binding) {
                Log.d(
                    "InfoNewsAdapter",
                    "${baseURL}/main_a/main_a/image/get/${infoModel.image}/pas"
                )
                if (!infoModel.image.isNullOrEmpty()) {
                    Glide.with(itemView.context)
                        .load(baseURL + "/main_a/image/get/" + infoModel.image + "/pas")
                        .placeholder(R.drawable.info_news_default)
                        .into(imgHomeInfoNews)
                }else{
                    if (infoModel.videoUrl != null){
                        val url = getThumbnail(infoModel.videoUrl)
                        Glide.with(itemView.context)
                            .load(url)
                            .placeholder(R.drawable.info_news_default)
                            .into(imgHomeInfoNews)
                    }else{
                        imgHomeInfoNews.setImageResource(R.drawable.info_news_default)
                    }
                }
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, InfoDetailActivity::class.java)
                    intent.putExtra("content", Gson().toJson(infoModel))
                    intent.putExtra("baseUrl", baseURL)
                    itemView.context.startActivity(intent)
                }
//                val imgUrl = "${baseURL}/main_a/main_a/image/get/${infoModel.image}/pas"
//                val imageLoader = ImageLoader.Builder(itemView.context)
//                    .components {
//                        add(SvgDecoder.Factory())
//                    }
//                    .build()
//                val imageRequest = ImageRequest.Builder(itemView.context)
//                    .data(imgUrl)
//                    .target(imgHomeInfoNews)
//                    .transformations(RoundedCornersTransformation())
//                    .build()
//                val disposable = imageLoader.enqueue(imageRequest)
                var title = ""
                if (infoModel.title.length > 96) {
                    title = infoModel.title + "...."
                } else {
                    title = infoModel.title
                }
                txtHomeInfoNewsTitle.text = title
                txtHomeInfoNewsDate.text = formatDateTime(infoModel.createTime, "dd-MM-yyyy")
            }
        }
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