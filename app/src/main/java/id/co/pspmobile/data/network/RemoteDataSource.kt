package id.co.pspmobile.data.network


import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteDataSource @Inject constructor(@ApplicationContext context: Context) {

    companion object {
//        const val BASE_URL = "https://api.katalis.info"
        const val CS_NUMBER = "6281326269992"
        const val BASE_URL = "https://api.dev.katalis.info"
    }

    val ctx = context

    val baseURL get() = BASE_URL
    val csNumber get() = CS_NUMBER

    fun <Api> buildApi(
        api: Class<Api>,
    ): Api {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(
                getRetrofitClient(ctx)
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }
    fun <Api> buildApiImage(
        api: Class<Api>,
    ): Api {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(
                getRetrofitClient(ctx)
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    private fun getRetrofitClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(false)
                    .build()
            )
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}
