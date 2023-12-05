package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.data.network.responses.digitalCard.CardDataItem
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCard
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCardItem
import id.co.pspmobile.databinding.AdapterSummaryInvoiceBinding
import id.co.pspmobile.databinding.ItemDigitalCardBinding
import id.co.pspmobile.databinding.ItemHistorySyncDigitalCardBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime
import id.co.pspmobile.ui.digitalCard.DigitalCardViewModel

class HistorySyncDigitalCardAdapter(viewModel: DigitalCardViewModel) :
    RecyclerView.Adapter<HistorySyncDigitalCardAdapter.ViewHolder>() {

    private lateinit var list: List<CardDataItem>
    var vm = viewModel

    @SuppressLint("NotifyDataSetChanged")
    fun setHistorySyncDigitalCard(list: List<CardDataItem>) {
        Log.d("HistorySyncDigitalCardAdapter", "bind: $list")
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemHistorySyncDigitalCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CardDataItem) {
            with(binding) {
                nfcId.text = "NFC ID: "+item.nfcId
                createDate.text = item.history.joinToString("\n")
                callerName.text = vm.getUserData().user.name
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