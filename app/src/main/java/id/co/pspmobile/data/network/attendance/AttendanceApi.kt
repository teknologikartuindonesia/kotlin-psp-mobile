package id.co.pspmobile.data.network.attendance

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AttendanceApi {

    @GET("/api_absensi_v2/pengguna_get_their_data_absen_hari/{callerId}/{date}")
    suspend fun getAttendance(
        @Path("callerId") callerId: String,
        @Path("date") date: String
    ): Response<List<AttendanceResDto>>

}