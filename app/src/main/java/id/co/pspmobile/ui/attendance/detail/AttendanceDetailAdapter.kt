package id.co.pspmobile.ui.attendance.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.co.pspmobile.data.network.attendance.AttendanceResDto
import id.co.pspmobile.databinding.AdapterHistoryAttendanceBinding

class AttendanceDetailAdapter : RecyclerView.Adapter<AttendanceDetailAdapter.ViewHolder>() {

    private lateinit var list: List<AttendanceResDto>
    private var baseUrl: String = ""

    @SuppressLint("NotifyDataSetChanged")
    fun setAttendance(list: List<AttendanceResDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    inner class ViewHolder(private val binding: AdapterHistoryAttendanceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: AttendanceResDto) {
            with(binding) {
                if (attendance.photo_url_masuk.isNotEmpty()) {
                    Picasso.get().load(attendance.photo_url_masuk).noFade().fit()
                        .into(ivPhotoIn);
                }
                if (attendance.photo_url_pulang.isNotEmpty()) {
                    Picasso.get().load(attendance.photo_url_pulang).noFade().fit()
                        .into(ivPhotoOut);
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceDetailAdapter.ViewHolder {
        val binding = AdapterHistoryAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceDetailAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}