package id.co.pspmobile.ui.topup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.BottomSheetTopupMerchantBinding
import id.co.pspmobile.ui.NumberTextWatcher
import id.co.pspmobile.ui.Utils.getMerchantIcon
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.topup.tutorial.TutorialMerchantActivity

@AndroidEntryPoint
class BottomSheetTopUpMerchant(
    private val merchantName : String
) : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetTopupMerchantBinding
    private val viewModel: TopUpViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetTopupMerchantBinding.inflate(inflater)

        binding.apply {
            etAmount.setText("0")
            ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), getMerchantIcon(merchantName)) })

            viewModel.topUpIdnResponse.observe(viewLifecycleOwner) {
                binding.progressbar.visible(it is Resource.Loading)
                if (it is Resource.Success) {
                    val i = Intent(requireContext(), TutorialMerchantActivity::class.java)
                    i.putExtra("bankName", merchantName)
                    i.putExtra("companyName", it.value.companyName)
                    i.putExtra("accountName", it.value.accountName)
                    i.putExtra("accountNumber", it.value.accountNumber)
                    i.putExtra("cid", it.value.billReq?.branch_code)
                    i.putExtra("amount", etAmount.text.toString().trim())
                    requireContext().startActivity(i)
                } else if (it is Resource.Failure) {
                    requireActivity().handleApiError(btnProcess, it)
                }
            }

            btnProcess.setOnClickListener {
                if (etAmount.text.toString().trim().isEmpty()) {
                    tvMessage.visible(true)
                    tvMessage.text = resources.getString(R.string.amount_empty)
                } else if (etAmount.text.toString()
                        .replace(",","")
                        .replace(".","").trim().toDouble() < 10000) {
                    tvMessage.visible(true)
                    tvMessage.text = resources.getString(R.string.amount_minimum)
                } else {
                    tvMessage.visible(false)
                    viewModel.topUpIdn(
                        etAmount.text.toString()
                            .replace(",","")
                            .replace(".","")
                            .trim().toDouble()
                    )
                }
            }
        }

        binding.etAmount.addTextChangedListener(NumberTextWatcher(binding.etAmount))
        return binding.root
    }

}