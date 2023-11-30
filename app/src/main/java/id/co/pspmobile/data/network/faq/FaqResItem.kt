package id.co.pspmobile.data.network.faq

data class FaqResItem(
    val _id: String?,
    val accountId: String?,
    val answer: String?,
    val category: String?,
    val companyId: String?,
    val createTime: String?,
    val image: String?,
    val isDeleted: Boolean?,
    val lang: String?,
    val question: String?,
    val openPage:String?,
    val tag: List<String?>?,
    val updateTime: String?,
    val urutan: Int?
)