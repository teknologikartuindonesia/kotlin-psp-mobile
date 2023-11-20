package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.LoginResponse
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import id.co.pspmobile.data.network.responses.infonews.InfoNewsResponse
import id.co.pspmobile.ui.forgotpassword.ModelChangePassword
import id.co.pspmobile.ui.forgotpassword.ModelCheckOtp
import id.co.pspmobile.ui.forgotpassword.ModelCreatePassword
import id.co.pspmobile.ui.forgotpassword.ModelSendOtp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthApi {

    @Headers("Content-Type: application/json")
    @POST("katalis/login")
    suspend fun login(
        @Body info: ModelLogin
    ): Response<LoginResponse>

    @GET("katalis/user/credential/check")
    suspend fun getUserInfo(): Response<CheckCredentialResponse>

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

    @GET("main_a/broadcast/broadcast/all")
    suspend fun getActiveBroadcast(
        @Query("status") status: String?,
        @Query("target") target: String?,
        @Query("size") size: Int?,
        @Query("page") page: Int?,
        @Query("sort") sort: String?,
        @Query("dir") dir: Int?
    ) : Response<InfoNewsResponse>

    @POST("main_a/info/get_info")
    suspend fun getInfoNews(
        @Body body: ModelInfoNews,
        @Query("size") size: Int?,
        @Query("page") page: Int?,
        @Query("sort") sort: String?,
        @Query("dir") dir: Int?
    ) : Response<InfoNewsResponse>
}