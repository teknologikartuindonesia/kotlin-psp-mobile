package id.co.pspmobile.ui.HomeBottomNavigation.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import id.co.pspmobile.R

class CarouselRvAdapter(
    private val list: List<String>
): RecyclerView.Adapter<CarouselRvAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(
            R.layout.item_broadcast_image, parent, false)
        return ViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = holder.itemView.findViewById<ShapeableImageView>(R.id.img_broadcast_detail)
        Glide.with(holder.itemView.context)
            .load(list[position])
            .placeholder(R.drawable.info_news_default)
            .into(img)
    }


}