package id.co.pspmobile.data.network.report

data class TransactionResDto(
    var status: Int,
    var size: Int,
    var page: Int,
    var totalElements: Int,
    var totalPages: Int,
    var sort: String,
    var sortDirection: Int,
    var content: List<TransactionDto>
)

data class TransactionDto(
    var count: Int,
    var transactionName: String,
    var amount: Double,
    var tags: String? = null
)