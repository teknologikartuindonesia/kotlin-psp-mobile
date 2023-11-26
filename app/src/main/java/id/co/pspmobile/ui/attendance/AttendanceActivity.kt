package id.co.pspmobile.ui.attendance

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.ActivityAttendanceBinding
import id.co.pspmobile.ui.attendance.detail.AttendanceDetailActivity
import java.io.Serializable

@AndroidEntryPoint
class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAttendanceBinding
    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var attendanceAdapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userData = viewModel.getUserData()
        val accounts = userData.user.accounts.get(0).callerIdentities

        attendanceAdapter = AttendanceAdapter()
        attendanceAdapter.setOnItemClickListener { view ->
            val callerIdentity = view!!.tag as CallerIdentity
            val intent = Intent(this, AttendanceDetailActivity::class.java)
            intent.putExtra("callerIdentity", callerIdentity as Serializable)
            startActivity(intent)
        }
        attendanceAdapter.setBaseUrl(viewModel.getBaseUrl())
        attendanceAdapter.setAccounts(accounts)
        binding.apply {
            rvAccount.setHasFixedSize(true)
            rvAccount.adapter = attendanceAdapter
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}