package id.co.pspmobile.data.network.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {

    @GET("/katalis/user/va")
    suspend fun getVa() : Response<VaResDto>

    @GET("/katalis/bank/va/open/{bankName}")
    suspend fun createVa(
        @Path("bankName") bankName: String
    ) : Response<Unit>

    @Headers("Content-Type: application/json")
    @POST("/katalis/idn/gateway/topup")
    suspend fun topUpIdn(
        @Body topUpIdnReqDto: TopUpIdnReqDto,
    ) : Response<TopUpIdnResDto>

}