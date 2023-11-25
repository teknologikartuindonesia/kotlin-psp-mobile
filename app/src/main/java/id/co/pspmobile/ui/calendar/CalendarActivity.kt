package id.co.pspmobile.ui.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.ModelCalendar
import id.co.pspmobile.data.network.responses.calendarschedule.ContentXX
import id.co.pspmobile.databinding.ActivityCalendarBinding
import id.co.pspmobile.ui.Utils.hideLottieLoader
import id.co.pspmobile.ui.Utils.showLottieLoader
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private val viewModel: CalendarViewModel by viewModels()

    private var daysArray = listOf<String>()
    private var monthsArray = listOf<String>()

    private var startDate = ""
    private var endDate = ""
    private var selectedMonth = ""
    private var selectedYear = ""

    private lateinit var calendarWithAgenda: List<ModelCalendar>
//    var daysBefore = mutableListOf<String>()

    /*

    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Calendar", "onCreate: ${viewModel.getLanguage()}")

        if (viewModel.getLanguage() == "id"){
            daysArray = listOf("Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min")
            monthsArray = listOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
                "Agustus", "September", "Oktober", "November", "Desember")
        } else {
            daysArray = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            monthsArray = listOf("January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December")
        }

        selectedMonth = SimpleDateFormat("MM").format(Calendar.getInstance().time)
        selectedYear = SimpleDateFormat("yyyy").format(Calendar.getInstance().time)
        setStartAndEndDate(selectedMonth, selectedYear)

        binding.btnNextMonth.setOnClickListener {
            nextMonth()
        }

        binding.btnPrevMonth.setOnClickListener {
            prevMonth()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
//
        viewModel.allAgendaResponse.observe(this){
            when(it is Resource.Loading){
                true -> showLottieLoader(supportFragmentManager)
                else -> hideLottieLoader(supportFragmentManager)
            }
            if(it is Resource.Success){
                Log.d("Calendar", "onCreate: ${it.value.content}")
                setNote(it.value.content)
                fillContent(calendarWithAgenda, it.value.content)
                fillFullCalendar(calendarWithAgenda)
//                gett(it.value.content)
            }
            else if (it is Resource.Failure){
                Log.d("Calendar", "onCreate: ${it.errorBody}")
            }
        }
//
        getAgenda()
        setDefaultDay()
    }

    private fun setNote(note: List<ContentXX>){
        val ada = CalendarNoteAdapter()
        ada.setDaysArray(note)
        binding.rvNoteAllDays.adapter = ada
    }
    private fun setDefaultDay(){
        // Set default day on top of calendar
        val ada = DefaultDayAdapter()
        ada.setDaysArray(daysArray)
        binding.rvDefaultDay.adapter = ada
    }

    fun getAgenda(){
        viewModel.getAgenda(startDate, endDate)
    }

    fun fillContent(listOfGeneratedCalendar: List<ModelCalendar>, listContent: List<ContentXX>) {
        Log.d("Calendar", "fillContent(${listOfGeneratedCalendar.size}): $listOfGeneratedCalendar")
        for (calendar in listOfGeneratedCalendar) {
            if (calendar.tgl == ""){ // go to next iteration if the date is empty
                continue
            }
            for (content in listContent) {
                val contentStartDate = content.startDate
                val contentEndDate = content.endDate

                if (calendar.tgl == contentStartDate && calendar.tgl == contentEndDate) {
                    // Agenda held only on this day
                    calendar.content += content.color
                } else {
                    // Agenda held for a range of days
                    val calendarDate = SimpleDateFormat("yyyy-MM-dd").parse(calendar.tgl)!!

                    if (calendarDate.after(SimpleDateFormat("yyyy-MM-dd").parse(contentStartDate)!!)
                        && calendarDate.before(SimpleDateFormat("yyyy-MM-dd").parse(contentEndDate)!!)
                    ) {
                        calendar.content += content.color
                    }
                }
            }
        }
        Log.d("Calendar", "fillContent(${listOfGeneratedCalendar.size}): $listOfGeneratedCalendar")
        Log.d("Calendar", "fillContent(${calendarWithAgenda.size}): $calendarWithAgenda")
    }

    fun fillFullCalendar(listOfGeneratedCalendar: List<ModelCalendar>){
        val ada = DayWithAgendaAdapter()
        ada.setDaysArray(listOfGeneratedCalendar, selectedYear.toInt(), selectedMonth.toInt())
        binding.rvAllDays.adapter = ada
    }

    private fun setStartAndEndDate(month: String, year: String){
        val monthNumber = month.toInt()
        val yearNumber = year.toInt()


        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yearNumber)
        calendar.set(Calendar.MONTH, monthNumber - 1) // Calendar months are 0-indexed
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        val startDate = calendar.time


        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1) // Move to the last day of the month

        val endDate = calendar.time

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val formattedStartDate = formatter.format(startDate)
        val formattedEndDate = formatter.format(endDate)
        this.startDate = formattedStartDate
        this.endDate = formattedEndDate

        selectedMonth = SimpleDateFormat("MM").format(startDate)
        selectedYear = SimpleDateFormat("yyyy").format(startDate)

        binding.txtMonthYear.text = "${monthsArray[monthNumber - 1]} $yearNumber"

        calendarWithAgenda = generateMonthCalendar(this.startDate, this.endDate)

        Log.d("Calendar", "setStartAndEndDate: $formattedStartDate - $formattedEndDate")
        Log.d("Calendar", "setStartAndEndDate: $calendarWithAgenda")
    }
    private fun nextMonth(){
        val monthNumber = selectedMonth.toInt()
        val yearNumber = selectedYear.toInt()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yearNumber)
        calendar.set(Calendar.MONTH, monthNumber - 1) // Calendar months are 0-indexed
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        calendar.add(Calendar.MONTH, 1)

        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        selectedMonth = month.toString()
        selectedYear = year.toString()

        setStartAndEndDate(selectedMonth, selectedYear)
        getAgenda()
    }
    private fun prevMonth(){
        val monthNumber = selectedMonth.toInt()
        val yearNumber = selectedYear.toInt()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, yearNumber)
        calendar.set(Calendar.MONTH, monthNumber - 1) // Calendar months are 0-indexed
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        calendar.add(Calendar.MONTH, -1)

        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        selectedMonth = month.toString()
        selectedYear = year.toString()

        setStartAndEndDate(selectedMonth, selectedYear)
        getAgenda()
    }

    fun generateMonthCalendar(startDate: String, endDate: String): List<ModelCalendar> {
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(startDate)!!

        val monthCalendars = mutableListOf<ModelCalendar>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        val endCalendar = Calendar.getInstance()
        endCalendar.time = SimpleDateFormat("yyyy-MM-dd").parse(endDate)!!

        while (!calendar.time.after(endCalendar.time)) {
            val date = dateFormat.format(calendar.time)
            monthCalendars.add(ModelCalendar(date, mutableListOf()))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return monthCalendars
    }
}