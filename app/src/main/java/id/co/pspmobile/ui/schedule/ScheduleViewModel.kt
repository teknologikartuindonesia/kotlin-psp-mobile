package id.co.pspmobile.ui.schedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.calendarschedule.CalendarScheduleRepository
import id.co.pspmobile.data.network.responses.calendarschedule.AllLessonResponse
import id.co.pspmobile.data.network.responses.calendarschedule.ComboYearResponse
import id.co.pspmobile.data.network.responses.calendarschedule.SchedulePerDayResponse
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: CalendarScheduleRepository,
    private val userPreferences: UserPreferences
): ViewModel(){

    fun getUser() = userPreferences.getUserData()

    fun getLanguage() = userPreferences.getLanguage()

    private val _allLessonResponse: MutableLiveData<Resource<AllLessonResponse>> = MutableLiveData()
    val allLessonResponse: MutableLiveData<Resource<AllLessonResponse>> get() = _allLessonResponse
    fun getAllLesson(
        companyId: String,
        page: Int,
        size: Int,
        sort: String,
        dir: Int
    ) = viewModelScope.launch{
        _allLessonResponse.value = Resource.Loading
        _allLessonResponse.value = repository.getAllLesson(companyId, page, size, sort, dir)
    }

    private val _calendarComboResponse: MutableLiveData<Resource<ComboYearResponse>> = MutableLiveData()
    val calendarComboResponse: MutableLiveData<Resource<ComboYearResponse>> get() = _calendarComboResponse
    fun getCalendarCombo(
        companyId: String
    ) = viewModelScope.launch{
        _calendarComboResponse.value = Resource.Loading
        _calendarComboResponse.value = repository.getCalendarCombo(companyId)
    }

    private val _schedulePerDayResponse: MutableLiveData<Resource<SchedulePerDayResponse>> = MutableLiveData()
    val schedulePerDayResponse: MutableLiveData<Resource<SchedulePerDayResponse>> get() = _schedulePerDayResponse
    fun getSchedulePerDay(
        companyId: String,
        year: String,
        callerId: String,
        isActive: Boolean,
        day: String,
        page: Int,
        size: Int,
        sort: String,
        dir: Int
    ) = viewModelScope.launch{
        _schedulePerDayResponse.value = Resource.Loading
        _schedulePerDayResponse.value = repository.getSchedulePerDay(companyId, year, callerId, isActive, day, page, size, sort, dir)
    }
}