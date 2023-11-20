package id.co.pspmobile.ui.topup

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.user.UserRepository
import id.co.pspmobile.data.network.user.VaResDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var recyclerViewState: Parcelable? = null

    private var _vaResponse: MutableLiveData<Resource<VaResDto>> = MutableLiveData()
    val vaResponse: LiveData<Resource<VaResDto>> get() = _vaResponse

    private var _createVaResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val createVaResponse: LiveData<Resource<Unit>> get() = _createVaResponse

    fun getVa() = viewModelScope.launch {
        _vaResponse.value = Resource.Loading
        _vaResponse.value = userRepository.getVa()
    }

    fun createVa(bankName: String) = viewModelScope.launch {
        _createVaResponse.value = Resource.Loading
        _createVaResponse.value = userRepository.createVa(bankName)
    }

}