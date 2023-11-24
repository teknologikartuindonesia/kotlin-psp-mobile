package id.co.pspmobile.data.network.attendance

data class AttendanceResDto(
    var status: Int,
    var size: Int,
    var page: Int,
    var totalElements: Int,
    var totalPages: Int,
    var sort: String,
    var sortDirection: Int,
    var content: List<AttendanceDto>
)

data class AttendanceDto(
    var count: Int,
    var transactionName: String,
    var amount: Double,
    var tags: String? = null
)