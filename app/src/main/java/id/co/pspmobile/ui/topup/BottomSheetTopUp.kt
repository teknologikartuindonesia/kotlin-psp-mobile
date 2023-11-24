package id.co.pspmobile.ui.topup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.user.VaNumber
import id.co.pspmobile.databinding.BottomSheetTopupBinding
import id.co.pspmobile.ui.Utils.copyToClipboard
import id.co.pspmobile.ui.Utils.formatCurrency

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

            when (vaNumber.bankName) {
                "BCA" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_bca) })
                    tvBankName.text = "Bank Central Asia"
                }
                "BANKJABAR" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_bjb) })
                    tvBankName.text = "Bank JABAR"
                }
                "BANKJATIM" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_bjs) })
                    tvBankName.text = "Bank Jatim Syariah"
                }
                "BNI" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_bni) })
                    tvBankName.text = "Bank BNI"
                }
                "BRI" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_bri) })
                    tvBankName.text = "Bank BRI"
                }
                "BSI" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_bsi) })
                    tvBankName.text = "Bank Syariah Indonesia"
                }
                "BTN" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_btn) })
                    tvBankName.text = "Bank BTN"
                }
                "BTPN" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_btpn) })
                    tvBankName.text = "Bank BTPN"
                }
                "CIMB" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_cimb) })
                    tvBankName.text = "Bank CIMB NIAGA"
                }
                "DANAMON" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_danamon) })
                    tvBankName.text = "Bank Danamon"
                }
                "MANDIRI" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_mandiri) })
                    tvBankName.text = "Bank Mandiri"
                }
                "MAYBANK" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_maybank) })
                    tvBankName.text = "Bank Maybank"
                }
                "MUAMALAT" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_muamalat) })
                    tvBankName.text = "Bank Muamalat"
                }
                "NTB SYARIAH" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_ntbs) })
                    tvBankName.text = "Bank NTB Syariah"
                }
                "OCBC" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_ocbc) })
                    tvBankName.text = "Bank OCBC"
                }
                "PERMATA SYARIAH" -> {
                    ivBank.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_permata_syariah) })
                    tvBankName.text = "Bank Permata Syariah"
                }
            }
        }

        return binding.root
    }

    private fun copyToClipboard(vaNumber: String) {
        copyToClipboard(requireContext(), vaNumber, "Account number copied to clipboard")
    }

}