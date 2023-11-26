package id.co.pspmobile.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.RemoteDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogCsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val remoteDataSource: RemoteDataSource
): ViewModel() {
    fun getCsNumber(): String {
        return remoteDataSource.csNumber
    }

    fun getUserData() = userPreferences.getUserData()
}