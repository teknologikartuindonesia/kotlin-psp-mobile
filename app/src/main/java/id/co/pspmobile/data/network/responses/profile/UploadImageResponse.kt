package id.co.pspmobile.data.network.responses.profile

data class UploadImageResponse(
    val accountId: String,
    val companyId: String,
    val createTime: String,
    val fileName: String,
    val fileType: String,
    val isDeleted: Boolean,
    val name: String,
    val originName: String,
    val temp: Boolean,
    val updateTime: String,
    val used: Int,
    val userId: String
)