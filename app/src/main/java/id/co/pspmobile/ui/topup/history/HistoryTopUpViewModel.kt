package id.co.pspmobile.ui.topup.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.transaction.HistoryTopUpResDto
import id.co.pspmobile.data.network.transaction.TransactionRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryTopUpViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

//    var recyclerViewState: Parcelable? = null

    private var _historyTopUpResponse: MutableLiveData<Resource<HistoryTopUpResDto>> = MutableLiveData()
    val historyTopUpResponse: LiveData<Resource<HistoryTopUpResDto>> get() = _historyTopUpResponse

    fun getHistoryTopUp(page: Int) = viewModelScope.launch {
        _historyTopUpResponse.value = Resource.Loading
        _historyTopUpResponse.value = transactionRepository.getHistoryTopUp(page, 10)
    }
}