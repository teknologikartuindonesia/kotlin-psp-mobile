package id.co.pspmobile.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.report.ReportRepository
import id.co.pspmobile.data.network.report.TransactionResDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private var _transactionResponse: MutableLiveData<Resource<TransactionResDto>> = MutableLiveData()
    val transactionResponse: LiveData<Resource<TransactionResDto>> get() = _transactionResponse

    private var _oldTransactionResponse: MutableLiveData<Resource<TransactionResDto>> = MutableLiveData()
    val oldTransactionResponse: LiveData<Resource<TransactionResDto>> get() = _oldTransactionResponse

    fun getTransaction(month: Int, year: Int) = viewModelScope.launch {
        var strMonth = month.toString()
        if (strMonth.length == 1) {
            strMonth = "0$strMonth"
        }
        _transactionResponse.value = Resource.Loading
        _transactionResponse.value = reportRepository.getTransaction(strMonth, year)
    }

    fun getOldTransaction(month: Int, year: Int) = viewModelScope.launch {
        var strMonth = month.toString()
        if (strMonth.length == 1) {
            strMonth = "0$strMonth"
        }
        _oldTransactionResponse.value = Resource.Loading
        _oldTransactionResponse.value = reportRepository.getOldTransaction(strMonth, year)
    }

}