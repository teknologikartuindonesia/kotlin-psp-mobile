package id.co.pspmobile.ui.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val userPreferences: UserPreferences
): ViewModel() {
    fun getUsername() = userPreferences.getUsername()
    fun saveUsername(username: String) = viewModelScope.launch {
        userPreferences.saveUsername(username)
    }
    fun getPassword() = userPreferences.getPassword()
    fun savePassword(password: String) = viewModelScope.launch {
        userPreferences.savePassword(password)
    }
}