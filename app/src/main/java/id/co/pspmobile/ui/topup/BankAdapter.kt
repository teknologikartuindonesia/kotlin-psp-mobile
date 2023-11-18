package id.co.pspmobile.ui.topup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.databinding.BankAdapterBinding

class BankAdapter : RecyclerView.Adapter<BankAdapter.ViewHolder>() {

    private lateinit var list: List<String>
    private var onItemClickListener : View.OnClickListener? = null

    fun setBanks(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListerner(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: BankAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bankName: String) {
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
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_bjb) })
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
        val binding = BankAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}