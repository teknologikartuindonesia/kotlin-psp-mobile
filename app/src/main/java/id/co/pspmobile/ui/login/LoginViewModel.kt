package id.co.pspmobile.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.LoginResponse
import id.co.pspmobile.data.network.responses.infonews.BroadcastResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var _broadcastResponse: MutableLiveData<Resource<BroadcastResponse>> = MutableLiveData()
    val broadcastResponse: LiveData<Resource<BroadcastResponse>> get() = _broadcastResponse
    fun getBroadcastMessage() = viewModelScope.launch {
        _broadcastResponse.value = Resource.Loading
        _broadcastResponse.value = authRepository.getActiveBroadcast()
    }

    private var _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>> get() = _loginResponse

    fun login(username: String, password: String) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = authRepository.login(username, password)
    }

    private var _checkCredentialResponse: MutableLiveData<Resource<CheckCredentialResponse>> = MutableLiveData()
    val checkCredentialResponse: LiveData<Resource<CheckCredentialResponse>> get() = _checkCredentialResponse

    fun checkCredential() = viewModelScope.launch {
        _checkCredentialResponse.value = Resource.Loading
        _checkCredentialResponse.value = authRepository.getCredentialInfo()
    }

    fun getUsername() = userPreferences.getUsername()
    fun saveUsername(username: String) = viewModelScope.launch {
        userPreferences.saveUsername(username)
    }
    fun getPassword() = userPreferences.getPassword()
    fun savePassword(password: String) = viewModelScope.launch {
        userPreferences.savePassword(password)
    }

    fun getToken() : String {
        return userPreferences.getAccessToken()
    }

    fun saveUserData(data: CheckCredentialResponse) = viewModelScope.launch {
        userPreferences.saveUserData(data)
    }
    fun getBaseUrl(): String {
        return remoteDataSource.baseURL
    }

}