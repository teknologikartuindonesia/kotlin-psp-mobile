package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.profile.UserResponse
import id.co.pspmobile.ui.forgotpassword.ModelChangePassword
import id.co.pspmobile.ui.forgotpassword.ModelCheckOtp
import id.co.pspmobile.ui.forgotpassword.ModelCreatePassword
import id.co.pspmobile.ui.forgotpassword.ModelSendOtp
import okhttp3.MultipartBody
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

    suspend fun getUserInfo() = safeApiCall({
        api.getUserInfoEditProfile()
    },
        userPreferences
    )

    suspend fun sendOtp(body: ModelSendOtp) = safeApiCall ({
        api.sendOtp(body)
    },
        userPreferences
    )


    suspend fun checkOtp(body: ModelCheckOtp) = safeApiCall({
        api.checkOtp(body)
    },
        userPreferences
    )

    suspend fun changePassword(body: ModelChangePassword) = safeApiCall({
        api.sendChangePassword(body)
    },
        userPreferences
    )

    suspend fun createPassword(body: ModelCreatePassword) = safeApiCall({
        api.sendCreatePassword(body)
    },
        userPreferences
    )

    suspend fun uploadImage(body: MultipartBody.Part) = safeApiCall({
        api.uploadImage(body)
    },
        userPreferences
    )

    suspend fun updateProfile(body: UserResponse) = safeApiCall({
        api.updateProfile(body)
    },
        userPreferences
    )
    suspend fun sendNewPasswordForgot(body: ModelSendOtp) = safeApiCall({
        api.sendNewPasswordForgot(body)
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

    suspend fun getNotificationMessage(page: Int) = safeApiCall({
        api.getNotification(
            "",
            "",
            page,
            10,
            "createdTime,desc",
        )
    },
        userPreferences
    )

    suspend fun getSentBroadcast(page: Int) = safeApiCall({
        api.getSentBroadcast(
            10,
            page,
            "broadcastSent",
            -1
        )
    },
        userPreferences
    )
}