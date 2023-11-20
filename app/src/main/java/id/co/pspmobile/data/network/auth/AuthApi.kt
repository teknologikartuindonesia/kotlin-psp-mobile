package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.LoginResponse
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import id.co.pspmobile.data.network.responses.infonews.InfoNewsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @Headers("Content-Type: application/json")
    @POST("katalis/login")
    suspend fun login(
        @Body info: ModelLogin
    ): Response<LoginResponse>

    @GET("katalis/user/credential/check")
    suspend fun getUserInfo(): Response<CheckCredentialResponse>

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