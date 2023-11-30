package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.AdapterHistoryInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.invoice.InvoiceViewModel

class HistoryAdapter(private val context: Context, private val viewModel: InvoiceViewModel) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var list = ArrayList<InvoiceDto>()
    private lateinit var onDetailClickListener: (invoice: InvoiceDto) -> Unit
    var vm = viewModel

    @SuppressLint("NotifyDataSetChanged")
    fun setInvoices(item: ArrayList<InvoiceDto>) {
        list.addAll(item)
        notifyDataSetChanged()
    }

    fun setOnDetailClickListener(onDetailClickListener: (invoice: InvoiceDto) -> (Unit)) {
        this.onDetailClickListener = onDetailClickListener
    }

    inner class ViewHolder(private val binding: AdapterHistoryInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
                tvPaidAmount.text = formatCurrency(invoice.paidAmount)
                tvUnpaidAmount.text = formatCurrency(invoice.amount - invoice.paidAmount)

                btnDetail.setOnClickListener {
//                    onDetailClickListener(invoice)
                    val bottomSheetDialogFragment: BottomSheetDialogFragment =
                        BottomSheetDetailInvoice("", invoice)
                    bottomSheetDialogFragment.show(
                        (context as FragmentActivity).supportFragmentManager,
                        bottomSheetDialogFragment.tag
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val binding =
            AdapterHistoryInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

}