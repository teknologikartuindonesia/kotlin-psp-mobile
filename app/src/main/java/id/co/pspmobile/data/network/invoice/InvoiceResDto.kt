package id.co.pspmobile.data.network.invoice

data class InvoiceResDto(
    val content: ArrayList<InvoiceDto>,
    val totalPages: Int? = null,
    val totalElements:Int?=null
)

data class InvoicePaymentDto(
    val id: String? = null,
    val dateTime: String? = null,
    val companyId: String? = null,
    val transactionId: String? = null,
    val byAccountId: String? = null,
    val amount: Double,
    val channel: String? = null,
    val tags: List<String>,
    val inquiryResponseDto: inquiryResponseDto
)

data class inquiryResponseDto(
    val id: String? = null,
    val title: String? = null,
    val companyId: String? = null,
    val dueDate: String? = null,
    val showDetail: Boolean,
    val createDate: String? = null,
    val invoiceDate: String? = null,
    val partialMethod: Boolean,
    val customerId:String?=null,
    val customerName:String?=null,
    val amount: Double,
    val paidAmount: Double,
)

data class InvoiceDto(
    val id: String? = null,
    val title: String? = null,
    val companyId: String? = null,
    val invoiceDate: String? = null,
    val createDate: String? = null,
    val dueDate: String? = null,
    val description: String? = null,
    val repeat: Boolean,
    val showDetail: Boolean,
    val partialMethod: Boolean,
    val invoiceId: String? = null,
    val callerId: String? = null,
    val callerName: String? = null,
    val amount: Double,
    val paidAmount: Double,
    val detail: ArrayList<Detail>,
    val status: String? = null,
    val history: List<History>,
    val tags: List<String>
)

data class Detail(
    val title: String,
    val count: Double,
    val price: Double,
    val amount: Double,
    val discount: Double,
    val coaCode: String
)

data class History(
    val dateTime: String,
    val amount: Double,
    val transactionId: String,
    val channel: String
)

