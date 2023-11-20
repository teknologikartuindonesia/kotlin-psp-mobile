package id.co.pspmobile.data.network.responses.checkcredential

data class CheckCredentialResponse(
    val activeCompany: ActiveCompany,
    val companies: List<Company>,
    val firstLogin: Boolean,
    val tags: List<String>,
    val user: UserX
)