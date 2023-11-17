package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.responses.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApi {

    @Headers("Content-Type: application/json")
    @POST("katalis/login")
    suspend fun login(
        @Body info: ModelLogin
    ): Response<LoginResponse>

    @GET("katalis/user/credential/check")
    suspend fun getUserInfo(): Response<CheckCredentialResponse>
}