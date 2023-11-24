package id.co.pspmobile.ui.donation.detail

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.donation.DonationDto
import id.co.pspmobile.databinding.ActivityDonationDetailBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class DonationDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDonationDetailBinding
    private val viewModel: DonationDetailViewModel by viewModels()
    private lateinit var donationHistoryAdapter: DonationHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val donationDto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("donationDto", DonationDto::class.java)
        } else {
            intent.getSerializableExtra("donationDto") as DonationDto
        }
        val balance = viewModel.getBalanceData().balance

        binding.apply {
            progressbar.visible(false)
            tvBalance.text = Utils.formatCurrency(balance)
            tvDonationName.text = donationDto!!.title
        }

        viewModel.donationResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                donationHistoryAdapter.setParticipants(it.value.participants)
                binding.apply {
                    rvDonationHistory.setHasFixedSize(true)
                    rvDonationHistory.adapter = donationHistoryAdapter
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvDonationHistory, it)
            }
        }

        donationHistoryAdapter = DonationHistoryAdapter()

        viewModel.getDonationById(donationDto!!.id)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}