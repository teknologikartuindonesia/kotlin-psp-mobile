package id.co.pspmobile.ui.digitalCard.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.local.SharePreferences
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.digitalCard.DigitalCardDtoItem
import id.co.pspmobile.data.network.responses.digitalCard.CardDataItem
import id.co.pspmobile.data.network.responses.digitalCard.NewDigitalCardData
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCard
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCardItem
import id.co.pspmobile.data.network.user.VaResDto
import id.co.pspmobile.databinding.FragmentBottomSheetSetLimitBinding
import id.co.pspmobile.ui.HomeBottomNavigation.home.BottomSheetOtherMenuViewModel
import id.co.pspmobile.ui.HomeBottomNavigation.profile.ProfileMenuModel
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.digitalCard.DigitalCardViewModel
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import id.co.pspmobile.ui.invoice.fragment.BottomSheetPaymentSuccessInvoice
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import kotlinx.coroutines.runBlocking
import java.util.Date

@AndroidEntryPoint
class BottomSheetSetLimitFragment(item: DigitalCardDtoItem) :
    BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetSetLimitBinding
    private val viewModel: DigitalCardViewModel by viewModels()
    private lateinit var userPreferences: UserPreferences
    private val item = item
    private var vaResponseDto: VaResDto = VaResDto()
    private var syncDigitalCard : SyncDigitalCard= SyncDigitalCard(arrayListOf())
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
                when (it is Resource.Loading) {
                    true -> showLottieLoader()
                    else -> hideLottieLoader()
                }
                if (it is Resource.Success) {
                    dismiss()
                    runBlocking {
                    }
                    Toast.makeText(context, "Atur Limit Berhasil", Toast.LENGTH_SHORT).show()
                    saveLimit(it.value)

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

    fun saveLimit(item: DigitalCardDtoItem) {
       val existing = SharePreferences.getNewSyncDigitalCard(requireContext()) // viewModel.getSyncDataNew()
        if (existing == null){
            val newItem = CardDataItem(item.nfcId, mutableListOf("date"))
            val newSync = NewDigitalCardData(mutableListOf(newItem))
            Log.d("test", "tambah baru $newSync")
            SharePreferences.saveNewSyncDigitalCard(requireContext(), newSync)
        }
        else{
            var isExist = false
            var indexExist = 0
            for (i in existing.dataList) {
                if(i.nfcId == item.nfcId){
                    isExist = true
                    indexExist = existing.dataList.indexOf(i)
                }
            }
            if(isExist) {
                val temp = mutableListOf<CardDataItem>()
                temp.addAll(existing.dataList)
                temp[indexExist].history.add("date")
                Log.d("test", "update $temp")
                SharePreferences.saveNewSyncDigitalCard(requireContext(), NewDigitalCardData(temp))
            }else{
                val newItem = CardDataItem(item.nfcId, mutableListOf("date"))
                val temp = mutableListOf<CardDataItem>()
                temp.addAll(existing.dataList)
                temp.add(newItem)
                Log.d("test", "tambah $temp")
                SharePreferences.saveNewSyncDigitalCard(requireContext(), NewDigitalCardData(temp))
            }
        }

    }

    private fun showLottieLoader() {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(parentFragmentManager, "lottieLoaderDialog")

    }

    private fun hideLottieLoader() {
        val loaderDialogFragment =
            parentFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }
}