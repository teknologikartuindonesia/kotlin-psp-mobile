package id.co.pspmobile.ui.attendance.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.attendance.AttendanceRepository
import id.co.pspmobile.data.network.attendance.AttendanceResDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceDetailViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val attendanceRepository: AttendanceRepository
) : ViewModel() {

    private var _attendanceResponse: MutableLiveData<Resource<AttendanceResDto>> = MutableLiveData()
    val attendanceResponse: LiveData<Resource<AttendanceResDto>> get() = _attendanceResponse

    fun getAttendance(callerId: String, date: String) = viewModelScope.launch {
        _attendanceResponse.value = Resource.Loading
        _attendanceResponse.value = attendanceRepository.getAttendance(callerId, date)
    }

    fun getBaseUrl(): String {
        return remoteDataSource.baseURL
    }

}