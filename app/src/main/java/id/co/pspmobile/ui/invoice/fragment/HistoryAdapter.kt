package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.AdapterHistoryInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private lateinit var list: List<InvoiceDto>
    private lateinit var onDetailClickListener : (invoice: InvoiceDto) -> (Unit)

    @SuppressLint("NotifyDataSetChanged")
    fun setInvoices(list: List<InvoiceDto>) {
        this.list = list

        notifyDataSetChanged()
    }

    fun setOnDetailClickListener(onDetailClickListener: (invoice: InvoiceDto) -> (Unit)) {
        this.onDetailClickListener = onDetailClickListener
    }

    inner class ViewHolder(private val binding: AdapterHistoryInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: InvoiceDto) {
            with(binding) {
                tvInvoiceName.text = invoice.title
                tvName.text = invoice.callerName
                tvNis.text = invoice.callerId
                tvDate.text = invoice.invoiceDate
                tvDueDate.text = invoice.dueDate
                if (invoice.partialMethod) {
                    tvType.text = "CREDIT"
                } else {
                    tvType.text = "CASH"
                }
                tvPaidAmount.text = formatCurrency(invoice.paidAmount)
                tvUnpaidAmount.text = formatCurrency(invoice.amount - invoice.paidAmount)

                btnDetail.setOnClickListener {
                    onDetailClickListener(invoice)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val binding = AdapterHistoryInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}