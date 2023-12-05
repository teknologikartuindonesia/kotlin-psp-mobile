package id.co.pspmobile.ui.digitalCard.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.FragmentBottomSheetCardDigitalHelpBinding

@AndroidEntryPoint
class BottomSheetCardDigitalHelpFragment() :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCardDigitalHelpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetCardDigitalHelpBinding.inflate(inflater)

        binding.apply {
            tvHelp.text = Html.fromHtml(
                "<b>Saldo Kartu</b> : Total saldo kartu yang masih tersisa dan bisa digunakan." +
                        "<br/><br/>" +
                        "<b>Batas Akumulasi Maksimal</b> : Digunakan untuk membatasi batas maksimal akumulasi dari batas kartu, batas kartu setiap hari akan terakumulasi sampai mencapai batas akumulasi maksimal." +
                        "<br/><br/>" +
                        "<b>Batas Harian</b> : Digunakan untuk membatasi penggunaan saldo kartu dalam satu hari." +
                        "<br/><br/>"
            )

            btnClose.setOnClickListener { dismiss() }
        }

        return binding.root

    }

}