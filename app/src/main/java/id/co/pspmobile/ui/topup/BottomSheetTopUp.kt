package id.co.pspmobile.ui.topup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.user.VaNumber
import id.co.pspmobile.databinding.BottomSheetTopupBinding
import id.co.pspmobile.ui.Utils

@AndroidEntryPoint
class BottomSheetTopUp(
    private val accountName : String,
    private val vaNumber : VaNumber
) : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetTopupBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetTopupBinding.inflate(inflater)

        binding.apply {
            tvAccountName.text = accountName
            tvBankName.text = vaNumber.bankName
            tvAdminBank.text = "Rp. " + Utils.formatCurrency(vaNumber.adminBank)
            tvAccountNumber.text = vaNumber.number
            tvAccountNumber.setOnClickListener {
                Utils.copyToClipboard(requireContext(), vaNumber.number.toString(), "Account number copied to clipboard")
            }
        }

        return binding.root
    }

}