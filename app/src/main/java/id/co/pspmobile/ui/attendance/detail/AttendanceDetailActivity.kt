package id.co.pspmobile.ui.attendance.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.ActivityAttendanceDetailBinding

@AndroidEntryPoint
class AttendanceDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAttendanceDetailBinding
    private val viewModel: AttendanceDetailViewModel by viewModels()
    private lateinit var attendanceDetailAdapter: AttendanceDetailAdapter

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
            tvNis.text = callerIdentity.callerId
        }

        attendanceDetailAdapter = AttendanceDetailAdapter()
    }
}