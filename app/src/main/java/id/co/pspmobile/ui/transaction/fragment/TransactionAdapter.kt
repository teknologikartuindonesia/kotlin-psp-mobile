package id.co.pspmobile.ui.transaction.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.report.TransactionDto
import id.co.pspmobile.databinding.AdapterTransactionBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private lateinit var list: List<TransactionDto>

    @SuppressLint("NotifyDataSetChanged")
    fun setTransactions(list: List<TransactionDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: TransactionDto) {
            with(binding) {
                tvTransactionName.text = transaction.transactionName
                tvAmount.text = "Rp " + formatCurrency(transaction.amount)
                tvCount.text = transaction.count.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionAdapter.ViewHolder {
        val binding = AdapterTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}