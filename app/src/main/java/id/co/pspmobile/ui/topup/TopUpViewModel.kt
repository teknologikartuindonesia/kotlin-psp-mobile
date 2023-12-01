package id.co.pspmobile.ui.topup

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.user.TopUpIdnResDto
import id.co.pspmobile.data.network.user.UserRepository
import id.co.pspmobile.data.network.user.VaResDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    var recyclerViewState: Parcelable? = null

    private var _vaResponse: MutableLiveData<Resource<VaResDto>> = MutableLiveData()
    val vaResponse: LiveData<Resource<VaResDto>> get() = _vaResponse

    private var _createVaResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val createVaResponse: LiveData<Resource<Unit>> get() = _createVaResponse

    private var _topUpIdnResponse: MutableLiveData<Resource<TopUpIdnResDto>> = MutableLiveData()
    val topUpIdnResponse: LiveData<Resource<TopUpIdnResDto>> get() = _topUpIdnResponse

    private var _checkCredentialResponse: MutableLiveData<Resource<CheckCredentialResponse>> = MutableLiveData()
    val checkCredentialResponse: LiveData<Resource<CheckCredentialResponse>> get() = _checkCredentialResponse

    fun checkCredential() = viewModelScope.launch {
        _checkCredentialResponse.value = Resource.Loading
        _checkCredentialResponse.value = authRepository.getCredentialInfo()
    }
    fun getVa() = viewModelScope.launch {
        _vaResponse.value = Resource.Loading
        _vaResponse.value = userRepository.getVa()
    }

    fun createVa(bankName: String) = viewModelScope.launch {
        _createVaResponse.value = Resource.Loading
        _createVaResponse.value = userRepository.createVa(bankName)
    }

    fun topUpIdn(amount: Double) = viewModelScope.launch {
        _topUpIdnResponse.value = Resource.Loading
        _topUpIdnResponse.value = userRepository.topUpIdn(amount)
    }

    fun getUserData() : CheckCredentialResponse {
        return userPreferences.getUserData()
    }

    fun getBalanceData() : BalanceResponse {
        return userPreferences.getBalanceData()
    }

    fun saveUserData(checkCredentialResponse: CheckCredentialResponse) = viewModelScope.launch {
        userPreferences.saveUserData(checkCredentialResponse)
    }

}