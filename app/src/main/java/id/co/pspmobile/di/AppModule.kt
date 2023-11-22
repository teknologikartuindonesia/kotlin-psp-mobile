package id.co.pspmobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.pspmobile.data.network.RemoteDataSource
import id.co.pspmobile.data.network.auth.AuthApi
import id.co.pspmobile.data.network.invoice.InvoiceApi
import id.co.pspmobile.data.network.transaction.TransactionApi
import id.co.pspmobile.data.network.user.UserApi
import id.co.pspmobile.data.network.report.ReportApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource
    ): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java)
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
    fun provideReportApi(
        remoteDataSource: RemoteDataSource
    ): ReportApi {
        return remoteDataSource.buildApi(ReportApi::class.java)
    }
}