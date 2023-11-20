package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.AdapterInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class InvoiceAdapter : RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {

    private var list=ArrayList<InvoiceDto>()
    private lateinit var onPayClickListener : (invoice: InvoiceDto) -> (Unit)

    @SuppressLint("NotifyDataSetChanged")
    fun setInvoices(item: ArrayList<InvoiceDto>) {
        list.addAll(item)
        notifyDataSetChanged()
    }

    fun setOnPayClickListener(onPayClickListener: (invoice: InvoiceDto) -> (Unit)) {
        this.onPayClickListener = onPayClickListener
    }

    inner class ViewHolder(private val binding: AdapterInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
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
                tvTotal.text = formatCurrency(invoice.amount)
                tvPaidAmount.text = formatCurrency(invoice.paidAmount)
                tvUnpaidAmount.text = formatCurrency(invoice.amount - invoice.paidAmount)

                btnPay.setOnClickListener {
                    onPayClickListener(invoice)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceAdapter.ViewHolder {
        val binding = AdapterInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

}