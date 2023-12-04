package id.co.pspmobile.data.network.faq

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class FaqRepository @Inject constructor (
    private val api: FaqApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getFaq(
        category:String,
        lang: String,
        tag: String
    ) = safeApiCall({
        api.getFaq(category,lang, tag)
    },
        userPreferences
    )


}