package id.co.pspmobile.data.network.transaction

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TransactionApi {

    @GET("/python/transaction/report/tag/topup")
    suspend fun getHistoryTopUp(
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<HistoryTopUpResDto>

}