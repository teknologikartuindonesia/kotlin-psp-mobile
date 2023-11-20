package id.co.pspmobile.data.network.transaction

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.Resource
import javax.inject.Inject

class TransactionRepository @Inject constructor (
    private val api: TransactionApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getHistoryTopUp(
        page: Int,
        size: Int
    ) : Resource<HistoryTopUpResDto> = safeApiCall(
        {
            api.getHistoryTopUp(page, size)
        },
        userPreferences
    )

}