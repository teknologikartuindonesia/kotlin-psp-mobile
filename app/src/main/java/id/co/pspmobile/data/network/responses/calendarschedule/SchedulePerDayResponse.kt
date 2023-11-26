package id.co.pspmobile.data.network.responses.calendarschedule

data class SchedulePerDayResponse(
    val content: List<Content>,
    val page: Int,
    val size: Int,
    val sort: String,
    val sortDirection: Int,
    val status: Int,
    val totalElements: Int,
    val totalPages: Int
)