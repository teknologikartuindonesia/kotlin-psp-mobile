package id.co.pspmobile.data.network.responses.infonews

data class ContentX(
    val _id: String,
    val accountId: String,
    val broadcastType: String,
    val companyId: String,
    val createTime: String,
    val endTime: String,
    val executor: String,
    val firebaseTopic: List<String>,
    val images: List<Any>,
    val imagesFirebase: List<String>,
    val isDashboard: Boolean,
    val isDeleted: Boolean,
    val isEmail: Boolean,
    val isEmailSent: Boolean,
    val isFireBase: Boolean,
    val isFireBaseSent: Boolean,
    val isLive: Boolean,
    val isMobile: Boolean,
    val message: String,
    val note: Any,
    val pic: String,
    val startTime: String,
    val status: String,
    val subtitle: String,
    val title: String,
    val updateTime: String
)