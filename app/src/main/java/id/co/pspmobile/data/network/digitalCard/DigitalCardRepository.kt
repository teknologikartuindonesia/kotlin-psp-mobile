package id.co.pspmobile.data.network.digitalCard

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.model.ModelDigitalCard
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

    suspend fun updateDigitalCard(
        cardId: String,
        accountId: String,
        active: Boolean,
        amount: Double,
        balance: Double,
        callerId: String,
        callerName: String,
        cardBalance: Double,
        ceiling: Double,
        companyId: String,
        deviceBalance: Double,
        id: String,
        limitDaily: Double,
        limitMax: Double,
        name: String,
        nfcId: String,
        photoUrl: String,
        usePin: Boolean
    ): Resource<DigitalCardDtoItem> = safeApiCall(
        {
            api.updateDigitalCard(
                cardId,
                ModelDigitalCard(
                    accountId,
                    active,
                    amount,
                    balance,
                    callerId,
                    callerName,
                    cardBalance,
                    ceiling,
                    companyId,
                    deviceBalance,
                    id,
                    limitDaily,
                    limitMax,
                    name,
                    nfcId,
                    photoUrl,
                    usePin
                )
            )
        },
        userPreferences
    )
}