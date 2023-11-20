package id.co.pspmobile.ui.main

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.auth.InvoicesRepository
import id.co.pspmobile.data.network.responses.InvoiceResponse
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.LoginResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val invoicesRepository: InvoicesRepository,
    private val remoteDataSource: RemoteDataSource,
) : ViewModel() {
    var recyclerViewState: Parcelable? = null

    private val _invoice: MutableLiveData<Resource<List<InvoiceResponse>>> = MutableLiveData()
    val invoice: LiveData<Resource<List<InvoiceResponse>>> get() = _invoice
    fun getDataInvoice() = viewModelScope.launch {
        _invoice.value = Resource.Loading
        _invoice.value = invoicesRepository.getDataInvoice()
    }
    fun getBaseUrl() : String {
        return remoteDataSource.baseURL
    }

}