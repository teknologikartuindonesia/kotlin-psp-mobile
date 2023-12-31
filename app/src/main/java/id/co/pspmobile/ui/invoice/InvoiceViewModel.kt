package id.co.pspmobile.ui.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.invoice.InvoicePaymentDto
import id.co.pspmobile.data.network.invoice.InvoiceRepository
import id.co.pspmobile.data.network.invoice.InvoiceResDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var _unpaidInvoiceResponse: MutableLiveData<Resource<InvoiceResDto>> = MutableLiveData()
    val unpaidInvoiceResponse: LiveData<Resource<InvoiceResDto>> get() = _unpaidInvoiceResponse

    private var _paidInvoiceResponse: MutableLiveData<Resource<InvoiceResDto>> = MutableLiveData()
    val paidInvoiceResponse: LiveData<Resource<InvoiceResDto>> get() = _paidInvoiceResponse

    private var _allInvoiceResponse: MutableLiveData<Resource<InvoiceResDto>> = MutableLiveData()
    val allInvoiceResponse: LiveData<Resource<InvoiceResDto>> get() = _allInvoiceResponse

    private var _paymentInvoiceResponse: MutableLiveData<Resource<InvoicePaymentDto>> =
        MutableLiveData()
    val paymentInvoiceResponse: LiveData<Resource<InvoicePaymentDto>> get() = _paymentInvoiceResponse


    fun getUnpaidInvoice(page: Int) = viewModelScope.launch {
        _unpaidInvoiceResponse.value = Resource.Loading
        _unpaidInvoiceResponse.value = invoiceRepository.getUnpaidInvoice(page, 10)
    }

    fun getPaidInvoice(page: Int) = viewModelScope.launch {
        _paidInvoiceResponse.value = Resource.Loading
        _paidInvoiceResponse.value = invoiceRepository.getPaidInvoice(page, 10)
    }

    fun getAllInvoice(page: Int) = viewModelScope.launch {
        _allInvoiceResponse.value = Resource.Loading
        _allInvoiceResponse.value = invoiceRepository.getAllInvoice(page, 10)
    }

    fun paymentInvoice(amount: Double, invoiceId: String) = viewModelScope.launch {
        _paymentInvoiceResponse.value = Resource.Loading
        _paymentInvoiceResponse.value = invoiceRepository.paymentInvoice(amount, invoiceId)
    }

    fun getUserData() = userPreferences.getUserData()
    fun getLanguage() = userPreferences.getLanguage()

    fun getBalanceUser() = userPreferences.getBalanceData()
}