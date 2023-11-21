package id.co.pspmobile.data.network.report

data class TransactionDetailResDto(
    var status: Int,
    var size: Int,
    var page: Int,
    var totalElements: Int,
    var totalPages: Int,
    var sort: String,
    var sortDirection: Int,
    var content: List<TransactionDetailDto>
)

data class TransactionDetailDto(
    var transactionId: String,
    var tags: String,
    var transactionName: String,
    var status: String,
    var callerName: String,
    var createDate: String,
    var note: String,
    var reffNumber: String? = null,
    var debit: Double,
    var credit: Double,
    var channel: String? = null
)