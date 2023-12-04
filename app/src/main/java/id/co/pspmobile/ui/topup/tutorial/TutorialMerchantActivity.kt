package id.co.pspmobile.ui.topup.tutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityTutorialMerchantBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class TutorialMerchantActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTutorialMerchantBinding
    private val viewModel: TutorialTopupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bankName = intent.getStringExtra("bankName") ?: ""
        val companyName = intent.getStringExtra("companyName") ?: ""
        val accountName = intent.getStringExtra("accountName") ?: ""
        val accountNumber = intent.getStringExtra("accountNumber") ?: ""
        val cid = intent.getStringExtra("cid") ?: ""
        val amount = intent.getStringExtra("amount") ?: ""

        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.ivBank.setImageDrawable(getDrawable(Utils.getMerchantIcon(bankName)))
        binding.tvCompanyName.text = companyName
        binding.tvAccountName.text = accountName
        binding.tvAccountNumber.text = accountNumber
        binding.tvAmount.text = "Rp. $amount"

        binding.tvAccountNumber.setOnClickListener {
            copyToClipboard(accountNumber)
        }
        binding.ivCopyVa.setOnClickListener {
            copyToClipboard(accountNumber)
        }
        viewModel.tutorialResponse.observe(this) {
            if (it is Resource.Success) {
                val tutorialResDto = it.value
                var msg = ""
                for (i in tutorialResDto){
                    msg += i.tutorial
                }
                binding.txtTutorial.text = Html.fromHtml(msg)
                if (msg == "") {
                    binding.txtTutorial.visible(false)
                }
            } else if (it is Resource.Failure) {
                binding.txtTutorial.visible(false)
                Toast.makeText(this, it.errorBody?.string() ?: "", Toast.LENGTH_LONG).show()
            }
        }
        getTutorial(bankName, viewModel.getLanguage(), cid, "", accountNumber)
    }

    fun getTutorial(bank: String, lang: String, cid: String, kode: String, va: String) {
        viewModel.getTutorial(bank, lang, cid, kode, va)
    }

    private fun copyToClipboard(vaNumber: String) {
        Utils.copyToClipboard(this, vaNumber, "Account number copied to clipboard")
    }
}