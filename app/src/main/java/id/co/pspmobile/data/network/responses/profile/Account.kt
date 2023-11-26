package id.co.pspmobile.data.network.responses.profile

data class Account(
    val accountNumber: String,
    val active: Boolean,
    val callerIdentities: List<CallerIdentity>,
    val companyId: String,
    val id: String,
    val lastLogin: String,
    val note: String,
    val roles: List<String>,
    val sourceOfFund: SourceOfFund,
    val transactionUnlimited: Boolean,
    val vaNumbers: List<VaNumber>
)