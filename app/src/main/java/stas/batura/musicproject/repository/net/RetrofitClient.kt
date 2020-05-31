package stas.batura.musicproject.repository.net

import com.google.gson.annotations.SerializedName
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


object RetrofitClient {

//    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    val BASE_URL = "https://canarado-lyrics.p.rapidapi.com/lyrics/"
//    val BASE_URL = "http://api.chartlyrics.com/apiv1.asmx/"

//    private fun provideRetrofit(): Retrofit {
//        val string = ""
//        return Retrofit.Builder().baseUrl(BASE_URL).build()
//    }
    private val loggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClient = OkHttpClient.Builder()



    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
     * object.
     */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(httpClient.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request: Request =
                    chain.request().newBuilder()
                        .addHeader("x-rapidapi-host", "canarado-lyrics.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "9016123d29mshfd7b7fa9b0d89eap17019fjsnfb4983ac17fa")
                        .build()
                return chain.proceed(request)
            }
        }).addInterceptor(loggingInterceptor).build())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    /**
     * Use the Retrofit builder to build a retrofit object using a Xml converter
     * object.
     */
    private val retrofitXmlCour = Retrofit.Builder()
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(httpClient.addInterceptor(loggingInterceptor).build())
        .baseUrl(BASE_URL)
        .build()

    object netApi {
        val retrofitServise : API_COUR by lazy {
            retrofit.create(API_COUR::class.java)
        }
    }

    interface API_COUR {
        @GET ("/lyrics/{title}")
        fun getSongText(@Path("title") title: String):
                Deferred<FullResponse>

        @GET ("cat/says/{sentence}")
        fun getSayingCat(@Path ("sentence") value: String) : Deferred<SearchResponse>


    }

}

class FullResponse {

    @SerializedName("status")
    var status: StatusResponse? = null

    @SerializedName("content")
    var content: List<Content>? = null
}

class StatusResponse {

    @SerializedName("code")
    var code: Int? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("failed")
    var failed: Boolean? = false
}

class Content {

    @SerializedName("title")
    var title: String? = null

    @SerializedName("artist")
    var artist: String? = null

    @SerializedName("lyrics")
    var lyrics: String? = null

}