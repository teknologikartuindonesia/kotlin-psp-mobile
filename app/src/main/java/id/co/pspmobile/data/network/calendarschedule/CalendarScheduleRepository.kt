package id.co.pspmobile.data.network.calendarschedule

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class CalendarScheduleRepository@Inject constructor(
    private val api: CalendarScheduleApi,
    private val userPreferences: UserPreferences
): BaseRepository(){

    suspend fun getAllLesson(
        companyId: String,
        page: Int,
        size: Int,
        sort: String,
        dir: Int
    ) = safeApiCall(
        {
            api.getAllLesson(companyId, page, size, sort, dir)
        },
        userPreferences
    )

    suspend fun getCalendarCombo(
        companyId: String
    ) = safeApiCall(
        {
            api.getCalendarCombo(companyId)
        },
        userPreferences
    )

    suspend fun getSchedulePerDay(
        companyId: String,
        year: String,
        callerId: String,
        isActive: Boolean,
        day: String,
        page: Int,
        size: Int,
        sort: String,
        dir: Int
    ) = safeApiCall(
        {
            api.getSchedulePerDay(companyId, year, callerId, isActive, day, page, size, sort, dir)
        },
        userPreferences
    )
}