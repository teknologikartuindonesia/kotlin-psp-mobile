package id.co.pspmobile.data.network.responses.checkcredential

data class Openfire(
    val active: Boolean,
    val id: String?,
    val password: String?
)