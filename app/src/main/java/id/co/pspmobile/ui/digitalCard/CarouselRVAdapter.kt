package id.co.pspmobile.ui.digitalCard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.data.network.model.ModelDigitalCard
import id.co.pspmobile.databinding.ItemDigitalCardBinding
import id.co.pspmobile.ui.Utils.formatCurrency


class CarouselRVAdapter(
    private val carouselDataList: ArrayList<ModelDigitalCard>,
    private val context: Context
) :
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    private lateinit var binding: ItemDigitalCardBinding

    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.item_digital_card, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val name = holder.itemView.findViewById<TextView>(R.id.name_card)
        val balance = holder.itemView.findViewById<TextView>(R.id.balance)
        val nfc_id = holder.itemView.findViewById<TextView>(R.id.nfc_id)
        name.text = carouselDataList[position].name
        balance.text = "Rp " + formatCurrency(carouselDataList[position].cardBalance)
        nfc_id.text = carouselDataList[position].nfcId

    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

}