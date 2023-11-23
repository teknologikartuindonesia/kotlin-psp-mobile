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
import id.co.pspmobile.ui.digitalCard.DigitalCardViewModel

@AndroidEntryPoint
class BottomSheetSetLimitFragment(item: DigitalCardDtoItem) :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetSetLimitBinding
    private lateinit var viewModel: DigitalCardViewModel
    private val item = item
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetSetLimitBinding.inflate(inflater)

        binding.apply {

            limitDaily.setText(item.limitDaily.toString())
            limitMax.setText(item.limitMax.toString())
            btnSave.setOnClickListener {
                update()
            }
            btnCancel.setOnClickListener { dismiss() }
        }

        return binding.root

    }
    fun update() {
        viewModel.updateDigitalCard(
            item.id,
            item.accountId,
            item.active,
            item.amount,
            item.balance,
            item.callerId,
            item.callerName,
            item.cardBalance,
            item.ceiling,
            item.companyId,
            item.deviceBalance,
            item.id,
            binding.limitDaily.text.toString().toDouble(),
            binding.limitMax.text.toString().toDouble(),
            item.name,
            item.nfcId,
            item.photoUrl,
            item.usePin
        )
    }

}