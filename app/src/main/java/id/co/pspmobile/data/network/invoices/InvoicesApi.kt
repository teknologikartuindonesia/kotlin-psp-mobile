package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.responses.InvoiceResponse
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface InvoicesApi {
    @GET("katalis/invoices")
    suspend fun getDataInvoice(
        @Header("Authorization") accessToken: String
    ) : Response<List<InvoiceResponse>>
}