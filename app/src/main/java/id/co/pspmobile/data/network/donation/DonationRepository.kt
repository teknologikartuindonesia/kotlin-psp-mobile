package id.co.pspmobile.data.network.donation

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class DonationRepository @Inject constructor (
    private val api: DonationApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getAllDonation(
        page: Int,
        size: Int
    ) = safeApiCall(
        {
            api.getAllDonation("createDate,Desc", page, size)
        },
        userPreferences
    )

    suspend fun getDonationById(
        id: String
    ) = safeApiCall(
        {
            api.getDonationById(id)
        },
        userPreferences
    )

    suspend fun donationPayment(
        id: String
    ) = safeApiCall(
        {
            api.donationPayment(id)
        },
        userPreferences
    )

    suspend fun donate(
        don: DonationPayDto
    ) = safeApiCall(
        {
            api.donate(don)
        },
        userPreferences
    )
}