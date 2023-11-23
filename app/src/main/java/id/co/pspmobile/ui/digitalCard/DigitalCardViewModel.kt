package id.co.pspmobile.ui.digitalCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.digitalCard.DigitalCardDto
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

    fun getDigitalCard(page: Int) = viewModelScope.launch {
        _digitalCardResponse.value = Resource.Loading
        _digitalCardResponse.value = digitalCardRepository.getDigitalCard(page, 10)
    }

}