package id.co.pspmobile.ui.HomeBottomNavigation.profile.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.profile.UploadImageResponse
import kotlinx.coroutines.launch
import javax.inject.Inject
import okhttp3.MultipartBody

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
): ViewModel() {

    private var _uploadImageResponse: MutableLiveData<Resource<UploadImageResponse>> = MutableLiveData()
    val uploadImageResponse: MutableLiveData<Resource<UploadImageResponse>> get() = _uploadImageResponse
    fun uploadImage(image: MultipartBody.Part) = viewModelScope.launch {
        _uploadImageResponse.value = Resource.Loading
        _uploadImageResponse.value = authRepository.uploadImage(image)
    }

    private var _updateProfileResponse: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val updateProfileResponse: MutableLiveData<Resource<Unit>> get() = _updateProfileResponse
    fun updateProfile(body: CheckCredentialResponse) = viewModelScope.launch {
        _updateProfileResponse.value = Resource.Loading
        _updateProfileResponse.value = authRepository.updateProfile(body)
    }

    private var _checkCredentialResponse: MutableLiveData<Resource<CheckCredentialResponse>> = MutableLiveData()
    val checkCredentialResponse: MutableLiveData<Resource<CheckCredentialResponse>> get() = _checkCredentialResponse
    fun checkCredential() = viewModelScope.launch {
        _checkCredentialResponse.value = Resource.Loading
        _checkCredentialResponse.value = authRepository.getCredentialInfo()
    }


    fun getUserInfo() = userPreferences.getUserData()


    fun saveUserInfo(body: CheckCredentialResponse) = viewModelScope.launch {
        userPreferences.saveUserData(body)
    }

}