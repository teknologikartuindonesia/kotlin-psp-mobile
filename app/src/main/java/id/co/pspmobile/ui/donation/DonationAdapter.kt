package id.co.pspmobile.ui.donation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.donation.DonationDto
import id.co.pspmobile.databinding.AdapterDonationBinding
import id.co.pspmobile.ui.Utils.formatDateTime
import id.co.pspmobile.ui.donation.detail.DonationDetailActivity
import java.io.Serializable

class DonationAdapter(private val context: Context) : RecyclerView.Adapter<DonationAdapter.ViewHolder>() {

    private var list = ArrayList<DonationDto>()
    private var onItemClickListener : View.OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setDonations(list: ArrayList<DonationDto>) {
        this.list.addAll(list)
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

                basePanel.setOnClickListener {
                    val intent = Intent(context, DonationDetailActivity::class.java)
                    intent.putExtra("donationDto", donationDto as Serializable)
                    context.startActivity(intent)
                }
                tvDonationDate.text = donationDto.title
                tvNominal.text = formatDateTime(donationDto.createDate, "dd MMMM yyyy")
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