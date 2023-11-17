package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.model.ModelLogin
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

}