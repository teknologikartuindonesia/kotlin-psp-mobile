package com.tki.internal.ui.education

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.responses.InvoiceResponse
import id.co.pspmobile.databinding.InvoiceItemBinding

class InvoiceAdapter : RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {

    private lateinit var list : List<InvoiceResponse>
    private lateinit var baseURL: String

    fun setInvoiceList(list: List<InvoiceResponse>, baseURL: String) {
        this.list = list
        this.baseURL = baseURL
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding:InvoiceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(education: InvoiceResponse) {
            with(binding) {
//                tvSchoolName.text = education.schoolName
//                tvSchoolGrade.text = education.schoolGrade
//                tvGraduationYear.text = education.graduationYear.toString()
//                if (education.note?.isEmpty() == true) {
//                    tvNote.text = "-"
//                } else {
//                    tvNote.text = education.note
//                }
//
//                if (education.certificateUrl?.isEmpty() == true) {
//                    imgCertificate.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.ic_no_image) })
//                } else {
//                    val activity =  itemView.context as? EducationActivity
//                    activity?.let {
//                        Glide.with(it)
//                            .load(baseURL + "/internal/image/thumbnail" + education.certificateUrl)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .skipMemoryCache(true)
//                            .into(imgCertificate)
//                    }
//
//                    imgCertificate.setOnClickListener() {
//                        activity?.let { it1 -> DialogFile(
//                            baseURL + "/internal/image" + education.certificateUrl,
//                            "PT Teknologi Kartu Indonesia",
//                            "Download ijazah " + education.schoolGrade
//                        ).show(it1.supportFragmentManager, "CertificateFile") }
//                    }
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceAdapter.ViewHolder {
        val binding = InvoiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}