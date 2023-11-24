package id.co.pspmobile.data.network.responses.balance

data class BalanceResponse(
    val accountId: String,
    val balance: Double,
    val ceiling: Double,
    val coa: Coa,
    val companyId: String,
    val id: String,
    val lastCredit: Double,
    val lastDebit: Double,
    val lastTransactionId: String,
    val lastTransactionTime: String
)