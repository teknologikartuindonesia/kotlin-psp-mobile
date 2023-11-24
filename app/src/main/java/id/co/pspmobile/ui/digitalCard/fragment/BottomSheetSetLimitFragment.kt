package id.co.pspmobile.ui.digitalCard.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.digitalCard.DigitalCardDtoItem
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCard
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCardItem
import id.co.pspmobile.databinding.FragmentBottomSheetSetLimitBinding
import id.co.pspmobile.ui.HomeBottomNavigation.home.BottomSheetOtherMenuViewModel
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.digitalCard.DigitalCardViewModel
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import id.co.pspmobile.ui.invoice.fragment.BottomSheetPaymentSuccessInvoice
import kotlinx.coroutines.runBlocking
import java.util.Date

@AndroidEntryPoint
class BottomSheetSetLimitFragment(item: DigitalCardDtoItem) :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetSetLimitBinding
    private val viewModel: DigitalCardViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences
    private lateinit var syncDigitalCard: SyncDigitalCard
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
            viewModel.updateDigitalCardResponse.observe(viewLifecycleOwner) {
                binding.progressbar.visible(it is Resource.Loading)
                if (it is Resource.Success) {
                    dismiss()
                    val newItem = SyncDigitalCardItem(Date(), "John Doe", "12345", "abc123")

                    runBlocking {
                        viewModel.saveSyncDigitalCard(newItem)
                    }
                    Toast.makeText(context, "Atur Limit Berhasil", Toast.LENGTH_SHORT).show()
                } else if (it is Resource.Failure) {
                }
            }
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