package id.co.pspmobile.data.network.responses.checkcredential

import java.io.Serializable

data class CallerIdentity(
    val callerId: String,
    val id: String,
    val name: String,
    val photoUrl: String,
    val tags: List<String>,
    val title: String
): Serializable