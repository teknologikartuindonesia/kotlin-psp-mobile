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

                when (bankName) {
                    "BCA" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bca) })
                        tvBankName.text = "Bank Central Asia"
                    }
                    "BANKJABAR" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bjb) })
                        tvBankName.text = "Bank JABAR"
                    }
                    "BANKJATIM" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bjs) })
                        tvBankName.text = "Bank Jatim Syariah"
                    }
                    "BNI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bni) })
                        tvBankName.text = "Bank BNI"
                    }
                    "BRI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bri) })
                        tvBankName.text = "Bank BRI"
                    }
                    "BSI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bsi) })
                        tvBankName.text = "Bank Syariah Indonesia"
                    }
                    "BTN" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_btn) })
                        tvBankName.text = "Bank BTN"
                    }
                    "BTPN" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_btpn) })
                        tvBankName.text = "Bank BTPN"
                    }
                    "CIMB" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_cimb) })
                        tvBankName.text = "Bank CIMB NIAGA"
                    }
                    "DANAMON" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_danamon) })
                        tvBankName.text = "Bank Danamon"
                    }
                    "MANDIRI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_mandiri) })
                        tvBankName.text = "Bank Mandiri"
                    }
                    "MAYBANK" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_maybank) })
                        tvBankName.text = "Bank Maybank"
                    }
                    "MUAMALAT" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_muamalat) })
                        tvBankName.text = "Bank Muamalat"
                    }
                    "NTB SYARIAH" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_ntbs) })
                        tvBankName.text = "Bank NTB Syariah"
                    }
                    "OCBC" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_ocbc) })
                        tvBankName.text = "Bank OCBC"
                    }
                    "PERMATA SYARIAH" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_permata_syariah) })
                        tvBankName.text = "Bank Permata Syariah"
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