package id.co.pspmobile.data.network.digitalCard

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.Resource
import javax.inject.Inject

class DigitalCardRepository @Inject constructor(
    private val api: DigitalCardApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getDigitalCard(
        page: Int,
        size: Int
    ): Resource<DigitalCardDto> = safeApiCall(
        {
            api.getDigitalCard("createdTime,Desc", page, size)
        },
        userPreferences
    )
}