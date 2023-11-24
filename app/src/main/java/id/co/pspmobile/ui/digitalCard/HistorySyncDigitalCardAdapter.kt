package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCard
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCardItem
import id.co.pspmobile.databinding.AdapterSummaryInvoiceBinding
import id.co.pspmobile.databinding.ItemDigitalCardBinding
import id.co.pspmobile.databinding.ItemHistorySyncDigitalCardBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime

class HistorySyncDigitalCardAdapter :
    RecyclerView.Adapter<HistorySyncDigitalCardAdapter.ViewHolder>() {

    private lateinit var list: List<SyncDigitalCardItem>

    @SuppressLint("NotifyDataSetChanged")
    fun setHistorySyncDigitalCard(list: List<SyncDigitalCardItem>) {
        this.list = list

        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemHistorySyncDigitalCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SyncDigitalCardItem) {
            with(binding) {
                nfcId.text = item.nfcId
                createDate.text = formatDateTime(item.createDate.toString(), "DD MMMM YYYY HH:MM")
                callerName.text = item.callerName
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistorySyncDigitalCardAdapter.ViewHolder {
        val binding = ItemHistorySyncDigitalCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistorySyncDigitalCardAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}