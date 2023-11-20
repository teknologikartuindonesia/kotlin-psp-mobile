package id.co.pspmobile.data.network.invoice

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InvoiceApi {

    @GET("/katalis/invoice/unpaid")
    suspend fun getUnpaidInvoice(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<InvoiceResDto>

    @GET("/katalis/invoice/paid")
    suspend fun getPaidInvoice(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<InvoiceResDto>

    @GET("/katalis/invoice/all")
    suspend fun getAllInvoice(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<InvoiceResDto>

}