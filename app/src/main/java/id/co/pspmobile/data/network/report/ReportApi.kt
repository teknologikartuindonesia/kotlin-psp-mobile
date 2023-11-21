package id.co.pspmobile.data.network.report

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApi {

    @GET("/python/transaction/report")
    suspend fun getTransaction(
        @Query("month") month: String,
        @Query("year") year: Int
    ) : Response<TransactionResDto>

    @GET("/python/transaction/report/old")
    suspend fun getOldTransaction(
        @Query("month") month: String,
        @Query("year") year: Int
    ) : Response<TransactionResDto>

    @GET("/python/balance/mutation")
    suspend fun getMutation(
        @Query("date") date: String,
        @Query("coa") coa: String,
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ) : Response<MutationResDto>

    @GET("/python/balance/old/mutation")
    suspend fun getOldMutation(
        @Query("date") date: String,
        @Query("coa") coa: String,
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("sort") sort: String
    ) : Response<MutationResDto>

}