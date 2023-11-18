package id.co.pspmobile.ui.HomeBottomNavigation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getUserData(): CheckCredentialResponse {
        return userPreferences.getUserData()
    }
}