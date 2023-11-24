package id.co.pspmobile.data.network.transaction

data class HistoryTopUpResDto(
	val status: Int? = null,
	val size: Int? = null,
	val page: Int? = null,
	val totalElements: Int? = null,
	val totalPages: Int? = null,
	val sort: String? = null,
	val sortDirection: Int? = null,
	val content: List<TransactionResDto>
)
