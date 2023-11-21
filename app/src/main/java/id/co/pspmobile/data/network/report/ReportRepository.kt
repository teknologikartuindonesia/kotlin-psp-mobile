package id.co.pspmobile.data.network.report

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class ReportRepository @Inject constructor (
    private val api: ReportApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getTransaction(
        month: String,
        year: Int
    ) = safeApiCall({
        api.getTransaction(month, year)
    },
        userPreferences
    )

    suspend fun getOldTransaction(
        month: String,
        year: Int
    ) = safeApiCall({
        api.getOldTransaction(month, year)
    },
        userPreferences
    )

    suspend fun getMutation(
        date: String,
        size: Int,
        page: Int
    ) = safeApiCall({
        api.getMutation(date, "21-200", size, page, "dateTime,desc")
    },
        userPreferences
    )

    suspend fun getOldMutation(
        date: String,
        size: Int,
        page: Int
    ) = safeApiCall({
        api.getOldMutation(date, "21-200", size, page, "dateTime,desc")
    },
        userPreferences
    )
}