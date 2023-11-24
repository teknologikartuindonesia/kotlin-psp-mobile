package id.co.pspmobile.ui.donation.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.donation.Participant
import id.co.pspmobile.databinding.AdapterDonationBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class DonationHistoryAdapter : RecyclerView.Adapter<DonationHistoryAdapter.ViewHolder>() {

    private lateinit var list: List<Participant>

    @SuppressLint("NotifyDataSetChanged")
    fun setParticipants(list: List<Participant>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterDonationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(participant: Participant) {
            with(binding) {
                tvDonationName.text = participant.callerName
                tvDate.text = formatCurrency(participant.amount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationHistoryAdapter.ViewHolder {
        val binding = AdapterDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DonationHistoryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}