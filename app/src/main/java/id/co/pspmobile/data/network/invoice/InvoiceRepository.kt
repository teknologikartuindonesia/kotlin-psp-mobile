package id.co.pspmobile.data.network.invoice

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.ModelInvoice
import javax.inject.Inject

class InvoiceRepository @Inject constructor (
    private val api: InvoiceApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getUnpaidInvoice(
        page: Int,
        size: Int
    ) : Resource<InvoiceResDto> = safeApiCall(
        {
            api.getUnpaidInvoice("invoiceDate,Desc", page, size)
        },
        userPreferences
    )

    suspend fun getPaidInvoice(
        page: Int,
        size: Int
    ) : Resource<InvoiceResDto> = safeApiCall(
        {
            api.getPaidInvoice("invoiceDate,Desc", page, size)
        },
        userPreferences
    )

    suspend fun getAllInvoice(
        page: Int,
        size: Int
    ) : Resource<InvoiceResDto> = safeApiCall(
        {
            api.getAllInvoice("invoiceDate,Desc", page, size)
        },
        userPreferences
    )

    suspend fun paymentInvoice(
        amount:Double,
        invoiceId:String
    ): Resource<InvoicePaymentDto> = safeApiCall({
        api.paymentInvoice(ModelInvoice(amount,true,"MOBILE",invoiceId,""))
    },userPreferences)

}