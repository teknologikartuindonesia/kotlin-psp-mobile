package id.co.pspmobile.data.network.auth

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.ModelLogin
import id.co.pspmobile.data.network.responses.InvoiceResponse
import javax.inject.Inject

class InvoicesRepository @Inject constructor (
    private val api: InvoicesApi,
    private val userPreferences: UserPreferences
): BaseRepository(){


    suspend fun getDataInvoice() : Resource<List<InvoiceResponse>> = safeApiCall(
        {
            api.getDataInvoice(userPreferences.getAccessToken())
        },
        userPreferences
    )

}