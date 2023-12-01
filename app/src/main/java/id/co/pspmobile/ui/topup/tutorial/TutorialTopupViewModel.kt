package id.co.pspmobile.ui.topup.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.user.TutorialResDto
import id.co.pspmobile.data.network.user.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialTopupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
): ViewModel() {

    private var _tutorialResponse: MutableLiveData<Resource<TutorialResDto>> = MutableLiveData()
    val tutorialResponse: LiveData<Resource<TutorialResDto>> get() = _tutorialResponse
    fun getTutorial(bank: String, lang: String, cid: String, kode: String, va: String) = viewModelScope.launch {
        _tutorialResponse.value = Resource.Loading
        _tutorialResponse.value = userRepository.getTutorial(bank, lang, cid, kode, va)
    }


    fun getLanguage() : String {
        return userPreferences.getLanguage()
    }
}