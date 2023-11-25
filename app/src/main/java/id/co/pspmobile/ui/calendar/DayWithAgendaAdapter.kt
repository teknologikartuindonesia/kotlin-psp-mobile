package id.co.pspmobile.ui.calendar

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.data.network.model.ModelCalendar
import id.co.pspmobile.databinding.ItemCalendarDayWithAgendaBinding
import java.util.Calendar

class DayWithAgendaAdapter: RecyclerView.Adapter<DayWithAgendaAdapter.ViewHolder>() {

    private var daysArray = listOf<ModelCalendar>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DayWithAgendaAdapter.ViewHolder {
        val binding = ItemCalendarDayWithAgendaBinding.inflate(
            parent.context.getSystemService(android.view.LayoutInflater::class.java),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayWithAgendaAdapter.ViewHolder, position: Int) {
        holder.bind(daysArray[position])
    }

    override fun getItemCount(): Int {
        return daysArray.size
    }

    fun setDaysArray(daysArray: List<ModelCalendar>, yearNumber: Int, monthNumber: Int){
        this.daysArray = daysArray
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yearNumber)
        calendar.set(Calendar.MONTH, monthNumber - 1) // Calendar months are 0-indexed
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        Log.d("Calendar", "setDaysArray: ${calendar.get(Calendar.DAY_OF_WEEK)}")

        when(calendar.get(Calendar.DAY_OF_WEEK)){
            1 -> this.daysArray = List(6) { ModelCalendar("", listOf()) } + daysArray
            2 -> this.daysArray = List(0) { ModelCalendar("", listOf()) } + daysArray
            3 -> this.daysArray = List(1) { ModelCalendar("", listOf()) } + daysArray
            4 -> this.daysArray = List(2) { ModelCalendar("", listOf()) } + daysArray
            5 -> this.daysArray = List(3) { ModelCalendar("", listOf()) } + daysArray
            6 -> this.daysArray = List(4) { ModelCalendar("", listOf()) } + daysArray
            7 -> this.daysArray = List(5) { ModelCalendar("", listOf()) } + daysArray
        }

        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCalendarDayWithAgendaBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(day: ModelCalendar) {
            with(binding) {
                // day.tgl is "2023-11-01" convert with format "d"
                if (day.tgl.isNotEmpty()) {
                    txtCalendarDay.text = day.tgl.split("-")[2].toInt().toString()
                } else {
                    txtCalendarDay.text = ""
                }
                if (day.content.isNotEmpty()) {
                    rvCalendarAgenda.visibility = View.VISIBLE
                    when (day.content.size) {
                        0 -> {
                            // set span count to 0
                            rvCalendarAgenda.layoutManager = androidx.recyclerview.widget.GridLayoutManager(binding.root.context, 0)
                        }
                        1 -> {
                            // set span count to 1
                            rvCalendarAgenda.layoutManager = androidx.recyclerview.widget.GridLayoutManager(binding.root.context, 1)
                        }
                        2 -> {
                            // set span count to 2
                            rvCalendarAgenda.layoutManager = androidx.recyclerview.widget.GridLayoutManager(binding.root.context, 2)
                        }
                        3 -> {
                            // set span count to 3
                            rvCalendarAgenda.layoutManager = androidx.recyclerview.widget.GridLayoutManager(binding.root.context, 3)
                        }
                        else -> {
                            // cut to only showing 3 dots
                            val dotAgendaArray = day.content.subList(0, 3)
                        }
                    }
                } else {
                    rvCalendarAgenda.visibility = View.INVISIBLE
                }
                val dotAdapter = DotAgendaAdapter(day.content)
                rvCalendarAgenda.adapter = dotAdapter
            }
        }
    }
}

class DotAgendaAdapter(private val dotAgendaArray: List<String>) :
    RecyclerView.Adapter<DotAgendaAdapter.DotAgendaViewHolder>() {

    class DotAgendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dotLL: LinearLayout = itemView.findViewById(R.id.ll_dot)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DotAgendaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dot, parent, false)
        return DotAgendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DotAgendaViewHolder, position: Int) {
        when(dotAgendaArray[position]){
            "red" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.red))
            "yellow" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.yellow))
            "green" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.green))
            "darkgreen" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.darkgreen))
            "blue" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.blue))
            "darkblue" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.darkblue))
            "brown" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.brown))
            "grey" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.grey))
            "purple" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.purple))
            "maroon" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.maroon))
            "silver" -> setRoundedLinearLayoutColor(holder.dotLL, holder.itemView.context.getColor(R.color.silver))
        }
    }

    override fun getItemCount(): Int {
        return dotAgendaArray.size
    }

    private fun setRoundedLinearLayoutColor(linearLayout: LinearLayout, color: Int) {
        val background = linearLayout.background
        if (background is GradientDrawable) {
            // Change the color of the GradientDrawable
            background.setColor(color)
        }
    }
}