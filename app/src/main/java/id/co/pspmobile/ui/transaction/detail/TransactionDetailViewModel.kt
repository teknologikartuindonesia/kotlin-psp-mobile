package id.co.pspmobile.ui.transaction.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.report.ReportRepository
import id.co.pspmobile.data.network.report.TransactionDetailResDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var _transactionDetailResponse: MutableLiveData<Resource<TransactionDetailResDto>> = MutableLiveData()
    val transactionDetailResponse: LiveData<Resource<TransactionDetailResDto>> get() = _transactionDetailResponse

    fun getTransactionDetail(
        isOldTransaction: Boolean,
        month: String,
        year: Int,
        transactionName: String
    ) = viewModelScope.launch {
        var strMonth = month.toString()
        if (strMonth.length == 1) {
            strMonth = "0$strMonth"
        }
        _transactionDetailResponse.value = Resource.Loading
        if (isOldTransaction) {
            _transactionDetailResponse.value = reportRepository.getOldTransactionDetail(strMonth, year, transactionName)
        } else {
            _transactionDetailResponse.value = reportRepository.getTransactionDetail(strMonth, year, transactionName)
        }
    }

    fun getUserData() = userPreferences.getUserData()

}