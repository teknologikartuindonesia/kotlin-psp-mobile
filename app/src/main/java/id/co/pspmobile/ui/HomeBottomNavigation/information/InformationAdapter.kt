package id.co.pspmobile.ui.HomeBottomNavigation.information

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.infonews.Content
import id.co.pspmobile.databinding.AdapterInformationBinding
import id.co.pspmobile.ui.Utils

class InformationAdapter() : RecyclerView.Adapter<InformationAdapter.ViewHolder>() {

    private var list = ArrayList<Content>()
    private var onItemClickListener : View.OnClickListener? = null
    private var baseUrl: String = ""
    private lateinit var context: Context

    @SuppressLint("NotifyDataSetChanged")
    fun setInformations(list: List<Content>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        this.list.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListerner(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    fun setContext(context: Context) {
        this.context = context
    }

    inner class ViewHolder(private val binding: AdapterInformationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: Content) {
            with(binding) {
                tvTitle.text = info.title
                tvDate.text = Utils.formatDateTime(info.createTime, "dd-MM-yyyy")
                tvDescription.text = info.description

                if (!info.image.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(baseUrl + "/main_a/image/get/" + info.image + "/pas")
                        .placeholder(R.drawable.info_news_default)
                        .into(ivInfo)
                }else{
                    if (info.videoUrl != null){
                        val url = getThumbnail(info.videoUrl)
                        Glide.with(context)
                            .load(url)
                            .placeholder(R.drawable.info_news_default)
                            .into(ivInfo)
                    }else{
                        ivInfo.setImageResource(R.drawable.info_news_default)
                    }
                }
                itemView.tag = info
                itemView.setOnClickListener {
                    val intent = Intent(context, InfoDetailActivity::class.java)
                    intent.putExtra("content", Gson().toJson(info))
                    intent.putExtra("baseUrl", baseUrl)
                    context.startActivity(intent)
                }
//                itemView.setOnClickListener(onItemClickListener)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationAdapter.ViewHolder {
        val binding = AdapterInformationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InformationAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}