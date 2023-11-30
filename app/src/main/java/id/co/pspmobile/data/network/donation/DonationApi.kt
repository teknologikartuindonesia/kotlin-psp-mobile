package id.co.pspmobile.data.network.donation

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DonationApi {

    @GET("/katalis/donation/all")
    suspend fun getAllDonation(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Response<DonationResDto>

    @GET("/katalis/donation/{id}")
    suspend fun getDonationById(
        @Path("id") id: String
    ) : Response<DonationDto>

    @POST("/katalis/donation/{id}")
    suspend fun donationPayment(
        @Path("id") id: String
    ) : Response<DonationResDto>


    @POST("/katalis/transaction/donation/payment")
    suspend fun donate(
        @Body donationPayDto: DonationPayDto
    ) : Response<DonationResDto>
}