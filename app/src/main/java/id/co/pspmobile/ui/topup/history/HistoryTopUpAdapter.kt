package id.co.pspmobile.ui.topup.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.data.network.transaction.TransactionResDto
import id.co.pspmobile.databinding.AdapterHistoryTopupBinding
import id.co.pspmobile.databinding.AdapterSummaryInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime
import id.co.pspmobile.ui.invoice.fragment.SummaryAdapter

class HistoryTopUpAdapter : RecyclerView.Adapter<HistoryTopUpAdapter.ViewHolder>() {

    private lateinit var list: List<TransactionResDto>

    @SuppressLint("NotifyDataSetChanged")
    fun setHistoryTopUp(list: List<TransactionResDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterHistoryTopupBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transaction: TransactionResDto) {
            with(binding) {
                tvDate.text = formatDateTime(transaction.dateTime.toString(),"dd MMMM yyyy")
                tvTransactionName.text = transaction.name
                tvBalance.text = "Rp. " + formatCurrency(transaction.amount)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryTopUpAdapter.ViewHolder {
        val binding = AdapterHistoryTopupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: HistoryTopUpAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}