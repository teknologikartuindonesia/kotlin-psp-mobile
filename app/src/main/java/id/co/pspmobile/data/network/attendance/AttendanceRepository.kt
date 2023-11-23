package id.co.pspmobile.data.network.attendance

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class AttendanceRepository @Inject constructor (
    private val api: AttendanceApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getAttendance(
        callerId: String,
        date: String
    ) = safeApiCall({
        api.getAttendance(callerId, date)
    },
        userPreferences
    )

}