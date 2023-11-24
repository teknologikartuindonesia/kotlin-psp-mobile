package id.co.pspmobile.data.network.info

data class InfoResDto(
    var status: Int,
    var size: Int,
    var page: Int,
    var totalElements: Int,
    var totalPages: Int,
    var sort: String,
    var sortDirection: Int,
    var content: List<InfoDto>
)

data class InfoDto(
    var id: String,
    var isVideo: Boolean,
    var videoUrl: String? = null,
    var isAction: Boolean,
    var valueAdd: List<String>,
    var image: String,
    var imageOpt: List<String>,
    var description: String,
    var viewCount: Int,
    var urlFb: String? = null,
    var urlIg: String? = null,
    var urlDownload: String? = null,
    var latitude: String? = null,
    var longitude: String? = null,
    var showTime: String? = null,
    var enable: Boolean,
    var isHeadline: Boolean,
    var isOpen: Boolean,
    var isDeleted: Boolean,
    var createTime: String,
    var updateTime: String,
    var companyId: String,
    var accountId: String,
    var title: String,
    var subtitle: String,
    var tags: List<String>
)