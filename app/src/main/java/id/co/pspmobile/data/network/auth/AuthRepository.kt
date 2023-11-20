package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import javax.inject.Inject

class AuthRepository @Inject constructor (
    private val api: AuthApi,
    private val userPreferences: UserPreferences
): BaseRepository(){

    suspend fun login(
        username: String,
        password: String
    ) = safeApiCall({
            api.login(ModelLogin(username, password))
        },
        userPreferences
    )

    suspend fun getCredentialInfo() = safeApiCall({
        api.getUserInfo()
    },
        userPreferences
    )

    suspend fun getBalance() = safeApiCall({
        api.getBalance()
    },
        userPreferences
    )

    suspend fun getActiveBroadcast() = safeApiCall({
        api.getActiveBroadcast("ACTIVE", "MOBILE", 10, 0, "updateTime", -1)
    },
        userPreferences
    )

    suspend fun getInfoNews(body: ModelInfoNews, page: Int) = safeApiCall({
        api.getInfoNews(body, 10, page, "updateTime", -1)
    },
        userPreferences
    )
}