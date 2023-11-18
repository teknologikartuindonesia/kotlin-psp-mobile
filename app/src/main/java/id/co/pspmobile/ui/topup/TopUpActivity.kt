package id.co.pspmobile.ui.topup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.user.VaNumber
import id.co.pspmobile.data.network.user.VaResDto
import id.co.pspmobile.databinding.ActivityTopupBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class TopUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTopupBinding
    private val viewModel: TopUpViewModel by viewModels()
    private lateinit var bankAdapter: BankAdapter
    private var vaResponseDto: VaResDto = VaResDto()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val banks: List<String> = intent.getSerializableExtra("banks") as List<String>

        binding.progressbar.visible(false)

        viewModel.vaResponse.observe(this) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                vaResponseDto = it.value


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
//                        viewModel.deleteInfo(info.id)
//                        activity.showNotification("Data info telah berhasil dihapus")
//                        activity.searchInfo()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        bankAdapter.setBanks(banks)
        binding.apply {
            rvBank.setHasFixedSize(true)
            rvBank.adapter = bankAdapter
            rvBank.layoutManager?.onRestoreInstanceState(viewModel.recyclerViewState)
        }
        viewModel.getVa()

        binding.btnBack.setOnClickListener {
            finish()
        }
    }



}