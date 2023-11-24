package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.infonews.Content
import id.co.pspmobile.databinding.ItemHomeInfoNewsBinding

class InfoNewsAdapter: RecyclerView.Adapter<InfoNewsAdapter.ViewHolder>() {
    private lateinit var infoArray: ArrayList<Content>
    private lateinit var baseURL: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoNewsAdapter.ViewHolder {
        val binding = ItemHomeInfoNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    inner class ViewHolder(private val binding: ItemHomeInfoNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(infoModel: Content) {
            with(binding) {
                Log.d("InfoNewsAdapter", "${baseURL}/main_a/main_a/image/get/${infoModel.image}/pas")
                Glide.with(itemView.context)
                    .load("${baseURL}/main_a/main_a/image/get/${infoModel.image}/pas")
                    .placeholder(R.drawable.info_news_default)
                    .into(imgHomeInfoNews)
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
                txtHomeInfoNewsTitle.text = infoModel.title
            }
        }
    }
}