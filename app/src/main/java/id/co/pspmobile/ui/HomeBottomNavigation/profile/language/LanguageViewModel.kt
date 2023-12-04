package id.co.pspmobile.ui.HomeBottomNavigation.profile.language

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.responses.customapp.CustomAppResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
): ViewModel() {
    fun saveLanguage(lang: String) = viewModelScope.launch {
        userPreferences.saveLanguage(lang)
    }

    fun getLanguage() = userPreferences.getLanguage()

    fun getUserData() = userPreferences.getUserData()

    private val _customApp: MutableLiveData<Resource<CustomAppResponse>> = MutableLiveData()
    val customAppResponse: LiveData<Resource<CustomAppResponse>> get() = _customApp
    fun getCustomApp(companyId: String, lang: String) = viewModelScope.launch {
        _customApp.value = Resource.Loading
        _customApp.value = authRepository.getCustomApp(companyId, lang)
    }

    fun saveLocalCustomApp(customApp: CustomAppResponse) = viewModelScope.launch {
        userPreferences.saveCustomApp(customApp)
    }
}