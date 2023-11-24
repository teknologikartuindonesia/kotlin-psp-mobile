package id.co.pspmobile.ui.HomeBottomNavigation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val userPreferences: UserPreferences,
    private val remoteDataSource: RemoteDataSource
): ViewModel(){

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    fun getUserData(): CheckCredentialResponse? {
        return userPreferences.getUserData()
    }

    fun getBaseUrl(): String {
        return remoteDataSource.baseURL
    }
}