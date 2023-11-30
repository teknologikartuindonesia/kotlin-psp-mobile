package id.co.pspmobile.ui.HomeBottomNavigation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.infonews.BroadcastResponse
import id.co.pspmobile.data.network.responses.infonews.InfoNewsResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    private val remoteDataSource: RemoteDataSource,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getUserData(): CheckCredentialResponse {
        return userPreferences.getUserData()
    }

    fun getBalanceData(): BalanceResponse {
        return userPreferences.getBalanceData()
    }

    fun saveBalanceData(balance: BalanceResponse) = viewModelScope.launch {
        userPreferences.saveBalanceData(balance)
    }

    fun getBaseUrl(): String {
        return remoteDataSource.baseURL
    }

    private val _balanceResponse: MutableLiveData<Resource<BalanceResponse>> = MutableLiveData()
    val balanceResponse: LiveData<Resource<BalanceResponse>> get() = _balanceResponse
    fun getBalance() = viewModelScope.launch {
        _balanceResponse.value = Resource.Loading
        _balanceResponse.value = authRepository.getBalance()
    }

    private val _infoNewsResponse: MutableLiveData<Resource<InfoNewsResponse>> = MutableLiveData()
    val infoNewsResponse: LiveData<Resource<InfoNewsResponse>> get() = _infoNewsResponse
    fun getInfoNews(body: ModelInfoNews, page: Int) = viewModelScope.launch {
        _infoNewsResponse.value = Resource.Loading
        _infoNewsResponse.value = authRepository.getInfoNews(body, page)
    }

    private var _broadcastResponse: MutableLiveData<Resource<BroadcastResponse>> = MutableLiveData()
    val broadcastResponse: LiveData<Resource<BroadcastResponse>> get() = _broadcastResponse
    fun getBroadcastMessage() = viewModelScope.launch {
        _broadcastResponse.value = Resource.Loading
        _broadcastResponse.value = authRepository.getActiveBroadcast()
    }

    private val _fbToken: MutableLiveData<Resource<Unit>> = MutableLiveData()
    fun saveFirebaseToken(
        token: String,
        serverKeyId:String,
    ) = viewModelScope.launch {
        _fbToken.value = Resource.Loading
        _fbToken.value = authRepository.saveFirebaseToken(token,serverKeyId)
    }
}