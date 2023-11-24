package id.co.pspmobile.data.network.digitalCard

import id.co.pspmobile.data.network.invoice.InvoicePaymentDto
import id.co.pspmobile.data.network.invoice.InvoiceResDto
import id.co.pspmobile.data.network.model.ModelDigitalCard
import id.co.pspmobile.data.network.model.ModelInvoice
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DigitalCardApi {

    @GET("/katalis/card")
    suspend fun getDigitalCard(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<DigitalCardDto>

    @Headers("Content-Type: application/json")
    @PUT("katalis/card/{cardId}")
    suspend fun updateDigitalCard(
        @Path("cardId") cardId: String,
        @Body info: ModelDigitalCard
    ): Response<DigitalCardDtoItem>
}