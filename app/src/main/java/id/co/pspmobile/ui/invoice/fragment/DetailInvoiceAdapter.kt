package id.co.pspmobile.ui.invoice.fragment

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.invoice.Detail
import id.co.pspmobile.data.network.invoice.InvoiceDto
import id.co.pspmobile.databinding.AdapterDetailInvoiceBinding
import id.co.pspmobile.databinding.AdapterSummaryInvoiceBinding
import id.co.pspmobile.ui.Utils.formatCurrency

class DetailInvoiceAdapter : RecyclerView.Adapter<DetailInvoiceAdapter.ViewHolder>() {

    private var list = ArrayList<Detail>()

    @SuppressLint("NotifyDataSetChanged")
    fun setDetail(item: ArrayList<Detail>) {
        list.addAll(item)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterDetailInvoiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(detail: Detail) {
            Log.e("Detail", detail.toString())
            with(binding) {
                detailName.text = detail.title
                amount.text = "Rp " + formatCurrency(detail.amount)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailInvoiceAdapter.ViewHolder {
        val binding =
            AdapterDetailInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailInvoiceAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}