package id.co.pspmobile.data.network.report

data class MutationResDto(
    var status: Int,
    var size: Int,
    var page: Int,
    var totalElements: Int,
    var totalPages: Int,
    var sort: String,
    var sortDirection: Int,
    var content: ArrayList<MutationDto>
)

data class MutationDto(
    var id: String,
    var channel: String,
    var category: String? = null,
    var reffNumber: String,
    var coaCode: String,
    var accountNumber: String,
    var accountId: String,
    var balance: Double,
    var callerId: String? = null,
    var callerName: String,
    var coaName: String,
    var credit: Double,
    var debit: Double,
    var accountName: String,
    var dateTime: String,
    var note: String,
    var status: String,
    var transactionName: String,
    var transactionId: String,
    var tags: String
)