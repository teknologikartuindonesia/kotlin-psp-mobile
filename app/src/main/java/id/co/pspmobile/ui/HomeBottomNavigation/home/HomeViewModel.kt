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
import id.co.pspmobile.data.network.customapp.CustomAppRepository
import id.co.pspmobile.data.network.model.customapp.ModelColor
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.customapp.CustomAppResponse
import id.co.pspmobile.data.network.responses.infonews.BroadcastResponse
import id.co.pspmobile.data.network.responses.infonews.InfoNewsResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val authRepository: AuthRepository,
    private val customAppRepository: CustomAppRepository,
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

    fun getCurrentLanguage(): String {
        return userPreferences.getLanguage()
    }

    fun saveColor(primary: String, secondary: String) = viewModelScope.launch {
        userPreferences.saveColor(ModelColor(primary, secondary))
    }

    fun getColor(): ModelColor {
        return userPreferences.getColor()
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


}
