package id.co.pspmobile.ui.topup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.user.VaNumber
import id.co.pspmobile.databinding.BottomSheetTopupBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.copyToClipboard
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.topup.tutorial.TutorialTopupActivity

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
            tvAdminBank.text = "Rp. " + formatCurrency(vaNumber.adminBank)
            tvAccountNumber.text = vaNumber.number
            tvAccountNumber.setOnClickListener {
                copyToClipboard(vaNumber.number.toString())
            }
            ivCopyVa.setOnClickListener {
                copyToClipboard(vaNumber.number.toString())
            }
            tvBankName.text = Utils.getBankShowName(vaNumber?.bankName ?: "")
            ivBank.setImageDrawable(getDrawable(requireContext(), Utils.getBankIcon(vaNumber?.bankName ?: "")))

        }

        binding.btnTutorial.setOnClickListener {
            val i = Intent(requireContext(), TutorialTopupActivity::class.java)
            i.putExtra("accountName", accountName)
            i.putExtra("vaNumber", Gson().toJson(vaNumber))
            startActivity(i)
        }

        return binding.root
    }

    private fun copyToClipboard(vaNumber: String) {
        copyToClipboard(requireContext(), vaNumber, "Account number copied to clipboard")
    }

}