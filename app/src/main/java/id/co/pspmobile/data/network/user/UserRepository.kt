package id.co.pspmobile.data.network.user

import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.BaseRepository
import javax.inject.Inject

class UserRepository @Inject constructor (
    private val api: UserApi,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getVa() = safeApiCall(
        {
            api.getVa()
        },
        userPreferences
    )

    suspend fun createVa(bankName: String) = safeApiCall(
        {
            api.createVa(bankName)
        },
        userPreferences
    )

    suspend fun topUpIdn(amount: Double) = safeApiCall(
        {
            api.topUpIdn(TopUpIdnReqDto(amount))
        },
        userPreferences
    )
    // https://api.dev.katalis.info/main_a/general/tutorial_bank?bank=BSI&lang=id&cid=asd&kode=ad&va=dsa
    suspend fun getTutorial(bank: String, lang: String, cid: String, kode: String, va: String) = safeApiCall(
        {
            api.getTutorial(bank, lang, cid, kode, va)
        },
        userPreferences
    )

    suspend fun updateAccount(userReqDto: UserReqDto) = safeApiCall(
        {
            api.updateAccount(userReqDto)
        },
        userPreferences
    )

}