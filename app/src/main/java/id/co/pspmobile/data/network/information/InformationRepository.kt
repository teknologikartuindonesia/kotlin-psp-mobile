package id.co.pspmobile.data.network.information

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.model.infonews.DefaultBool
import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import id.co.pspmobile.data.network.model.infonews.TagInSearch
import javax.inject.Inject

class InformationRepository @Inject constructor (
    private val api: InformationApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getInformation(
        page: Int,
        size: Int
    ) = safeApiCall(
        {
            val userData = userPreferences.getUserData()
            val defaultBool: List<DefaultBool> =
                listOf(DefaultBool("enable", true))
            val tagInSearch: List<TagInSearch> =
                listOf(TagInSearch("tags", userData.tags))
            val modelInfoNews = ModelInfoNews(defaultBool, emptyList(), tagInSearch)
            api.getInformation("updateTime", page, size, modelInfoNews)
        },
        userPreferences
    )

}