package id.co.pspmobile.ui.attendance.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
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
                    Picasso.get().load(attendance.photo_url_masuk).noFade().fit().into(ivPhotoIn);
                }
                if (attendance.photo_url_pulang.isNotEmpty()) {
                    Picasso.get().load(attendance.photo_url_pulang).noFade().fit().into(ivPhotoOut);
                }
                tvShiftName.text = attendance.shift_name
                tvDate.text = formatDateTime(attendance.tanggal_absen, "dd-MMM-yyyy")
                tvInTime.text = attendance.jam_absen_masuk
                tvOutTime.text = attendance.jam_absen_pulang

                if (attendance.sesi_ket_masuk == "DATANG TEPAT") {
                    tvInStatus.text = "On Time"
                    tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_green_bg) }
                } else if (attendance.sesi_ket_masuk == "DATANG TERLAMBAT") {
                    tvInStatus.text = "Terlambat"
                    tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                } else {
                    if (attendance.sesi_ket_pulang.isEmpty()) {
                        tvInStatus.text = " - "
                    } else {
                        tvInStatus.text = attendance.sesi_ket_masuk.lowercase().replaceFirstChar(Char::uppercaseChar)
                    }
                    tvInStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_red_bg) }
                }

                if (attendance.sesi_ket_pulang == "PULANG TEPAT") {
                    tvOutStatus.text = "On Time"
                    tvOutStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_green_bg) }
                } else if (attendance.sesi_ket_pulang == "PULANG AWAL") {
                    tvOutStatus.text = "Pulang Awal"
                    tvOutStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_yellow_bg) }
                } else {
                    if (attendance.sesi_ket_pulang.isEmpty()) {
                        tvOutStatus.text = " - "
                    } else {
                        tvOutStatus.text = attendance.sesi_ket_pulang.lowercase().replaceFirstChar(Char::uppercaseChar)
                    }
                    tvOutStatus.background = itemView.context?.let {ActivityCompat.getDrawable(it, R.drawable.attendance_red_bg) }
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