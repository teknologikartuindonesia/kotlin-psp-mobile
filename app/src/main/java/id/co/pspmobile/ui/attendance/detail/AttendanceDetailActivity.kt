package id.co.pspmobile.ui.attendance.detail

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.ActivityAttendanceDetailBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AttendanceDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAttendanceDetailBinding
    private val viewModel: AttendanceDetailViewModel by viewModels()
    private lateinit var attendanceDetailAdapter: AttendanceDetailAdapter

    var selectedDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val callerIdentity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("callerIdentity", CallerIdentity::class.java)
        } else {
            intent.getSerializableExtra("callerIdentity") as CallerIdentity
        }

        binding.apply {
            if (callerIdentity!!.photoUrl!!.isNotEmpty()) {
                Picasso.get().load(viewModel.getBaseUrl() + "/main_a/image/get/" + callerIdentity.photoUrl + "/pas").noFade().fit()
                    .into(ivPhoto);
            }
            tvAccountName.text = callerIdentity.name
            tvNis.text = "NIS "+callerIdentity.callerId
            btnCalendar.setOnClickListener {
                // open date picker dialog to get date with format yyyy-MM-dd
//                val dpd = DatePickerDialog(this@AttendanceDetailActivity, { _, year, month, dayOfMonth ->
//                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
//                    val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//                    viewModel.getAttendance(callerIdentity.callerId, formattedDate)
//                }, LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth)
                val calendar = Calendar.getInstance()
                val dpd = DatePickerDialog(
                    this@AttendanceDetailActivity,
                    { _, year, month, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(year, month, dayOfMonth)

                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        selectedDate = formatter.format(selectedCalendar.time)

                        viewModel.getAttendance(callerIdentity.callerId!!, selectedDate)
                    },
                    selectedDate.split("-")[0].toInt(),
                    selectedDate.split("-")[1].toInt() - 1,
                    selectedDate.split("-")[2].toInt()
                )
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", dpd)
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, resources.getString(R.string.cancel), dpd)
                dpd.show()

            }
        }

        viewModel.attendanceResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                attendanceDetailAdapter.setAttendance(it.value)
                binding.apply {
                    rvAttendance.setHasFixedSize(true)
                    rvAttendance.adapter = attendanceDetailAdapter
                    if (it.value.isNotEmpty()) {
                        tvInformation.visible(false)
                    } else {
                        tvInformation.visible(true)
                    }
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvAttendance, it)
            }
        }

        attendanceDetailAdapter = AttendanceDetailAdapter()
        attendanceDetailAdapter.setBaseUrl(viewModel.getBaseUrl())

        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
        viewModel.getAttendance(
            callerIdentity!!.callerId!!,
            selectedDate
        )

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}