package id.co.pspmobile.data.network.responses.digitalCard

import java.util.Date

data class SyncDigitalCardItem(
    var createDate: String? = null,
    var callerName: String? = null,
    var nfcId: String? = null,
    var id: String? = null
)