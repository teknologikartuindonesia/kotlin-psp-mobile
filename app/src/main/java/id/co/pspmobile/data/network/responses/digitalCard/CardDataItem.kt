package id.co.pspmobile.data.network.responses.digitalCard

data class CardDataItem(
    var nfcId: String,
    var history: MutableList<String>
)