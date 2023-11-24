package id.co.pspmobile.ui.attendance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun getBaseUrl(): String {
        return remoteDataSource.baseURL
    }

    fun getUserData() : CheckCredentialResponse {
        return userPreferences.getUserData()
    }

}