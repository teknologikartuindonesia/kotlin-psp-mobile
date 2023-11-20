package id.co.pspmobile.ui.topup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.databinding.AdapterBankBinding

class BankAdapter : RecyclerView.Adapter<BankAdapter.ViewHolder>() {

    private lateinit var list: ArrayList<String>
    private var onItemClickListener : View.OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setBanks(list: ArrayList<String>) {
        this.list = list
        this.list.remove("IDN")
        this.list.remove("XENDIT DISBURSEMENT")

        notifyDataSetChanged()
    }

    fun setOnItemClickListerner(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: AdapterBankBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bank: String) {
            var bankName = bank
            bankName = bankName.replace("OVERBOOK", "").trim()
            bankName = bankName.replace("XENDIT", "").trim()

            with(binding) {
                itemView.tag = bankName
                itemView.setOnClickListener(onItemClickListener)

                tvBankName.text = bankName

                when (bankName) {
                    "BCA" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bca) })
                    }
                    "BANKJABAR" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bjb) })
                    }
                    "BANKJATIM" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bjs) })
                    }
                    "BNI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bni) })
                    }
                    "BRI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bri) })
                    }
                    "BSI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bsi) })
                    }
                    "BTN" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_btn) })
                    }
                    "BTPN" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_btpn) })
                    }
                    "CIMB" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_cimb) })
                    }
                    "DANAMON" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_danamon) })
                    }
                    "MANDIRI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_mandiri) })
                    }
                    "MAYBANK" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_maybank) })
                    }
                    "MUAMALAT" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_muamalat) })
                    }
                    "NTB SYARIAH" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_ntbs) })
                    }
                    "OCBC" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_ocbc) })
                    }
                    "PERMATA SYARIAH" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_permata_syariah) })
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankAdapter.ViewHolder {
        val binding = AdapterBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}