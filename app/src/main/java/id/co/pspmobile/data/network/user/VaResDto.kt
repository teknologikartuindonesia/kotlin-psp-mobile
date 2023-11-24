package id.co.pspmobile.data.network.user

data class VaResDto(
	val accountId: String? = null,
	val name: String? = null,
	val accountNumber: String? = null,
	val vaNumbers: List<VaNumber?>? = null
)

data class VaNumber(
	val bankId: String? = null,
	val bankName: String? = null,
	val number: String? = null,
	val adminBank: Double = 0.0
)
