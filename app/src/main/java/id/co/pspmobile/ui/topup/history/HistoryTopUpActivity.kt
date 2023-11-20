package id.co.pspmobile.ui.topup.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.co.pspmobile.databinding.ActivityHistoryTopupBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistoryTopUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryTopupBinding
    private val viewModel: HistoryTopUpViewModel by viewModels()
    private lateinit var historyTopUpAdapter: HistoryTopUpAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            getHistoryTopUp()
        }
        binding.swipeRefreshLayout.setOnRefreshListener(refreshListener)

        historyTopUpAdapter = HistoryTopUpAdapter()
        binding.rvHistoryTopup.adapter = historyTopUpAdapter.withLoadStateFooter(
            LoadStateAdapter()
        )

        binding.btnBack.setOnClickListener {
            finish()
        }

        getHistoryTopUp()
    }

    private fun getHistoryTopUp() {
        lifecycleScope.launch {
            viewModel.getHistoryTopUp().collectLatest {
                historyTopUpAdapter.submitData(it)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}