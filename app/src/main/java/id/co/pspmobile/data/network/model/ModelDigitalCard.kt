package id.co.pspmobile.data.network.model

import java.io.Serializable

data class ModelDigitalCard(
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
    var limitDaily: Double,
    var limitMax: Double,
    val name: String,
    val nfcId: String,
    val photoUrl: String,
    val usePin: Boolean
): Serializable
