package id.co.pspmobile.ui.mutation.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.data.network.report.MutationDto
import id.co.pspmobile.databinding.AdapterMutationBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.Utils.formatDateTime

class MutationAdapter(private val context: Context) :
    RecyclerView.Adapter<MutationAdapter.ViewHolder>() {

    private var list = ArrayList<MutationDto>()

    @SuppressLint("NotifyDataSetChanged")
    fun setMutations(item: List<MutationDto>) {
        list.addAll(item)
        notifyDataSetChanged()
    }
    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: AdapterMutationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(mutation: MutationDto) {
            with(binding) {
                tvDateTime.text = formatDateTime(mutation.dateTime, "dd-MM-YYYY hh:mm")
                if (mutation.debit == 0.0) {
                    tvDebitCredit.text = "Rp " + formatCurrency(mutation.credit)
                    tvDebitCredit.setTextColor(context.getColor(R.color.green))
                } else {
                    tvDebitCredit.setTextColor(context.getColor(R.color.danger))
                    tvDebitCredit.text = "-Rp " + formatCurrency(mutation.debit)
                }
                tvTransactionName.text = mutation.transactionName
                tvBalance.text = "Saldo: Rp " + formatCurrency(mutation.balance)
                tvNote.text = mutation.callerName

                if (mutation.tags.indexOf("invoice") >= 0) {
                    ivIcon.setImageDrawable(itemView.context?.let {
                        ActivityCompat.getDrawable(it, R.drawable.ic_home_invoice)
                    })
                } else if (mutation.tags.indexOf("donation") >= 0) {
                    ivIcon.setImageDrawable(itemView.context?.let {
                        ActivityCompat.getDrawable(it, R.drawable.ic_home_donation)
                    })
                } else if (mutation.tags.indexOf("topup") >= 0) {
                    ivIcon.setImageDrawable(itemView.context?.let {
                        ActivityCompat.getDrawable(it, R.drawable.ic_home_topup)
                    })
                } else if (mutation.tags.indexOf("admin topup") >= 0) {
                    ivIcon.setImageDrawable(itemView.context?.let {
                        ActivityCompat.getDrawable(it, R.drawable.ic_home_transaction)
                    })
                } else if (mutation.tags.indexOf("cashout") >= 0) {
                    ivIcon.setImageDrawable(itemView.context?.let {
                        ActivityCompat.getDrawable(it, R.drawable.ic_home_account)
                    })
                } else if (mutation.tags.indexOf("card") >= 0) {
                    ivIcon.setImageDrawable(itemView.context?.let {
                        ActivityCompat.getDrawable(it, R.drawable.ic_home_digital_card)
                    })
                } else {
                    ivIcon.setImageDrawable(itemView.context?.let {
                        ActivityCompat.getDrawable(it, R.drawable.ic_home_schedule)
                    })
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MutationAdapter.ViewHolder {
        val binding =
            AdapterMutationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MutationAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}