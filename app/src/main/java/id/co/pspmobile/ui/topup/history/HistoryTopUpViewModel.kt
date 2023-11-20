package id.co.pspmobile.ui.topup.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.transaction.TransactionPagingSource
import id.co.pspmobile.data.network.transaction.TransactionRepository
import id.co.pspmobile.data.network.transaction.TransactionResDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HistoryTopUpViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

//    var recyclerViewState: Parcelable? = null

    fun getHistoryTopUp(): Flow<PagingData<TransactionResDto>> {
        return Pager(
            PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                initialLoadSize = 10
            )
        ) {
            TransactionPagingSource(transactionRepository)
        }.flow.cachedIn(viewModelScope)
    }
}