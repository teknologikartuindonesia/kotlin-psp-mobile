package id.co.pspmobile.data.network.user

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.Resource
import javax.inject.Inject

class UserRepository @Inject constructor (
    private val api: UserApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getVa() : Resource<VaResDto> = safeApiCall(
        {
            api.getVa(
                userPreferences.getAccessToken()
            )
        },
        userPreferences
    )

}