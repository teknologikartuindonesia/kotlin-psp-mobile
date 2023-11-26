package id.co.pspmobile.ui.HomeBottomNavigation.profile.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.ui.forgotpassword.ModelChangePassword
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    private var _changePasswordResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val changePasswordResponse: MutableLiveData<Resource<Unit>> get() = _changePasswordResponse

    fun changePassword(oldPassword: String, newPassword: String) = viewModelScope.launch {
        _changePasswordResponse.value = Resource.Loading
        _changePasswordResponse.value = authRepository.changePassword(ModelChangePassword(oldPassword, newPassword))
    }

    fun getCurrentPassword() = userPreferences.getPassword()

    fun savePassword(password: String) = viewModelScope.launch {
        userPreferences.savePassword(password)
    }

}