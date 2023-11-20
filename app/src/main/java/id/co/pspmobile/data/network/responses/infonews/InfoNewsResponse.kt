package id.co.pspmobile.data.network.responses.infonews

data class InfoNewsResponse(
    val content: List<Content>,
    val page: Int,
    val size: Int,
    val sort: Any,
    val sortDirection: Int,
    val status: Int,
    val totalElements: Int,
    val totalPages: Int
)