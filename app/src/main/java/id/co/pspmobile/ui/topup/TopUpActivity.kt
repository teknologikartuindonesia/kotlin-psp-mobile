package id.co.pspmobile.ui.topup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.user.VaNumber
import id.co.pspmobile.data.network.user.VaResDto
import id.co.pspmobile.databinding.ActivityTopupBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.getBankShowName
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.snackbar
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.dialog.DialogYesNo
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import id.co.pspmobile.ui.topup.history.HistoryTopUpActivity

@AndroidEntryPoint
class TopUpActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTopupBinding
    private val viewModel: TopUpViewModel by viewModels()
    private lateinit var bankAdapter: BankAdapter
    private lateinit var merchantAdapter: MerchantAdapter
    private var vaResponseDto: VaResDto = VaResDto()
    var scrollViewOnTop = true
    var rvBankOnTop = true
    var vaNumber: VaNumber = VaNumber()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        binding.swipeRefresh.setOnRefreshListener {
            getUserInfo()
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.vaResponse.observe(this) {
            when(it is Resource.Loading){
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                vaResponseDto = it.value
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvBank, it)
            }
        }

        viewModel.createVaResponse.observe(this) {
            when(it is Resource.Loading){
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                viewModel.getVa()
                Toast.makeText(this, resources.getString(R.string.va_has_been_created), Toast.LENGTH_SHORT).show()
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvBank, it)
            }
        }

        viewModel.checkCredentialResponse.observe(this) {
            when(it is Resource.Loading){
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                viewModel.saveUserData(it.value)
                init()
            } else if (it is Resource.Failure) {
                handleApiError(binding.rvBank, it)
            }
        }

        binding.apply {
            rvBank.setHasFixedSize(true)
            rvBank.adapter = bankAdapter
            rvBank.layoutManager?.onRestoreInstanceState(viewModel.recyclerViewState)
        }

        binding.btnHitoryTopup.setOnClickListener {
            startActivity(Intent(this, HistoryTopUpActivity::class.java))
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.rvBank.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                rvBankOnTop = !recyclerView.canScrollVertically(-1)
                binding.swipeRefresh.isEnabled = rvBankOnTop && scrollViewOnTop
            }
        })
        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            scrollViewOnTop = scrollY == 0
            binding.swipeRefresh.isEnabled = rvBankOnTop && scrollViewOnTop
        }
    }



    fun getUserInfo(){
        viewModel.checkCredential()
    }

    fun init(){
        val userData = viewModel.getUserData()
        val banks: List<String> = userData.activeCompany.banks
        val balance = viewModel.getBalanceData().balance

        if (banks.contains("IDN")) {
            val merchants: List<String> = listOf("INDOMARET", "ALFAMART", "GOPAY", "TOKOPEDIA", "SHOPEE", "BLIBLI", "AYOPOP")
            merchantAdapter = MerchantAdapter()
            merchantAdapter.setOnItemClickListener { view ->
                val merchantName = view!!.tag as String
                BottomSheetTopUpMerchant(merchantName).apply {
                    show(supportFragmentManager, tag)
                }
            }
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


        bankAdapter = BankAdapter()
        bankAdapter.setOnItemClickListener { view ->
            val bankName = view!!.tag as String

//            var vaNumber: VaNumber = VaNumber()

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
                val dialogYesNo = DialogYesNo(
                    resources.getString(R.string.title_information),
                    resources.getString(R.string.create_va_message)
                        .replace("bankName", getBankShowName(bankName)),
                    resources.getString(R.string.yes),
                    resources.getString(R.string.no),
                    yesListener = {
                        viewModel.createVa(bankName)
                    },
                    noListener = {

                    }
                )
                dialogYesNo.show(supportFragmentManager, dialogYesNo.tag)
            }
        }
        bankAdapter.setBanks(banks as ArrayList<String>)

        viewModel.getVa()
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