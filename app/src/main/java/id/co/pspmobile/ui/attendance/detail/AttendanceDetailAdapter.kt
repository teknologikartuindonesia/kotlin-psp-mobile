package id.co.pspmobile.ui.attendance.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.AdapterAttendanceBinding

class AttendanceDetailAdapter : RecyclerView.Adapter<AttendanceDetailAdapter.ViewHolder>() {

    private lateinit var list: List<CallerIdentity>
    private var baseUrl: String = ""
    private var onItemClickListener : View.OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setAccounts(list: List<CallerIdentity>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    fun setOnItemClickListerner(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: AdapterAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(callerIdentity: CallerIdentity) {
            itemView.tag = callerIdentity
            itemView.setOnClickListener(onItemClickListener)

            with(binding) {
                if (callerIdentity.photoUrl.isNotEmpty()) {
                    Picasso.get().load(baseUrl + "/main_a/image/get/" + callerIdentity.photoUrl + "/pas").noFade().fit()
                        .into(ivPhoto);
                }
                tvAccountName.text = callerIdentity.name
                tvNis.text = callerIdentity.callerId
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceDetailAdapter.ViewHolder {
        val binding = AdapterAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceDetailAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}