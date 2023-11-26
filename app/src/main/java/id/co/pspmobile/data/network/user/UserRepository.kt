package id.co.pspmobile.data.network.user

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class UserRepository @Inject constructor (
    private val api: UserApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getVa() = safeApiCall(
        {
            api.getVa()
        },
        userPreferences
    )

    suspend fun createVa(bankName: String) = safeApiCall(
        {
            api.createVa(bankName)
        },
        userPreferences
    )

    suspend fun topUpIdn(amount: Double) = safeApiCall(
        {
            api.topUpIdn(TopUpIdnReqDto(amount))
        },
        userPreferences
    )
}