package id.co.pspmobile.data.network.donation

import java.io.Serializable

data class DonationResDto(
    val content: ArrayList<DonationDto>,
    val totalPages: Int,
    val totalElement: Int? = null
)

data class DonationDto(
    val id: String,
    val createDate: String,
    val title: String,
    val coaCode: String,
    val companyId: String,
    val description: String? = null,
    val status: String,
    val participants: List<Participant>,
    val tags: List<String>
): Serializable

data class Participant(
    val id: String,
    val dateTime: String,
    val accountId: String,
    val accountName: String,
    val callerId: String? = null,
    val callerName: String? = null,
    val amount: Double,
    val transactionId: String
)
data class DonationPayDto(
    val accountId: String,
    val callerId: String? = "",
    val amount: Int,
    val cash: Boolean,
    val channel: String,
    val donationId: String
)
data class DonationPayResDto(
    val id: String
)