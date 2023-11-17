package id.co.pspmobile.data.network.responses

data class CallerIdentity(
    val callerId: String,
    val id: String,
    val name: String,
    val photoUrl: String,
    val tags: List<String>,
    val title: String
)