package id.co.pspmobile.data.network.digitalCard

data class CardReqDto(
    var id: String,
    var accountId: String,
    var nfcId: String,
    var companyId: String,
    var limitMax: Double,
    var limitDaily: Double,
    var balance: Double
)
