package com.lx.todaysbing.api

import com.lx.todaysbing.data.bingapis.SearchResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BingApisService {

//    @GET("search?&form=BAXBGL&offset=0&count=30&setmkt=en-us&omwq=1&q=Eurasian%20otter")
    @GET("search")
    suspend fun search(
        @Query("form") form: String,
        @Query("q") q: String,
        @Query("form") setmkt: String = "en-us",
        @Query("omwq") omwq: Int = 1,
        @Query("offset") offset: Int = 0,
        @Query("count") count: Int = 30
    ): SearchResponse

    companion object {
        private const val BASE_URL = "https://c.bingapis.com/api/custom/opal/image/"

        fun create(): BingApisService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BingApisService::class.java)
        }
    }
}