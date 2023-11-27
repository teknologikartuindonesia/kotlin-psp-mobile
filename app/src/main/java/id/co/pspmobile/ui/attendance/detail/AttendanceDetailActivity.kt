package id.co.pspmobile.ui.attendance.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.ActivityAttendanceDetailBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AttendanceDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAttendanceDetailBinding
    private val viewModel: AttendanceDetailViewModel by viewModels()
    private lateinit var attendanceDetailAdapter: AttendanceDetailAdapter

    @RequiresApi(Build.VERSION_CODES.O)
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
            if (callerIdentity!!.photoUrl.isNotEmpty()) {
                Picasso.get().load(viewModel.getBaseUrl() + "/main_a/image/get/" + callerIdentity.photoUrl + "/pas").noFade().fit()
                    .into(ivPhoto);
            }
            tvAccountName.text = callerIdentity.name
            tvNis.text = "NIS "+callerIdentity.callerId
        }

        viewModel.attendanceResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                attendanceDetailAdapter.setAttendance(it.value)
                binding.apply {
                    rvAttendance.setHasFixedSize(true)
                    rvAttendance.adapter = attendanceDetailAdapter
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvAttendance, it)
            }
        }

        attendanceDetailAdapter = AttendanceDetailAdapter()
        attendanceDetailAdapter.setBaseUrl(viewModel.getBaseUrl())

        viewModel.getAttendance(
            callerIdentity!!.callerId,
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        )

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}