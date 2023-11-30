package id.co.pspmobile.data.network.faq

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FaqApi {

    @GET("/main_a/general/my_faq")
    suspend fun getFaq(
        @Query("category") category: String,
        @Query("lang") lang: String,
        @Query("tag") tag: String
    ): Response<FaqRes>

}