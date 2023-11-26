package id.co.pspmobile.ui.schedule

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.responses.calendarschedule.ContentX
import id.co.pspmobile.data.network.responses.calendarschedule.Lesson
import id.co.pspmobile.databinding.ItemScheduleBinding

class LessonAdapter: RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    private lateinit var scheduleList: ArrayList<Lesson>
    private var lessonList = ArrayList<ContentX>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LessonAdapter.ViewHolder {
        val binding = ItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LessonAdapter.ViewHolder, position: Int) {
        holder.bind(scheduleList[position])
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    fun setScheduleList(scheduleList: ArrayList<Lesson>) {
        this.scheduleList = scheduleList
        Log.d("scheduleList", scheduleList.toString())
        notifyDataSetChanged()
    }

    fun setLessonList(lessonList: ArrayList<ContentX>) {
        this.lessonList = lessonList
        Log.d("lessonList", lessonList.toString())
    }

    inner class ViewHolder(private val binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(scheduleModel: Lesson) {
            with(binding){
                val time = scheduleModel.startTime + " - " + scheduleModel.endTime
                val name = ""
                for (lesson in lessonList) {
                    if (lesson._id == scheduleModel.lessonId) {
                        txtName.text = lesson.lessonName
                        Log.d("timeAndName", "$time $name")
                    }
                }
                txtTime.text = time


            }
        }

    }
}