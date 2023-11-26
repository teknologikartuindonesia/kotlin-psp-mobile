package id.co.pspmobile.data.network.information

import id.co.pspmobile.data.network.model.infonews.ModelInfoNews
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface InformationApi {

    @Headers("Content-Type: application/json")
    @POST("/main_a/info/get_info")
    suspend fun getInformation(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Body modelInfoNews: ModelInfoNews
    ): Response<InformationResDto>

}