package id.co.pspmobile.data.network.info

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface InfoApi {

    @GET("/api_absensi_v2/pengguna_get_their_data_absen_hari/{callerId}/{date}")
    suspend fun getInfo(
        @Path("callerId") callerId: String,
        @Path("date") date: String
    ): Response<List<InfoResDto>>

}