package id.co.pspmobile.ui.HomeBottomNavigation.profile.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val userPreferences: UserPreferences
): ViewModel() {
    fun saveLanguage(lang: String) = viewModelScope.launch {
        userPreferences.saveLanguage(lang)
    }

    fun getLanguage() = userPreferences.getLanguage()
}