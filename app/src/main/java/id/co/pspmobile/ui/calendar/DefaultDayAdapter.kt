package id.co.pspmobile.ui.calendar

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.databinding.ItemCalendarDefaultDayBinding

class DefaultDayAdapter: RecyclerView.Adapter<DefaultDayAdapter.ViewHolder>() {

    private var daysArray = listOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarDefaultDayBinding.inflate(
            parent.context.getSystemService(android.view.LayoutInflater::class.java),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return daysArray.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(daysArray[position])
    }

    fun setDaysArray(daysArray: List<String>) {
        this.daysArray = daysArray
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCalendarDefaultDayBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(day: String) {
            with(binding) {
                txtDefaultDay.text = day
            }

        }
    }
}