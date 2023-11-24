package id.co.pspmobile.ui.donation

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.donation.DonationDto
import id.co.pspmobile.databinding.ActivityDonationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.donation.detail.DonationDetailActivity
import java.io.Serializable

@AndroidEntryPoint
class DonationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDonationBinding
    private val viewModel: DonationViewModel by viewModels()
    private lateinit var donationAdapter: DonationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            progressbar.visible(false)
        }

        viewModel.donationResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                donationAdapter.setDonations(it.value.content)
                binding.apply {
                    rvDonation.setHasFixedSize(true)
                    rvDonation.adapter = donationAdapter
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvDonation, it)
            }
        }

        donationAdapter = DonationAdapter()
        donationAdapter.setOnItemClickListerner { view ->
            val donationDto = view!!.tag as DonationDto
            val intent = Intent(this, DonationDetailActivity::class.java)
            intent.putExtra("donationDto", donationDto as Serializable)
            startActivity(intent)
        }

        viewModel.getAllDonation(1)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}