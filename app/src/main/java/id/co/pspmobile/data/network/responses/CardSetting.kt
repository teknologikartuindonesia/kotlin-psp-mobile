package id.co.pspmobile.data.network.responses

data class CardSetting(
    val limitChange: Boolean,
    val limitDaily: Double,
    val limitMax: Double,
    val usedPin: Boolean
)