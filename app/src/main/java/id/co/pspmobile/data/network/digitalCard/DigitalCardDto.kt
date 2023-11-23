package id.co.pspmobile.data.network.digitalCard

class DigitalCardDto : ArrayList<DigitalCardDtoItem>()

data class DigitalCardDtoItem(
    val accountId: String,
    val active: Boolean,
    val amount: Double,
    val balance: Double,
    val callerId: String,
    val callerName: String,
    val cardBalance: Double,
    val ceiling: Double,
    val companyId: String,
    val deviceBalance: Double,
    val id: String,
    val limitDaily: Double,
    val limitMax: Double,
    val name: String,
    val nfcId: String,
    val photoUrl: String,
    val usePin: Boolean
)