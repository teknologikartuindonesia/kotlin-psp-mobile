package id.co.pspmobile.ui.HomeBottomNavigation.message

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import id.co.pspmobile.data.network.responses.activebroadcast.Content
import id.co.pspmobile.databinding.ItemNotificationMessageBinding

class NotificationMessageAdapter(private val context: Context): RecyclerView.Adapter<NotificationMessageAdapter.ViewHolder>() {

    private var list = ArrayList<Content>()

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
    }

    fun setNotificationMessage(item: ArrayList<Content>) {
        list.addAll(item)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationMessageAdapter.ViewHolder {
        val binding = ItemNotificationMessageBinding.inflate(
            parent.context.getSystemService(android.view.LayoutInflater::class.java),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationMessageAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemNotificationMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notificationMessage: Content) {
            with(binding) {
                txtNotifTitle.text = notificationMessage.title
                txtNotifDesc.text = Html.fromHtml(notificationMessage.message)
                basePanel.setOnClickListener {
                    val i = Intent(context, NotificationMessageDetailActivity::class.java)
                    i.putExtra("content", Gson().toJson(notificationMessage))
                    context.startActivity(i)
                }
            }
        }
    }
}