package id.co.pspmobile.data.network.transaction

data class TransactionResDto(
	val id: String? = null,
	val channel: String? = null,
	val name: String? = null,
	val callerName: String? = null,
	val companyId: String? = null,
	val accountId: String? = null,
	val reffNumber: String? = null,
	val byAccountId: String? = null,
	val amount: Double = 0.0,
	val dateTime: String? = null,
	val note: String? = null,
	val tags: String? = null,
	val status: String? = null,
)
