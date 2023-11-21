package id.co.pspmobile.data.network.model

data class ModelInvoice(
    val amount: Double,
    val cash: Boolean,
    val channel: String,
    val invoiceId: String,
    val tag: String
)