package id.co.pspmobile.ui.createpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.responses.LoginResponse
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.ui.forgotpassword.ModelChangePassword
import id.co.pspmobile.ui.forgotpassword.ModelCreatePassword
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel@Inject constructor(
    private val repository: AuthRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    private val _changePasswordResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val changePasswordResponse: LiveData<Resource<Unit>>
        get() = _changePasswordResponse

    fun sendChangePassword(body: ModelChangePassword) = viewModelScope.launch {
        _changePasswordResponse.value = Resource.Loading
        _changePasswordResponse.value = repository.changePassword(body)
    }


    private val _createPasswordResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val createPasswordResponse: LiveData<Resource<Unit>>
        get() = _createPasswordResponse

    fun sendCreatePassword(body: ModelCreatePassword) = viewModelScope.launch {
        _createPasswordResponse.value = Resource.Loading
        _createPasswordResponse.value = repository.createPassword(body)
    }

    private val _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse
    fun login(
        username: String,
        password: String
    ) = viewModelScope.launch {
        _loginResponse.value = Resource.Loading
        _loginResponse.value = repository.login(username, password)
    }

    private val _checkCredentialResponse: MutableLiveData<Resource<CheckCredentialResponse>> = MutableLiveData()
    val checkCredentialResponse: LiveData<Resource<CheckCredentialResponse>>
        get() = _checkCredentialResponse
    fun checkCredential() = viewModelScope.launch {
        _checkCredentialResponse.value = Resource.Loading
        _checkCredentialResponse.value = repository.getCredentialInfo()
    }

    fun saveUsername(username: String) = viewModelScope.launch {
        userPreferences.saveUsername(username)
    }
    fun getUsername(): String {
        return userPreferences.getUsername()
    }
    fun saveUserData(data: CheckCredentialResponse) = viewModelScope.launch {
        userPreferences.saveUserData(data)
    }
    fun saveAccessToken(string: String) = viewModelScope.launch{
        userPreferences.saveAccessToken(string)
    }
    fun savePassword(password: String) = viewModelScope.launch {
        userPreferences.savePassword(password)
    }
    fun getPassword(): String {
        return userPreferences.getPassword()
    }

    fun clearToken() = viewModelScope.launch {
        userPreferences.saveAccessToken("")
    }
}