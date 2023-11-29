package id.co.pspmobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.attendance.AttendanceApi
import id.co.pspmobile.data.network.auth.AuthApi
import id.co.pspmobile.data.network.calendarschedule.CalendarScheduleApi
import id.co.pspmobile.data.network.customapp.CustomAppApi
import id.co.pspmobile.data.network.digitalCard.DigitalCardApi
import id.co.pspmobile.data.network.donation.DonationApi
import id.co.pspmobile.data.network.information.InformationApi
import id.co.pspmobile.data.network.invoice.InvoiceApi
import id.co.pspmobile.data.network.report.ReportApi
import id.co.pspmobile.data.network.transaction.TransactionApi
import id.co.pspmobile.data.network.user.UserApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAttendanceApi(
        remoteDataSource: RemoteDataSource
    ): AttendanceApi {
        return remoteDataSource.buildApi(AttendanceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource
    ): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCalendarScheduleApi(
        remoteDataSource: RemoteDataSource
    ): CalendarScheduleApi {
        return remoteDataSource.buildApi(CalendarScheduleApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDigitalCardApi(
        remoteDataSource: RemoteDataSource
    ): DigitalCardApi {
        return remoteDataSource.buildApi(DigitalCardApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDonationApi(
        remoteDataSource: RemoteDataSource
    ): DonationApi {
        return remoteDataSource.buildApi(DonationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideInformationApi(
        remoteDataSource: RemoteDataSource
    ): InformationApi {
        return remoteDataSource.buildApi(InformationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideInvoiceApi(
        remoteDataSource: RemoteDataSource
    ): InvoiceApi {
        return remoteDataSource.buildApi(InvoiceApi::class.java)
    }

    @Singleton
    @Provides
    fun provideReportApi(
        remoteDataSource: RemoteDataSource
    ): ReportApi {
        return remoteDataSource.buildApi(ReportApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTransactionApi(
        remoteDataSource: RemoteDataSource
    ): TransactionApi {
        return remoteDataSource.buildApi(TransactionApi::class.java)
    }

    @Singleton
    @Provides
    fun provideUserApi(
        remoteDataSource: RemoteDataSource
    ): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCustomApi(
        remoteDataSource: RemoteDataSource
    ): CustomAppApi{
        return remoteDataSource.buildApiImage(CustomAppApi::class.java)
    }
}
