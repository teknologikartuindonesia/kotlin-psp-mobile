package id.co.pspmobile.data.network.user

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/main_a/general/tutorial_bank")
    suspend fun getTutorial(
        @Query("bank") bank: String,
        @Query("lang") lang: String,
        @Query("cid") cid: String,
        @Query("kode") kode: String,
        @Query("va") va: String
    ) : Response<TutorialResDto>
}