package id.co.pspmobile.ui.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.calendarschedule.CalendarScheduleRepository
import id.co.pspmobile.data.network.responses.calendarschedule.AllAgendaResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarScheduleRepository,
    private val userPreferences: UserPreferences
): ViewModel() {

    fun getLanguage(): String {
        return userPreferences.getLanguage()
    }

    private var _allAgendaResponse: MutableLiveData<Resource<AllAgendaResponse>> = MutableLiveData()
    var allAgendaResponse: MutableLiveData<Resource<AllAgendaResponse>> = _allAgendaResponse
    fun getAgenda(
        startDate: String,
        endDate: String,
    ) = viewModelScope.launch{
        _allAgendaResponse.value = Resource.Loading
        _allAgendaResponse.value = repository.getAgenda(
            getCompanyId(), true, startDate, endDate, 0, 100, "startDate", 1)
    }

    fun getCompanyId(): String {
        return userPreferences.getUserData().activeCompany.id
    }
}