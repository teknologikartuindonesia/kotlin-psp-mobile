package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.AdapterSummaryInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class SummaryAdapter : RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

    private lateinit var list: List<InvoiceDto>

    @SuppressLint("NotifyDataSetChanged")
    fun setInvoices(list: List<InvoiceDto>) {
        this.list = list

        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterSummaryInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: InvoiceDto) {
            with(binding) {
                tvName.text = invoice.title
                tvDate.text = invoice.invoiceDate
                tvAmount.text = formatCurrency(invoice.amount)
                tvPaid.text = formatCurrency(invoice.paidAmount)
                tvDue.text = formatCurrency(invoice.amount - invoice.paidAmount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryAdapter.ViewHolder {
        val binding = AdapterSummaryInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SummaryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}