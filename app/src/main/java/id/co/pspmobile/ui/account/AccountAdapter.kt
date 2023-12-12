package id.co.pspmobile.ui.account

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.AdapterAccountBinding

class AccountAdapter : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    private lateinit var list: List<CallerIdentity>
    private var baseUrl: String = ""

    @SuppressLint("NotifyDataSetChanged")
    fun setAccounts(list: List<CallerIdentity>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    inner class ViewHolder(private val binding: AdapterAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(callerIdentity: CallerIdentity) {
            with(binding) {
                if (callerIdentity.photoUrl!!.isNotEmpty()) {
                    Picasso.get()
                        .load(baseUrl + "/main_a/image/get/" + callerIdentity.photoUrl + "/pas")
                        .noFade().fit()
                        .into(ivPhoto);
                }
                tvAccountName.text =callerIdentity.name
                tvNis.text =  "NIS: " + callerIdentity.callerId
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountAdapter.ViewHolder {
        val binding =
            AdapterAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}