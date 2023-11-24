package id.co.pspmobile.ui.digitalCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.digitalCard.DigitalCardDto
import id.co.pspmobile.data.network.digitalCard.DigitalCardDtoItem
import id.co.pspmobile.data.network.digitalCard.DigitalCardRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DigitalCardViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val digitalCardRepository: DigitalCardRepository
) : ViewModel() {

    private var _digitalCardResponse: MutableLiveData<Resource<DigitalCardDto>> = MutableLiveData()
    val digitalCardResponse: LiveData<Resource<DigitalCardDto>> get() = _digitalCardResponse

    private val _updateDigitalCardResponse: MutableLiveData<Resource<DigitalCardDtoItem>> = MutableLiveData()
    val updateDigitalCardResponse: LiveData<Resource<DigitalCardDtoItem>> get() = _updateDigitalCardResponse

    fun getDigitalCard(page: Int) = viewModelScope.launch {
        _digitalCardResponse.value = Resource.Loading
        _digitalCardResponse.value = digitalCardRepository.getDigitalCard(page, 10)
    }


    fun updateDigitalCard(
        cardId: String,
        accountId: String,
        active: Boolean,
        amount: Double,
        balance: Double,
        callerId: String,
        callerName: String,
        cardBalance: Double,
        ceiling: Double,
        companyId: String,
        deviceBalance: Double,
        id: String,
        limitDaily: Double,
        limitMax: Double,
        name: String,
        nfcId: String,
        photoUrl: String,
        usePin: Boolean
    ) = viewModelScope.launch {
        _updateDigitalCardResponse.value = Resource.Loading
        _updateDigitalCardResponse.value = digitalCardRepository.updateDigitalCard(cardId, accountId, active, amount, balance, callerId, callerName, cardBalance, ceiling, companyId, deviceBalance, id, limitDaily, limitMax, name, nfcId, photoUrl, usePin)
    }

}