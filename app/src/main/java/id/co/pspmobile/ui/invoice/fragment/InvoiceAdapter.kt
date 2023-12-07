package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.AdapterInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.invoice.InvoicePaymentActivity
import id.co.pspmobile.ui.invoice.InvoiceViewModel


class InvoiceAdapter(private val context: Context,private val viewModel: InvoiceViewModel) : RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {

    private var list=ArrayList<InvoiceDto>()
    var vm = viewModel
    @SuppressLint("NotifyDataSetChanged")
    fun setInvoices(item: ArrayList<InvoiceDto>) {
        list.addAll(item)
        Log.e("list", "list ${list.size}")
        notifyDataSetChanged()
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
                    when (vm.getLanguage().toString()) {
                        "en" -> tvType.text = "CREDIT"
                        else -> tvType.text = "KREDIT"
                    }

                } else {
                    when (viewModel.getLanguage().toString()) {
                        "en" -> tvType.text = "CASH"
                        else -> tvType.text = "TUNAI"
                    }

                }
                tvTotal.text = formatCurrency(invoice.amount)
                tvPaidAmount.text = formatCurrency(invoice.paidAmount)
                tvUnpaidAmount.text = formatCurrency(invoice.amount - invoice.paidAmount)

                btnPay.setOnClickListener {
                    val i = Intent(context, InvoicePaymentActivity::class.java)
                    i.putExtra("content", Gson().toJson(invoice))
                    context.startActivity(i)
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
        notifyItemRangeRemoved(0, itemCount)
    }

    override fun getItemCount(): Int = list.size

}