package id.co.pspmobile.data.network


import android.content.Context
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
        const val BASE_URL = "https://api.katalis.info"
        const val tempDebug = true
    }

    val ctx = context

    val baseURL get() = BASE_URL

    fun <Api> buildApi(
        api: Class<Api>,
    ): Api {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(
                getRetrofitClient(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY),
                    ctx
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    private fun getRetrofitClient(logging: HttpLoggingInterceptor, context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(ChuckerInterceptor(context))
            .also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }
            }
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}