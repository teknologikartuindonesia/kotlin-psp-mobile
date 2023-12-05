package id.co.pspmobile.data.network.digitalCard

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class DigitalCardRepository @Inject constructor(
    private val api: DigitalCardApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getDigitalCard(
        page: Int,
        size: Int
    ) = safeApiCall(
        {
            api.getDigitalCard("createdTime,Desc", page, size)
        },
        userPreferences
    )

    suspend fun updateDigitalCard(
        cardReqDto: CardReqDto
    ) = safeApiCall(
        {
            api.updateDigitalCard(
                cardReqDto.id,
                cardReqDto
            )
        },
        userPreferences
    )
}