package id.co.pspmobile.ui.topup.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.transaction.TransactionResDto
import id.co.pspmobile.databinding.AdapterHistoryTopupBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class HistoryTopUpAdapter : PagingDataAdapter<TransactionResDto, HistoryTopUpAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionResDto>() {
            override fun areItemsTheSame(oldItem: TransactionResDto, newItem: TransactionResDto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: TransactionResDto, newItem: TransactionResDto): Boolean =
                oldItem == newItem
        }
    }

    inner class ViewHolder(private val binding: AdapterHistoryTopupBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transaction: TransactionResDto) {
            with(binding) {
                tvDate.text = transaction.dateTime
                tvTransactionName.text = transaction.name
                tvBalance.text = "Rp. " + formatCurrency(transaction.amount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterHistoryTopupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

}