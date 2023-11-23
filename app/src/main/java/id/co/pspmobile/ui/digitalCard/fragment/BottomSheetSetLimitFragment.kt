package id.co.pspmobile.ui.digitalCard.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.digitalCard.DigitalCardDtoItem
import id.co.pspmobile.databinding.FragmentBottomSheetSetLimitBinding
import id.co.pspmobile.ui.HomeBottomNavigation.home.BottomSheetOtherMenuViewModel

@AndroidEntryPoint
class BottomSheetSetLimitFragment(item: DigitalCardDtoItem) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetSetLimitBinding
    private lateinit var viewModel: BottomSheetOtherMenuViewModel
    private val item = item
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetSetLimitBinding.inflate(inflater)

        binding.limitDaily.setText(item.limitDaily.toString())
        binding.limitMax.setText(item.limitMax.toString())
        return binding.root
    }


}