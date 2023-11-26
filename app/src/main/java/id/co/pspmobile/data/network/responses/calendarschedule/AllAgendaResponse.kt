package id.co.pspmobile.data.network.responses.calendarschedule

data class AllAgendaResponse(
    val content: List<ContentXX>,
    val page: Int,
    val size: Int,
    val sort: String,
    val sortDirection: Int,
    val status: Int,
    val totalElements: Int,
    val totalPages: Int
)