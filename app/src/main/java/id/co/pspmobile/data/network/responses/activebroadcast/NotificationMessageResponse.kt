package id.co.pspmobile.data.network.responses.activebroadcast

data class NotificationMessageResponse(
    val content: List<Content>,
    val page: Int,
    val size: Int,
    val sort: String,
    val sortDirection: Int,
    val status: Int,
    val totalElements: Int,
    val totalPages: Int
)