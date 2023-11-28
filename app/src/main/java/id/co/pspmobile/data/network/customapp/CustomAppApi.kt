package id.co.pspmobile.data.network.customapp

import id.co.pspmobile.data.network.responses.customapp.CustomAppResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CustomAppApi {

    @GET("main_a/web_view/custom_apps/mobile/{companyId}")
    suspend fun getCustomApp(
        @Path("companyId") companyId: String,
        @Query("lang") lang: String
    ): Response<CustomAppResponse>

    @GET("main_a/web_view/custom_apps/icon/{companyId}/{icon}")
    suspend fun getIcon(
        @Path("companyId") companyId: String,
        @Path("icon") icon: String
    ): Response<String>
}