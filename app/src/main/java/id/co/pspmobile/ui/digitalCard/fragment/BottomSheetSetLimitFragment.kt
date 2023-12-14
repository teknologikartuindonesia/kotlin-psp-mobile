package id.co.pspmobile.ui.digitalCard.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.local.SharePreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.ModelDigitalCard
import id.co.pspmobile.data.network.responses.digitalCard.CardDataItem
import id.co.pspmobile.data.network.responses.digitalCard.NewDigitalCardData
import id.co.pspmobile.databinding.FragmentBottomSheetSetLimitBinding
import id.co.pspmobile.ui.HomeActivity
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.digitalCard.DigitalCardActivity
import id.co.pspmobile.ui.digitalCard.DigitalCardViewModel
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BottomSheetSetLimitFragment(
    modelDigitalCard: ModelDigitalCard,
    val setLimitCallback: (modelDigitalCard: ModelDigitalCard) -> (Unit)
) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentBottomSheetSetLimitBinding
    private val viewModel: DigitalCardViewModel by viewModels()
    private val modelDigitalCard = modelDigitalCard
    private val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetSetLimitBinding.inflate(inflater)

        formatter.applyPattern("#,###,###,###")
        val args = arguments
        val nfcId = args?.getString("nfcId")

        val userData = viewModel.getUserData()

        binding.apply {
            limitDaily.setText(formatter.format(modelDigitalCard.limitDaily.toString().toDouble()))
            limitDaily.addTextChangedListener(onTextChangedListener(limitDaily))

            limitMax.setText(formatter.format(modelDigitalCard.limitMax.toString().toDouble()))
            limitMax.addTextChangedListener(onTextChangedListener(limitMax))

            Log.e("test", "unlimited ${userData.user.accounts[0].transactionUnlimited}")
            switchUnlimitedTransaction.isChecked = userData.user.accounts[0].transactionUnlimited

            viewModel.updateDigitalCardResponse.observe(viewLifecycleOwner) {
//                when (it is Resource.Loading) {
//                    true -> showLottieLoader()
//                    else -> hideLottieLoader()
//                }
                if (it is Resource.Success) {
                    Toast.makeText(context, "Atur Limit Berhasil", Toast.LENGTH_SHORT).show()
                    saveLimit(modelDigitalCard)
                    setLimitCallback(modelDigitalCard)

                    dismiss()
                } else if (it is Resource.Failure) {
                }
            }

            viewModel.updateAccount.observe(viewLifecycleOwner) {
//                when (it is Resource.Loading) {
//                    true -> showLottieLoader()
//                    else -> hideLottieLoader()
//                }
                if (it is Resource.Success) {
                    Toast.makeText(context, "Atur Limit Berhasil", Toast.LENGTH_SHORT).show()
                    viewModel.saveUserData(userData)
                    switchUnlimitedTransaction.isChecked =
                        userData.user.accounts[0].transactionUnlimited
                    dismiss()
                } else if (it is Resource.Failure) {
                }
            }

            btnSave.setOnClickListener {
                var isDifference = false
                if (modelDigitalCard.limitDaily != limitDaily.text.toString().replace(",", "")
                        .toDouble() ||
                    modelDigitalCard.limitMax != limitMax.text.toString().replace(",", "")
                        .toDouble()
                ) {
                    isDifference = true
                    modelDigitalCard.limitDaily =
                        limitDaily.text.toString().replace(",", "").toDouble()
                    modelDigitalCard.limitMax =
                        limitMax.text.toString().replace(",", "").toDouble()
                    viewModel.updateDigitalCard(modelDigitalCard)
                }
                if (switchUnlimitedTransaction.isChecked != userData.user.accounts[0].transactionUnlimited) {

                    try {
                        isDifference = true
                        userData.user.accounts[0].transactionUnlimited =
                            switchUnlimitedTransaction.isChecked
                        if (userData.user.socmedAccounts == null) userData.user.socmedAccounts =
                            mutableListOf()
                        if (userData.user.address == null) userData.user.address = ""

                        Log.e(
                            "test",
                            "unlimited2 ${userData.user.accounts[0].transactionUnlimited}"
                        )

                        viewModel.updateAccount(userData.user, modelDigitalCard)
                        (activity as DigitalCardActivity?)?.refreshLastSync()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                }

                if (!isDifference) {
                    dismiss()
                }
            }

            btnCancel.setOnClickListener { dismiss() }
        }

        return binding.root

    }

    private fun saveLimit(item: ModelDigitalCard) {

        val today = Calendar.getInstance()
        val sendDateUAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(today.time)
        val dateNow = Utils.formatDateTime(sendDateUAT, "dd-MM-yyyy HH:mm")
        val existing =
            SharePreferences.getNewSyncDigitalCard(requireContext()) // viewModel.getSyncDataNew()
        if (existing == null) {
            val newItem = CardDataItem(item.nfcId, mutableListOf(dateNow))
            val newSync = NewDigitalCardData(mutableListOf(newItem))
            Log.d("test", "tambah baru $newSync")
            SharePreferences.saveNewSyncDigitalCard(requireContext(), newSync)
        } else {
            var isExist = false
            var indexExist = 0
            for (i in existing.dataList) {
                if (i.nfcId == item.nfcId) {
                    isExist = true
                    indexExist = existing.dataList.indexOf(i)
                }
            }

            if (isExist) {

                val temp = mutableListOf<CardDataItem>()
                temp.addAll(existing.dataList)
                temp[indexExist].history.add(dateNow)
                Log.d("test", "update $temp")
                SharePreferences.saveNewSyncDigitalCard(requireContext(), NewDigitalCardData(temp))
            } else {
                val newItem = CardDataItem(
                    item.nfcId,
                    mutableListOf(dateNow)
                )
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

    private fun onTextChangedListener(editText: EditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                editText.removeTextChangedListener(this)
                try {
                    var originalString = s.toString()
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    val longVal: Long = originalString.toLong()
                    val formattedString: String = formatter.format(longVal)

                    //setting text after format to EditText
                    editText.setText(formattedString)
                    editText.setSelection(editText.text.length)
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }
                editText.addTextChangedListener(this)
            }
        }
    }
}