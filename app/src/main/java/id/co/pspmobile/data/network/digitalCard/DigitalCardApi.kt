package id.co.pspmobile.data.network.digitalCard

import id.co.pspmobile.data.network.invoice.InvoiceResDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DigitalCardApi {

    @GET("/katalis/card")
    suspend fun getDigitalCard(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<DigitalCardDto>
}