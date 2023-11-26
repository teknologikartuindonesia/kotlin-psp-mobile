package id.co.pspmobile.ui.topup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.BottomSheetTopupMerchantBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible

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
            when (merchantName) {
                "INDOMARET" -> {
                    ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_indomaret) })
                }
                "ALFAMART" -> {
                    ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_alfamart) })
                }
                "GOPAY" -> {
                    ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_gopay) })
                }
                "TOKOPEDIA" -> {
                    ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_tokopedia) })
                }
                "SHOPEE" -> {
                    ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_shopee) })
                }
                "BLIBLI" -> {
                    ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_blibli) })
                }
                "AYOPOP" -> {
                    ivMerchant.setImageDrawable(context.let { ActivityCompat.getDrawable(requireContext(), R.drawable.logo_ayopop) })
                }
            }

            viewModel.topUpIdnResponse.observe(viewLifecycleOwner) {
                binding.progressbar.visible(it is Resource.Loading)
                if (it is Resource.Success) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Silahkan melakukan top up berdasarkan panduan yang ada.")
                        .setCancelable(false)
                        .setPositiveButton("OK") { _, _ ->
                            dismiss()
                        }
                        .create()
                        .show()
                } else if (it is Resource.Failure) {
                    requireActivity().handleApiError(btnProcess, it)
                }
            }

            btnProcess.setOnClickListener {
                if (etAmount.text.toString().trim().isEmpty()) {
                    tvMessage.visible(true)
                    tvMessage.text = "Nominal top up belum diisi"
                } else if (etAmount.text.toString().trim().toDouble() < 10000) {
                    tvMessage.visible(true)
                    tvMessage.text = "Nominal top up minimal Rp 10.000"
                } else {
                    tvMessage.visible(false)
                    viewModel.topUpIdn(etAmount.text.toString().trim().toDouble())
                }
            }
        }

        return binding.root
    }

}