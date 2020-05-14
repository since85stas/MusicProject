package stas.batura.musicproject.repository.net

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


object RetrofitClient {

    val BASE_URL = "https://cataas.com/"

    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://cataas.com/").build()
    }

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
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    object netApi {
        val retrofitServise : API_COUR by lazy {
            retrofit.create(API_COUR::class.java)
        }
    }

    interface API_COUR {
        @GET ("cat")
        fun getSimpleCat(): Deferred<ResponseBody>

        @GET ("cat/says/{sentence}")
        fun getSayingCat(@Path ("sentence") value: String) : Deferred<ResponseBody>
    }

}