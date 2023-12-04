package id.co.pspmobile.ui.schedule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.responses.calendarschedule.ComboYearResponseItem
import id.co.pspmobile.data.network.responses.calendarschedule.ContentX
import id.co.pspmobile.data.network.responses.calendarschedule.Content
import id.co.pspmobile.data.network.responses.calendarschedule.Lesson
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.ActivityScheduleBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.hideLottieLoader
import id.co.pspmobile.ui.Utils.showLottieLoader
import id.co.pspmobile.ui.Utils.snackbar
import id.co.pspmobile.ui.Utils.visible
import java.util.Calendar

@AndroidEntryPoint
class ScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScheduleBinding
    private val viewModel: ScheduleViewModel by viewModels()

    var allLesson = ArrayList<ContentX>()
    var allYear = ArrayList<ComboYearResponseItem>()
    var schedulePerDay = ArrayList<Lesson>()
    var companyId = ""
    val days = arrayOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")
    private var currentDay = ""
    var callers = ArrayList<CallerIdentity>()
    private lateinit var selectedCaller: CallerIdentity
    private var selectedYear = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        companyId = viewModel.getUser().activeCompany.id

        binding.progressBar.visible(false)

        viewModel.allLessonResponse.observe(this){
            binding.progressBar.visible(it is Resource.Loading)
            if(it is Resource.Success){
                Log.d("allLesson", it.value.toString())
                allLesson = it.value.content as ArrayList<ContentX>
                Log.d("allLesson", allLesson.toString())
                getCalendarCombo()
            }
            else if (it is Resource.Failure){
//                handleApiError(binding.root, it)
            }
        }

        viewModel.calendarComboResponse.observe(this){
            binding.progressBar.visible(it is Resource.Loading)
            if(it is Resource.Success){
                allYear = it.value
                for (year in allYear){
                    if (year.defaultYear){
                        selectedYear = year.year
                        break
                    }
                }
                getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
            }
            else if (it is Resource.Failure){
//                handleApiError(binding.root, it)
            }
        }

        viewModel.schedulePerDayResponse.observe(this){
            when(it is Resource.Loading){
                true -> showLottieLoader(supportFragmentManager)
                else -> hideLottieLoader(supportFragmentManager)
            }
            if(it is Resource.Success){
                schedulePerDay = if (it.value.content.isEmpty()){
                    binding.root.snackbar(resources.getString(R.string.no_schedule))
                    ArrayList<Lesson>()
                }else{
                    it.value.content[0].lessons as ArrayList<Lesson>
                }
                val adapter = LessonAdapter()
                adapter.setScheduleList(schedulePerDay)
                adapter.setLessonList(allLesson)
                binding.rvSchedule.adapter = adapter
                Log.d("schedulePerDay", schedulePerDay.toString())
            }
            else if (it is Resource.Failure){
                schedulePerDay = ArrayList<Lesson>()
                val adapter = LessonAdapter()
                adapter.setScheduleList(schedulePerDay)
                adapter.setLessonList(allLesson)
                binding.rvSchedule.adapter = adapter
            }
        }

        callers = viewModel.getUser().user.accounts[0].callerIdentities as ArrayList<CallerIdentity>
        if (callers.size < 1){
            Toast.makeText(this, "Akun anda kosong, anda tidak memiliki akses ke jadwal", Toast.LENGTH_SHORT).show()
            finish()
        } else{
            selectedCaller = callers[0]
        }

        init()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnPrev.setOnClickListener {
            prevDay()
        }

        binding.btnNext.setOnClickListener {
            nextDay()
        }

        val calendar = Calendar.getInstance()
        val day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1]
        currentDay = day
        configureAsset()

//        val adapter = ArrayAdapter(this, R.layout.item_spinner, callers)
//        binding.spinner.adapter = adapter
//        binding.spinner.setSelection(0)
//        binding.spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
//                selectedCaller = callers[position]
//                getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
//            }
//
//            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
//                selectedCaller = callers[0]
//                getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
//            }
//        }

        // Assuming you have a Spinner in your layout with the ID "spinner"
        val spinner: Spinner = findViewById(R.id.spinner)
        // Create the custom adapter
        val callerAdapter = CallerAdapter(this, callers)
        // Set the adapter to the Spinner
        spinner.adapter = callerAdapter
        // Set a listener for item selection if needed
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Handle item selection if needed
                selectedCaller = callers[position]
                getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    fun init(){
        getAllLesson()

    }

    fun prevDay(){
        val index = days.indexOf(currentDay)
        if (index > 0){
            currentDay = days[index - 1]
            getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
        } else if (index == 0){
            currentDay = days[6]
            getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
        }
        configureAsset()
    }

    fun configureAsset(){
        if (viewModel.getLanguage() == "en"){
            binding.txtDay.text = currentDay
        }else {
            val indoDays = arrayOf("MINGGU", "SENIN", "SELASA", "RABU", "KAMIS", "JUMAT", "SABTU")
            binding.txtDay.text = indoDays[days.indexOf(currentDay)]
        }
        when(currentDay){
            "SUNDAY" -> {
                binding.imgSchedule.setImageResource(R.drawable.ic_calendar_sun)
                binding.llDayName.setBackgroundResource(R.drawable.bg_schedule_sun)
            }
            "MONDAY" -> {
                binding.imgSchedule.setImageResource(R.drawable.ic_calendar_mon)
                binding.llDayName.setBackgroundResource(R.drawable.bg_schedule_mon)
            }
            "TUESDAY" -> {
                binding.imgSchedule.setImageResource(R.drawable.ic_calendar_tue)
                binding.llDayName.setBackgroundResource(R.drawable.bg_schedule_tue)
            }
            "WEDNESDAY" -> {
                binding.imgSchedule.setImageResource(R.drawable.ic_calendar_wed)
                binding.llDayName.setBackgroundResource(R.drawable.bg_schedule_wed)
            }
            "THURSDAY" -> {
                binding.imgSchedule.setImageResource(R.drawable.ic_calendar_thu)
                binding.llDayName.setBackgroundResource(R.drawable.bg_schedule_thu)
            }
            "FRIDAY" -> {
                binding.imgSchedule.setImageResource(R.drawable.ic_calendar_fri)
                binding.llDayName.setBackgroundResource(R.drawable.bg_schedule_fri)
            }
            "SATURDAY" -> {
                binding.imgSchedule.setImageResource(R.drawable.ic_calendar_sat)
                binding.llDayName.setBackgroundResource(R.drawable.bg_schedule_sat)
            }

        }
    }

    fun nextDay(){
        val index = days.indexOf(currentDay)
        if (index < 6){
            currentDay = days[index + 1]
            getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
        } else if (index == 6){
            currentDay = days[0]
            getSchedulePerDay(selectedYear, selectedCaller.callerId, currentDay)
        }
        configureAsset()
    }

    fun getAllLesson(){
        viewModel.getAllLesson(
            companyId,
            0,
            50,
            "lessonName",
            1
        )
    }

    fun getCalendarCombo(){
        viewModel.getCalendarCombo(
            companyId
        )
    }

    fun getSchedulePerDay(year: String, callerId: String, day: String){
        viewModel.getSchedulePerDay(
            companyId,
            year,
            callerId,
            true,
            day,
            0,
            50,
            "dayCode",
            1
        )
    }

}