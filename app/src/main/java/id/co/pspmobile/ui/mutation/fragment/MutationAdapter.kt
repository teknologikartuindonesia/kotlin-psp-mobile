package id.co.pspmobile.ui.mutation.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.report.MutationDto
import id.co.pspmobile.databinding.AdapterMutationBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class MutationAdapter : RecyclerView.Adapter<MutationAdapter.ViewHolder>() {

    private lateinit var list: List<MutationDto>

    @SuppressLint("NotifyDataSetChanged")
    fun setMutations(list: List<MutationDto>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterMutationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mutation: MutationDto) {
            with(binding) {
                tvDateTime.text = mutation.dateTime
                if (mutation.debit == 0.0) {
                    tvDebitCredit.text = "Rp " + formatCurrency(mutation.credit)
                } else {
                    tvDebitCredit.text = "-Rp " + formatCurrency(mutation.debit)
                }
                tvTransactionName.text = mutation.transactionName
                tvBalance.text = "Rp " + formatCurrency(mutation.balance)
                tvNote.text = mutation.note
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MutationAdapter.ViewHolder {
        val binding = AdapterMutationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MutationAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}