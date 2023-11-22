package id.co.pspmobile.ui.topup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.user.VaNumber
import id.co.pspmobile.data.network.user.VaResDto
import id.co.pspmobile.databinding.ActivityTopupBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.topup.history.HistoryTopUpActivity

@AndroidEntryPoint
class TopUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTopupBinding
    private val viewModel: TopUpViewModel by viewModels()
    private lateinit var bankAdapter: BankAdapter
    private lateinit var merchantAdapter: MerchantAdapter
    private var vaResponseDto: VaResDto = VaResDto()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userData = viewModel.getUserData()
        val banks: List<String> = userData.activeCompany.banks
        val balance = viewModel.getBalanceData().balance

        if (banks.contains("IDN")) {
            val merchants: List<String> = listOf("INDOMARET", "ALFAMART", "GOPAY", "TOKOPEDIA", "SHOPEE", "BLIBLI", "AYOPOP")
            merchantAdapter = MerchantAdapter()
            merchantAdapter.setMerchants(merchants)
            binding.apply {
                rvMerchant.setHasFixedSize(true)
                rvMerchant.adapter = merchantAdapter
                rvMerchant.layoutManager?.onRestoreInstanceState(viewModel.recyclerViewState)
            }
        } else {
            binding.tvOtherMethod.visible(false)
            binding.rvMerchant.visible(false)
        }
        binding.apply {
            progressbar.visible(false)
            tvBalance.text = Utils.formatCurrency(balance)
        }
        viewModel.vaResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                vaResponseDto = it.value
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvBank, it)
            }
        }

        viewModel.createVaResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                viewModel.getVa()
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvBank, it)
            }
        }

        bankAdapter = BankAdapter()
        bankAdapter.setOnItemClickListerner { view ->
            val bankName = view!!.tag as String

            var vaNumber: VaNumber = VaNumber()
            var isExist = false

            for (objVaNumber in vaResponseDto.vaNumbers!!) if (objVaNumber != null) {
                if (objVaNumber.bankName == bankName) {
                    vaNumber = objVaNumber
                    isExist = true
                    break
                }
            }

            if (isExist) {
                BottomSheetTopUp(vaResponseDto.name.toString(), vaNumber).apply {
                    show(supportFragmentManager, tag)
                }
            } else {
                AlertDialog.Builder(this)
                    .setMessage("You don't have a Virtual Account at " + bankName + " yet. Do you want to make one ?")
                    .setCancelable(false)
                    .setPositiveButton("Create Virtual Account") { _, _ ->
                        viewModel.createVa(bankName)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        bankAdapter.setBanks(banks as ArrayList<String>)
        binding.apply {
            rvBank.setHasFixedSize(true)
            rvBank.adapter = bankAdapter
            rvBank.layoutManager?.onRestoreInstanceState(viewModel.recyclerViewState)
        }
        viewModel.getVa()

        binding.btnHitoryTopup.setOnClickListener {
            startActivity(Intent(this, HistoryTopUpActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }



}