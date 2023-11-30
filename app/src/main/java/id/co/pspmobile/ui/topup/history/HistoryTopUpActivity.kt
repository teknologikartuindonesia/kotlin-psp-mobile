package id.co.pspmobile.ui.topup.history

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityHistoryTopupBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.invoice.fragment.HistoryAdapter
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment

@AndroidEntryPoint
class HistoryTopUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryTopupBinding
    private val viewModel: HistoryTopUpViewModel by viewModels()
    private lateinit var historyTopUpAdapter: HistoryTopUpAdapter
    private var page: Int = 0
    private var size: Int = 10
    private var totalContent: Int = 0
    private var isLoading = false

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)

        binding.progressbar.visible(false)

        binding.rvHistoryTopup.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = historyTopUpAdapter.itemCount

                if (!isLoading && totalContent >= size) {
                    if (visibleItemCount + pastVisibleItem >= total) {
                        isLoading = true
                        viewModel.getHistoryTopUp(page++)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        viewModel.historyTopUpResponse.observe(this) {
            when(it is Resource.Loading){
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                historyTopUpAdapter.setHistoryTopUp(it.value.content)
                totalContent = it.value.content.size
                isLoading = false
            } else if (it is Resource.Failure) {
               handleApiError(binding.rvHistoryTopup, it)
            }
        }

        historyTopUpAdapter = HistoryTopUpAdapter()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            historyTopUpAdapter.clear()
            totalContent = 0
            viewModel.getHistoryTopUp(0)
            binding.swipeRefreshLayout.isRefreshing = false
        }
        setupRecyclerView()
        viewModel.getHistoryTopUp(page)
    }

    private fun setupRecyclerView() {
        binding.rvHistoryTopup.setHasFixedSize(true)
        binding.rvHistoryTopup.layoutManager = layoutManager
        historyTopUpAdapter = HistoryTopUpAdapter()
        binding.rvHistoryTopup.adapter = historyTopUpAdapter
    }

    private fun showLottieLoader() {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(supportFragmentManager, "lottieLoaderDialog")

    }
    private fun hideLottieLoader() {
        val loaderDialogFragment =
            supportFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }


}