package id.co.pspmobile.data.network.responses

data class ActiveCompany(
    val activeUntil: String,
    val adminManualTopUp: Double,
    val adminManualTopUpMerchant: Double,
    val adminManualTopUpSchool: Double,
    val autoBanksTrx: List<String>,
    val banks: List<String>,
    val cardSetting: CardSetting,
    val companyCode: String,
    val customApps: Boolean,
    val id: String,
    val laundry: Boolean,
    val menus: List<Any>,
    val name: String,
    val solution: List<Any>,
    val suspensionStatus: String,
    val topUpManual: Boolean
)