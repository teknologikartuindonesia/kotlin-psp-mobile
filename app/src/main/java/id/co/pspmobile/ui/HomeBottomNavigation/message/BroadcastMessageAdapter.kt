package id.co.pspmobile.ui.HomeBottomNavigation.message

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.activebroadcast.ContentX
import id.co.pspmobile.databinding.ItemBroadcastMessageBinding
import id.co.pspmobile.ui.Utils.visible

class BroadcastMessageAdapter(private val context: Context): RecyclerView.Adapter<BroadcastMessageAdapter.ViewHolder>() {
    private var list = ArrayList<ContentX>()
    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    fun setBroadcastMessage(item: ArrayList<ContentX>) {
        list.addAll(item)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BroadcastMessageAdapter.ViewHolder {
        val binding = ItemBroadcastMessageBinding.inflate(
            parent.context.getSystemService(android.view.LayoutInflater::class.java),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BroadcastMessageAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(private val binding: ItemBroadcastMessageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(broadcastMessage: ContentX) {
            with(binding) {
                txtBroadcastTitle.text = broadcastMessage.title
                val sent = broadcastMessage.broadcastSent.split("T")[0]
                txtBroadcastDate.text = sent.split("-").reversed().joinToString("/")
                if (broadcastMessage.imagesFirebase.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(broadcastMessage.imagesFirebase[0])
                        .placeholder(R.drawable.info_news_default)
                        .into(imgBroadcast)
                } else {
                    imgBroadcast.visible(false)
                }
                if (broadcastMessage.message.isNotEmpty()) {
                    txtBroadcastDescription.text =  Html.fromHtml(Html.fromHtml(broadcastMessage.message).toString())
                } else {
                    txtBroadcastDescription.visible(false)
                }
                llBroadcastMessage.setOnClickListener {
                    val i = Intent(context, BroadcastDetailActivity::class.java)
                    i.putExtra("content", Gson().toJson(broadcastMessage))
                    context.startActivity(i)
                }
            }
        }

    }
}
