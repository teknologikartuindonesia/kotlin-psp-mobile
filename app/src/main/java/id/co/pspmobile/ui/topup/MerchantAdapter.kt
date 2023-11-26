package id.co.pspmobile.ui.topup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.databinding.AdapterBankBinding

class MerchantAdapter : RecyclerView.Adapter<MerchantAdapter.ViewHolder>() {

    private lateinit var list: List<String>
    private var onItemClickListener : View.OnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setMerchants(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: AdapterBankBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(merchant: String) {
            with(binding) {
                itemView.tag = merchant
                itemView.setOnClickListener(onItemClickListener)


                when (merchant) {
                    "INDOMARET" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_indomaret) })
                        tvBankName.text = "Indomaret"
                    }
                    "ALFAMART" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_alfamart) })
                        tvBankName.text = "Alfamart"
                    }
                    "GOPAY" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_gopay) })
                        tvBankName.text = "Gopay"
                    }
                    "TOKOPEDIA" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_tokopedia) })
                        tvBankName.text = "Tokopedia"
                    }
                    "SHOPEE" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_shopee) })
                        tvBankName.text = "Shopee"
                    }
                    "BLIBLI" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_blibli) })
                        tvBankName.text = "Blibli"
                    }
                    "AYOPOP" -> {
                        ivBank.setImageDrawable(itemView.context?.let { ActivityCompat.getDrawable(it, R.drawable.logo_ayopop) })
                        tvBankName.text = "Ayopop"
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchantAdapter.ViewHolder {
        val binding = AdapterBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MerchantAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}