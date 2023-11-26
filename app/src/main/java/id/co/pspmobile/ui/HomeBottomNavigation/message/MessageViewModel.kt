package id.co.pspmobile.ui.HomeBottomNavigation.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.auth.AuthRepository
import id.co.pspmobile.data.network.responses.activebroadcast.BroadcastMessageResponse
import id.co.pspmobile.data.network.responses.activebroadcast.NotificationMessageResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var _notificationMessageResponse: MutableLiveData<Resource<NotificationMessageResponse>> = MutableLiveData()
    val notificationMessageResponse: MutableLiveData<Resource<NotificationMessageResponse>> get() = _notificationMessageResponse
    fun getNotificationMessage(page: Int) = viewModelScope.launch {
        _notificationMessageResponse.value = Resource.Loading
        _notificationMessageResponse.value = authRepository.getNotificationMessage(page)
    }

    private var _broadcastMessageResponse: MutableLiveData<Resource<BroadcastMessageResponse>> = MutableLiveData()
    val broadcastMessageResponse: MutableLiveData<Resource<BroadcastMessageResponse>> get() = _broadcastMessageResponse

    fun getBroadcastMessage(page: Int) = viewModelScope.launch {
        _broadcastMessageResponse.value = Resource.Loading
        _broadcastMessageResponse.value = authRepository.getSentBroadcast(page)
    }
}