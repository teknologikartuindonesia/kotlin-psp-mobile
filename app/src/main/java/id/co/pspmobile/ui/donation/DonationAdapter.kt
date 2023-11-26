package id.co.pspmobile.ui.donation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.donation.DonationDto
import id.co.pspmobile.databinding.AdapterDonationBinding
import id.co.pspmobile.ui.Utils.formatDateTime

class DonationAdapter : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    private lateinit var list: List<DonationDto>
    private var onItemClickListener : View.OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDonations(list: List<DonationDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: AdapterDonationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(donationDto: DonationDto) {
            with(binding) {
                itemView.tag = donationDto
                itemView.setOnClickListener(onItemClickListener)

                tvDonationName.text = donationDto.title
                tvDate.text = formatDateTime(donationDto.createDate, "dd MMMM yyyy")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationAdapter.ViewHolder {
        val binding = AdapterDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonationAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}