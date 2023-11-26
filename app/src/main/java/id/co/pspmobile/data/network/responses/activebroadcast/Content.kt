package id.co.pspmobile.data.network.responses.activebroadcast

data class Content(
    val accountId: String,
    val accountName: String,
    val category: String,
    val companyId: String,
    val companyName: String,
    val content: String,
    val createdTime: String,
    val email: Boolean,
    val emailDestination: String,
    val emailSendStatus: String,
    val firebase: Boolean,
    val firebaseDestination: String,
    val firebaseSendStatus: String,
    val id: String,
    val message: String,
    val note: String,
    val openfire: Boolean,
    val openfireDestination: String,
    val openfireSendStatus: String,
    val process: String,
    val readStatus: String,
    val sms: Boolean,
    val smsDestination: String,
    val smsSendStatus: String,
    val tags: List<Any>,
    val title: String
)