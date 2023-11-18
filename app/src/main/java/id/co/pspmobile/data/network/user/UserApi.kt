package id.co.pspmobile.data.network.user

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApi {

    @GET("katalis/user/va")
    suspend fun getVa(
        @Header("Authorization") accessToken: String
    ) : Response<VaResDto>

    @GET("katalis/user/va")
    suspend fun createVa(
        @Header("Authorization") accessToken: String
    ) : Response<VaResDto>

}