package id.co.pspmobile.ui.topup.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityHistoryTopupBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class HistoryTopUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryTopupBinding
    private val viewModel: HistoryTopUpViewModel by viewModels()
    private lateinit var historyTopUpAdapter: HistoryTopUpAdapter
    private var page: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressbar.visible(false)

        viewModel.historyTopUpResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                historyTopUpAdapter.setHistoryTopUp(it.value.content)
                binding.apply {
                    rvHistoryTopup.setHasFixedSize(true)
                    rvHistoryTopup.adapter = historyTopUpAdapter
                }
            } else if (it is Resource.Failure) {
               handleApiError(binding.rvHistoryTopup, it)
            }
        }

        historyTopUpAdapter = HistoryTopUpAdapter()

        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.getHistoryTopUp(page)
    }

}