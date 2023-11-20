package id.co.pspmobile.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ItemLoginBankBinding

class BankPartnerAdapter: RecyclerView.Adapter<BankPartnerAdapter.ViewHolder>() {

    private lateinit var menuArray: List<String>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BankPartnerAdapter.ViewHolder {
        val binding = ItemLoginBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BankPartnerAdapter.ViewHolder, position: Int) {
        holder.bind(menuArray[position])
    }

    override fun getItemCount(): Int {
        return menuArray.size
    }

    fun setMenuList(menuList: List<String>) {
        this.menuArray = menuList
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val binding:ItemLoginBankBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(menuModel: String) {
            with(binding) {
                when(menuModel) {
                    "TKI" -> {
                        imgLoginBank.setImageResource(R.drawable.logo_tki_blue)
                    }
                    "BSI" -> {
                        imgLoginBank.setImageResource(R.drawable.logo_bsi)
                    }
                    "BNI" -> {
                        imgLoginBank.setImageResource(R.drawable.logo_bni)
                    }
                    "BJS" -> {
                        imgLoginBank.setImageResource(R.drawable.logo_bjs)
                    }
                }
            }
        }
    }
}