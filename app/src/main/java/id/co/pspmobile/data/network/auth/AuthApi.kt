package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.model.firebase.ModelFirebaseToken
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.LoginResponse
import id.co.pspmobile.data.network.responses.activebroadcast.BroadcastMessageResponse
import id.co.pspmobile.data.network.responses.activebroadcast.NotificationMessageResponse
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import id.co.pspmobile.data.network.responses.infonews.BroadcastResponse
import id.co.pspmobile.data.network.responses.infonews.InfoNewsResponse
import id.co.pspmobile.data.network.responses.profile.UploadImageResponse
import id.co.pspmobile.data.network.responses.profile.UserResponse
import id.co.pspmobile.ui.forgotpassword.ModelChangePassword
import id.co.pspmobile.ui.forgotpassword.ModelCheckOtp
import id.co.pspmobile.ui.forgotpassword.ModelCreatePassword
import id.co.pspmobile.ui.forgotpassword.ModelSendOtp
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface AuthApi {

    @Headers("Content-Type: application/json")
    @POST("katalis/login")
    suspend fun login(
        @Body info: ModelLogin
    ): Response<LoginResponse>

    @GET("katalis/user/credential/check")
    suspend fun getUserInfo(): Response<CheckCredentialResponse>

    // https://api.katalis.info/katalis/user
    @GET("katalis/user")
    suspend fun getUserInfoEditProfile(): Response<UserResponse>

    @PUT("katalis/sso/credential/forget")
    suspend fun sendOtp(
        @Body data: ModelSendOtp
    ): Response<Unit>

    @POST("katalis/sso/credential/forget")
    suspend fun checkOtp(
        @Body body: ModelCheckOtp
    ): Response<Unit>

    @PUT("katalis/sso/credential/forget")
    suspend fun sendNewPasswordForgot(
        @Body data: ModelSendOtp
    ): Response<Unit>

    @PUT("katalis/sso/credential/change")
    suspend fun sendChangePassword(
        @Body data: ModelChangePassword
    ): Response<Unit>

    @PUT("katalis/sso/credential/create")
    suspend fun sendCreatePassword(
        @Body data: ModelCreatePassword
    ): Response<Unit>

    @GET("python/balance")
    suspend fun getBalance(): Response<BalanceResponse>

    @Multipart
    @POST("main_a/image/upload_image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<UploadImageResponse>

    // https://api.katalis.info/katalis/user
    @PUT("katalis/user")
    suspend fun updateProfile(
        @Body body: UserResponse
    ): Response<Unit>


    @GET("main_a/broadcast/broadcast/all")
    suspend fun getActiveBroadcast(
        @Query("status") status: String?,
        @Query("target") target: String?,
        @Query("size") size: Int?,
        @Query("page") page: Int?,
        @Query("sort") sort: String?,
        @Query("dir") dir: Int?
    ) : Response<BroadcastResponse>

    @POST("main_a/info/get_info")
    suspend fun getInfoNews(
        @Body body: ModelInfoNews,
        @Query("size") size: Int?,
        @Query("page") page: Int?,
        @Query("sort") sort: String?,
        @Query("dir") dir: Int?
    ) : Response<InfoNewsResponse>

    @GET("python/notification")
    suspend fun getNotification(
        @Query("email") email: String?,
        @Query("phone") phone: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("sort") sort: String?,
    ) : Response<NotificationMessageResponse>

    @GET("main_a/broadcast/broadcast/sent")
    suspend fun getSentBroadcast(
        @Query("size") size: Int?,
        @Query("page") page: Int?,
        @Query("sort") sort: String?,
        @Query("dir") dir: Int?
    ) : Response<BroadcastMessageResponse>

    @PUT("katalis/user/firebase/token")
    suspend fun saveFirebaseToken(
        @Body data: ModelFirebaseToken
    ): Response<Unit>
}