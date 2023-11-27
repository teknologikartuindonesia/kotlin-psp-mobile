package id.co.pspmobile.ui.HomeBottomNavigation.information

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.co.pspmobile.data.network.information.InformationDto
import id.co.pspmobile.databinding.AdapterInformationBinding
import id.co.pspmobile.ui.Utils

class InformationAdapter() : RecyclerView.Adapter<InformationAdapter.ViewHolder>() {

    private lateinit var list: List<InformationDto>
    private var onItemClickListener : View.OnClickListener? = null
    private var baseUrl: String = ""

    @SuppressLint("NotifyDataSetChanged")
    fun setInformations(list: List<InformationDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListerner(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    inner class ViewHolder(private val binding: AdapterInformationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(info: InformationDto) {
            with(binding) {
                tvTitle.text = info.title
                tvDate.text = Utils.formatDateTime(info.createTime, "dd-MM-yyyy")
                tvDescription.text = info.description

                if (info.image.isNotEmpty()) {
                    Picasso.get().load(baseUrl + "/main_a/image/get/" + info.image + "/pas").noFade().fit()
                        .into(ivInfo);
                }
                itemView.tag = info
                itemView.setOnClickListener(onItemClickListener)
            }
        }
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