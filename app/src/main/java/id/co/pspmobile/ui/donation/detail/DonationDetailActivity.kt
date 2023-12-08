package id.co.pspmobile.ui.donation.detail

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.donation.DonationDto
import id.co.pspmobile.data.network.donation.DonationPayDto
import id.co.pspmobile.databinding.ActivityDonationDetailBinding
import id.co.pspmobile.ui.NumberTextWatcher
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.snackbar
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment

@AndroidEntryPoint
class DonationDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDonationDetailBinding
    private val viewModel: DonationDetailViewModel by viewModels()
    private lateinit var donationHistoryAdapter: DonationHistoryAdapter
    private var lang = "id"
    private var amount = ""

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
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                donationHistoryAdapter.setParticipants(it.value.participants.reversed())
                binding.apply {
                    rvDonationHistory.setHasFixedSize(true)
                    rvDonationHistory.adapter = donationHistoryAdapter
                }
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvDonationHistory, it)
            }
        }

        viewModel.donateResponse.observe(this) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                val msg =
                    if (lang == "id") "Donasi ${donationDto?.title} ($amount) Berhasil"
                    else "Donation ${donationDto?.title} ($amount) Success"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                finish()
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvDonationHistory, it)
            }
        }

        donationHistoryAdapter = DonationHistoryAdapter()

        viewModel.getDonationById(donationDto!!.id)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.etAmount.addTextChangedListener(NumberTextWatcher(binding.etAmount))

        binding.btnDonation.setOnClickListener {
            this.amount = "Rp. ${binding.etAmount.text.toString().trim()}"
            val amount = binding.etAmount.text.toString().trim().replace(".", "").replace(",", "")
            val intAmount = amount.toIntOrNull()
            if (amount.isEmpty()){
                binding.root.snackbar(resources.getString(R.string.donation_is_empty))
            } else if(intAmount != null && intAmount < 10){
                binding.root.snackbar(resources.getString(R.string.donation_minimum))
            } else if(intAmount != null && intAmount>balance){
                binding.root.snackbar(resources.getString(R.string.insufficient_balance))
            }else{
                val accountId = viewModel.getUserData().user.accounts[0].id
                val body = DonationPayDto(
                    accountId,"",
                    amount.toInt()?:0,true,
                    "PSP Mobile", donationDto.id)
                viewModel.donate(body)
            }
        }

        viewModel.balanceResponse.observe(this){
            if (it is Resource.Success) {
                binding.tvBalance.text = Utils.formatCurrency(it.value.balance)
                viewModel.saveBalanceData(it.value)
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvDonationHistory, it)
            }
        }
        getBalance()
    }

    fun getBalance(){
        viewModel.getBalance()
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