package id.co.pspmobile.ui.calendar

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.data.network.responses.calendarschedule.ContentXX
import id.co.pspmobile.databinding.ItemCalendarNoteBinding

class CalendarNoteAdapter: RecyclerView.Adapter<CalendarNoteAdapter.ViewHolder>() {

    private var daysArray = listOf<ContentXX>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarNoteAdapter.ViewHolder {
        val binding = ItemCalendarNoteBinding.inflate(
            parent.context.getSystemService(android.view.LayoutInflater::class.java),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarNoteAdapter.ViewHolder, position: Int) {
        holder.bind(daysArray[position])
    }

    override fun getItemCount(): Int {
        return daysArray.size
    }

    fun setDaysArray(daysArray: List<ContentXX>) {
        this.daysArray = daysArray
        Log.d("CalendarNoteAdapter", "setDaysArray: ${this.daysArray}")
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCalendarNoteBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(day: ContentXX) {
            with(binding) {
                txtNote.text = day.description
                val startAndEndDate = if(day.startDate != day.endDate)
                    day.startDate.substring(8, 10) + " - " + day.endDate.substring(8, 10)
                else
                    day.startDate.substring(8, 10)
                txtDate.text = startAndEndDate
                when(day.color){
                    "red" -> txtDate.setTextColor(itemView.context.getColor(R.color.red))
                    "yellow" -> txtDate.setTextColor(itemView.context.getColor(R.color.yellow))
                    "green" -> txtDate.setTextColor(itemView.context.getColor(R.color.green))
                    "darkgreen" -> txtDate.setTextColor(itemView.context.getColor(R.color.darkgreen))
                    "blue" -> txtDate.setTextColor(itemView.context.getColor(R.color.blue))
                    "darkblue" -> txtDate.setTextColor(itemView.context.getColor(R.color.darkblue))
                    "brown" -> txtDate.setTextColor(itemView.context.getColor(R.color.brown))
                    "grey" -> txtDate.setTextColor(itemView.context.getColor(R.color.grey))
                    "purple" -> txtDate.setTextColor(itemView.context.getColor(R.color.purple))
                    "maroon" -> txtDate.setTextColor(itemView.context.getColor(R.color.maroon))
                    "silver" -> txtDate.setTextColor(itemView.context.getColor(R.color.silver))
                }
            }

        }
    }

}