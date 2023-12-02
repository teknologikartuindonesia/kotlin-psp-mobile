package id.co.pspmobile.ui.attendance.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import id.co.pspmobile.R
import id.co.pspmobile.data.network.attendance.AttendanceResDto
import id.co.pspmobile.databinding.AdapterHistoryAttendanceBinding
import id.co.pspmobile.ui.Utils.formatDateTime

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
                    Glide.with(itemView.context)
                        .load(attendance.photo_url_masuk)
                        .placeholder(R.drawable.ic_account)
                        .into(ivPhotoIn)
                }
                if (attendance.photo_url_pulang.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(attendance.photo_url_pulang)
                        .placeholder(R.drawable.ic_account)
                        .into(ivPhotoOut)
                }
                tvShiftName.text = attendance.shift_name
                tvDate.text = formatDateTime(attendance.tanggal_absen, "dd-MMM-yyyy")
                tvInTime.text = attendance.jam_absen_masuk
                tvOutTime.text = attendance.jam_absen_pulang

                when (attendance.sesi_ket_masuk) {
                    "DATANG TEPAT" -> {
                        tvInStatus.text = itemView.context.resources.getString(R.string.on_time)
                        tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_green_bg) }
                    }
                    "DATANG TERLAMBAT" -> {
                        tvInStatus.text = itemView.context.resources.getString(R.string.late)
                        tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                    }
                    else -> {
                        if (attendance.sesi_ket_pulang.isEmpty()) {
                            tvInStatus.text = " - "
                            tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                        } else {
                            if(attendance.sesi_ket_masuk == "NONE") {
                                tvInStatus.text = " - "
                                tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                            } else {
                                tvInStatus.text = attendance.sesi_ket_masuk.lowercase().replaceFirstChar(Char::uppercaseChar)
                                tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_red_bg) }
                            }
                        }
                    }
                }

                when (attendance.sesi_ket_pulang) {
                    "PULANG TEPAT" -> {
                        tvOutStatus.text = itemView.context.resources.getString(R.string.on_time)
                        tvOutStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_green_bg) }
                    }
                    "PULANG AWAL" -> {
                        tvOutStatus.text = itemView.context.resources.getString(R.string.early)
                        tvOutStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                    }
                    else -> {
                        if (attendance.sesi_ket_pulang.isEmpty()) {
                            tvOutStatus.text = " - "
                            tvOutStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                        } else {
                            if(attendance.sesi_ket_masuk == "NONE") {
                                tvOutStatus.text = " - "
                                tvOutStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                            } else {
                                tvOutStatus.text = attendance.sesi_ket_pulang.lowercase().replaceFirstChar(Char::uppercaseChar)
                                tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_red_bg) }
                            }
                        }
                    }
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