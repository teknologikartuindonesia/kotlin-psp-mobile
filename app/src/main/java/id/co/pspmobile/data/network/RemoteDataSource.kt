package id.co.pspmobile.data.network


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RemoteDataSource @Inject constructor() {

    companion object {
        const val BASE_URL = "https://api.katalis.info"
        const val tempDebug = true
    }

    val baseURL get() = BASE_URL

    fun <Api> buildApi(
        api: Class<Api>,
    ): Api {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(
                getRetrofitClient(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)
    }

    private fun getRetrofitClient(logging: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .also { client ->
                if (tempDebug) {
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