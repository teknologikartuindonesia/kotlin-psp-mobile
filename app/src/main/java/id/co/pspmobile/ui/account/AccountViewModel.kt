package id.co.pspmobile.ui.account

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.RemoteDataSource
import javax.inject.Inject

@HiltViewModel
class AccountViewModel  @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    fun getBaseUrl(): String {
        return remoteDataSource.baseURL
    }
}