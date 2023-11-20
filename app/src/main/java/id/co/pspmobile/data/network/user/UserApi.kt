package id.co.pspmobile.data.network.user

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("/katalis/user/va")
    suspend fun getVa() : Response<VaResDto>

    @GET("/katalis/bank/va/open/{bankName}")
    suspend fun createVa(
        @Path("bankName") bankName: String
    ) : Response<Unit>

}