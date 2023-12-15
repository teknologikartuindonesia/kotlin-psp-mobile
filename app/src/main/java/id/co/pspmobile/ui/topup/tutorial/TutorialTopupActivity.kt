package id.co.pspmobile.ui.topup.tutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.user.VaNumber
import id.co.pspmobile.databinding.ActivityTutorialTopupBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.getBankIcon
import id.co.pspmobile.ui.Utils.getBankShowName
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class TutorialTopupActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTutorialTopupBinding
    private val viewModel: TutorialTopupViewModel by viewModels()
    private var vaNumber: VaNumber? = VaNumber()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val fromExtra = intent.extras?.getString("vaNumber")
        val accountName = intent.extras?.getString("accountName")
        val isIdn = intent.extras?.getBoolean("isIdn") ?: false
        if (fromExtra != null) {
            vaNumber = Gson().fromJson(fromExtra, VaNumber::class.java)
            binding.tvAccountName.text = accountName
            binding.tvBankName.text = vaNumber?.bankName
            binding.tvAdminBank.text = "Rp. " + Utils.formatCurrency(vaNumber?.adminBank ?: 0.0)
            binding.tvAccountNumber.text = vaNumber?.number
            binding.tvAccountNumber.setOnClickListener {
                copyToClipboard(vaNumber?.number.toString())
            }
            binding.ivCopyVa.setOnClickListener {
                copyToClipboard(vaNumber?.number.toString())
            }
            binding.tvBankName.text = getBankShowName(vaNumber?.bankName ?: "")
            binding.ivBank.setImageDrawable(getDrawable(getBankIcon(vaNumber?.bankName ?: "")))

            getTutorial(vaNumber?.bankName ?: "", viewModel.getLanguage(), "", "", vaNumber?.number.toString())
        }

        viewModel.tutorialResponse.observe(this) {
            if (it is Resource.Success) {
                val tutorialResDto = it.value
                var msg = ""
                for (i in tutorialResDto){
                    msg += i.tutorial
                }
                binding.wvTutorial.settings.javaScriptEnabled = true
                binding.wvTutorial.settings.domStorageEnabled = true
                binding.wvTutorial.isVerticalScrollBarEnabled = false
                binding.wvTutorial.isHorizontalScrollBarEnabled = false
                binding.wvTutorial.loadDataWithBaseURL(null, msg, "text/html", "UTF-8", null)
                if (msg == "") {
                    binding.wvTutorial.visible(false)
                }
            } else if (it is Resource.Failure) {
                binding.wvTutorial.visible(false)
                Toast.makeText(this, it.errorBody?.string() ?: "", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    fun getTutorial(bank: String, lang: String, cid: String, kode: String, va: String) {
        viewModel.getTutorial(bank, lang, cid, kode, va)
    }

    fun s(){
//        val lang = viewModel.getLanguage()
//        try {
//            val cid = vaNumber.number?.substring(3, 4) ?: ""
//            val idPelanggan = vaNumber.number?.substring(7, vaNumber.number!!.length) ?: ""
//            val lang = viewModel.getLanguage()
//            Toast.makeText(this, "cid: $cid, idPelanggan: $idPelanggan", Toast.LENGTH_LONG).show()
//            viewModel.getTutorial(bankName, lang, cid, idPelanggan, vaNumber.number.toString())
//        } catch (e: Exception) {
//            e.printStackTrace()
//            viewModel.getTutorial(bankName, lang, "", "", vaNumber.number.toString())
//            Toast.makeText(this, bankName, Toast.LENGTH_LONG).show()
//        }
    }

    private fun copyToClipboard(vaNumber: String) {
        Utils.copyToClipboard(this, vaNumber, "Account number copied to clipboard")
    }

}