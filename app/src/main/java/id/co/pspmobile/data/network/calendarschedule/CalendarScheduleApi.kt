package id.co.pspmobile.data.network.calendarschedule

import id.co.pspmobile.data.network.responses.calendarschedule.AllAgendaResponse
import id.co.pspmobile.data.network.responses.calendarschedule.AllLessonResponse
import id.co.pspmobile.data.network.responses.calendarschedule.ComboYearResponse
import id.co.pspmobile.data.network.responses.calendarschedule.SchedulePerDayResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface CalendarScheduleApi {

    @GET("/academic/calendar/office/lesson/all")
    suspend fun getAllLesson(
        @Query("companyId") companyId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("dir") dir: Int
    ) : Response<AllLessonResponse>

    @GET("/academic/calendar/office/calendar/combo")
    suspend fun getCalendarCombo(
        @Query("companyId") companyId: String
    ) : Response<ComboYearResponse>

    @GET("/academic/calendar/office/schedule/all")
    suspend fun getSchedulePerDay(
        @Query("companyId") companyId: String,
        @Query("year") year: String,
        @Query("callerId") callerId: String,
        @Query("isActive") isActive: Boolean,
        @Query("day") day: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("dir") dir: Int
    ) : Response<SchedulePerDayResponse>

    @GET("/academic/calendar/office/calendar/agenda/all")
    suspend fun getAgenda(
        @Query("companyId") companyId: String,
        @Query("isActive") isActive: Boolean,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
        @Query("dir") dir: Int
    ) : Response<AllAgendaResponse>
}