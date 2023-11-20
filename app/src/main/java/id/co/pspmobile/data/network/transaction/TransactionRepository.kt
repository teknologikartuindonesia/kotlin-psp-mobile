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
    ) : List<TransactionResDto> {
        val response = safeApiCall(
            {
                api.getHistoryTopUp(page, size)
            },
            userPreferences
        )
        return if (response is Resource.Success) {
            val historyTopUpResDto = response.value
            historyTopUpResDto.content
        } else {
            emptyList()
        }
    }

}