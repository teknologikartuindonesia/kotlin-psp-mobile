package id.co.pspmobile.data.network.customapp

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class CustomAppRepository @Inject constructor(
    private val customAppApi: CustomAppApi,
    private val userPreferences: UserPreferences
): BaseRepository() {

    suspend fun getCustomApp(
        companyId: String,
        lang: String
    ) = safeApiCall({
        customAppApi.getCustomApp(companyId, lang)},
        userPreferences
    )

    suspend fun getIcon(
        companyId: String,
        icon: String
    ) = safeApiCall({
        customAppApi.getIcon(companyId, icon)},
        userPreferences
    )
}