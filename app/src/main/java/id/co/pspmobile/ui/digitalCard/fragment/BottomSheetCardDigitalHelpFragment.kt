package id.co.pspmobile.ui.digitalCard.fragment

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.FragmentBottomSheetCardDigitalHelpBinding
import id.co.pspmobile.ui.digitalCard.DigitalCardViewModel

@AndroidEntryPoint
class BottomSheetCardDigitalHelpFragment() :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetCardDigitalHelpBinding
    private val viewModel: DigitalCardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetCardDigitalHelpBinding.inflate(inflater)

        binding.apply {
           if( viewModel.getLanguage() =="en" ){
               tvHelp.text = Html.fromHtml(
                       "<b>Card Balance</b> : The remaining card balance that can still be used." +
                               "<br/><br/>" +
                               "<b>The Maximum Accumulation Limit</b> : Used to limit the maximum accumulation limit of the card, the daily card limit will be accumulated until the maximum accumulation limit is reached." +
                               "<br/><br/>" +
                               "<b>Daily Limit</b> : Utilized to restrict the usage of card balance within a single day." +
                               "<br/><br/>"
                       )
           }else{
               tvHelp.text = Html.fromHtml(
                       "<b>Saldo Kartu</b> : Total saldo kartu yang masih tersisa dan bisa digunakan." +
                               "<br/><br/>" +
                               "<b>Batas Akumulasi Maksimal</b> : Digunakan untuk membatasi batas maksimal akumulasi dari batas kartu, batas kartu setiap hari akan terakumulasi sampai mencapai batas akumulasi maksimal." +
                               "<br/><br/>" +
                               "<b>Batas Harian</b> : Digunakan untuk membatasi penggunaan saldo kartu dalam satu hari." +
                               "<br/><br/>"
                       )
           }


            btnClose.setOnClickListener { dismiss() }
        }

        return binding.root

    }

}