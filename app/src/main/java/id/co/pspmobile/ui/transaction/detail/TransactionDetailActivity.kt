package id.co.pspmobile.ui.transaction.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityTransactionDetailBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class TransactionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionDetailBinding
    private val viewModel: TransactionDetailViewModel by viewModels()
    private lateinit var transactionDetailAdapter: TransactionDetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transactionName = intent.getStringExtra("transactionName")
        val month = intent.getIntExtra("month", 1)
        val year = intent.getIntExtra("year", 2023)
        val isOldTransaction = intent.getBooleanExtra("isOldTransaction", false)

        binding.progressbar.visible(false)

        viewModel.transactionDetailResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                transactionDetailAdapter.setTransactions(it.value.content)
                binding.apply {
                    rvTransaction.setHasFixedSize(true)
                    rvTransaction.adapter = transactionDetailAdapter
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvTransaction, it)
            }
        }

        transactionDetailAdapter = TransactionDetailAdapter(viewModel)

        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.getTransactionDetail(isOldTransaction, month.toString(), year, transactionName!!)
    }
}