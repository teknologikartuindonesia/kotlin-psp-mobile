package id.co.pspmobile.ui.transaction.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.report.TransactionDetailDto
import id.co.pspmobile.databinding.AdapterTransactionDetailBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime

class TransactionDetailAdapter : RecyclerView.Adapter<TransactionDetailAdapter.ViewHolder>() {

    private lateinit var list: List<TransactionDetailDto>

    @SuppressLint("NotifyDataSetChanged")
    fun setTransactions(list: List<TransactionDetailDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterTransactionDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: TransactionDetailDto) {
            with(binding) {

                tvDate.text = formatDateTime(
                    transaction.createDate.toString(),
                    "dd MMMM yyyy "
                ) + "Pukul" + formatDateTime(transaction.createDate.toString(), " HH:mm")
                tvTransactionName.text = transaction.transactionName
                if (transaction.credit == 0.0) {
                    tvAmount.text = "Rp " + formatCurrency(transaction.debit)
                } else {
                    tvAmount.text = "Rp " + formatCurrency(transaction.credit)
                }
                tvUserName.text = transaction.callerName
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransactionDetailAdapter.ViewHolder {
        val binding = AdapterTransactionDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionDetailAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}