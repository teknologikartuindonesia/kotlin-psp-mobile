package id.co.pspmobile.ui.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel@Inject constructor(
    private val repository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _sendOtpResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val sendOtpResponse: LiveData<Resource<Unit>>
        get() = _sendOtpResponse

    fun sendOtp(body: ModelSendOtp) = viewModelScope.launch {
        _sendOtpResponse.value = Resource.Loading
        _sendOtpResponse.value = repository.sendOtp(body)
    }

    private val _checkOtpResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val checkOtpResponse: LiveData<Resource<Unit>>
        get() = _checkOtpResponse

    fun checkOtp(body: ModelCheckOtp) = viewModelScope.launch {
        _checkOtpResponse.value = Resource.Loading
        _checkOtpResponse.value = repository.checkOtp(body)
    }

    private val _sendNewPassword: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val sendNewPassword: LiveData<Resource<Unit>>
        get() = _sendNewPassword

    fun sendNewPasswordForgot(body: ModelSendOtp) = viewModelScope.launch {
        _sendNewPassword.value = Resource.Loading
        _sendNewPassword.value = repository.sendNewPasswordForgot(body)
    }

    fun saveUsername(username: String) = viewModelScope.launch {
        userPreferences.saveUsername(username)
    }

    fun savePassword(password: String) = viewModelScope.launch {
        userPreferences.savePassword(password)
    }

    fun getLastResetPassword() = userPreferences.getLastResetPassword()

    fun saveLastResetPassword(lastResetPassword: String) = viewModelScope.launch {
        userPreferences.saveLastResetPassword(lastResetPassword)
    }

}