package id.co.pspmobile.ui.HomeBottomNavigation.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.information.InformationRepository
import id.co.pspmobile.data.network.information.InformationResDto
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.responses.infonews.InfoNewsResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val informationRepository: InformationRepository,
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var _informationResponse: MutableLiveData<Resource<InformationResDto>> = MutableLiveData()
    val informationResponse: LiveData<Resource<InformationResDto>> get() = _informationResponse


    fun getInformation(page: Int) = viewModelScope.launch {
        _informationResponse.value = Resource.Loading
        _informationResponse.value = informationRepository.getInformation(page, 10)
    }

    private val _infoNewsResponse: MutableLiveData<Resource<InfoNewsResponse>> = MutableLiveData()
    val infoNewsResponse: LiveData<Resource<InfoNewsResponse>> get() = _infoNewsResponse
    fun getInfoNews(body: ModelInfoNews, page: Int) = viewModelScope.launch {
        _infoNewsResponse.value = Resource.Loading
        _infoNewsResponse.value = authRepository.getInfoNews(body, page)
    }

    fun getBaseUrl(): String {
        return remoteDataSource.baseURL
    }

    fun getUserData() = userPreferences.getUserData()
}