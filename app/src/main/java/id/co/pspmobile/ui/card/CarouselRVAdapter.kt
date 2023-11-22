package id.co.pspmobile.ui.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.infonews.Content
import id.co.pspmobile.databinding.ItemDigitalCardBinding
import id.co.pspmobile.databinding.ItemHomeInfoNewsBinding
import id.co.pspmobile.ui.HomeBottomNavigation.home.InfoNewsAdapter

class CarouselRVAdapter(private val carouselDataList: ArrayList<String>) :
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_card, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.textview)
        textView.text = carouselDataList[position]
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

}