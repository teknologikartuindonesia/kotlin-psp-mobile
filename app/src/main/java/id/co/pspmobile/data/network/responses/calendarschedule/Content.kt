package id.co.pspmobile.data.network.responses.calendarschedule

data class Content(
    val _id: String,
    val accountId: String,
    val classId: String,
    val companyId: String,
    val createTime: String,
    val day: String,
    val dayCode: Int,
    val isActive: Boolean,
    val isDeleted: Boolean,
    val lessons: List<Lesson>,
    val tags: List<String>,
    val updateTime: String,
    val year: String
)