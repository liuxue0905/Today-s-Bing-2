package com.lx.todaysbing.api

import com.lx.todaysbing.data.opalapi.Image
import com.lx.todaysbing.data.opalapi.IndexResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GalleryService {

    @GET("/api/index")
    suspend fun index(): IndexResponse

    // /api/random/20
    @GET("/api/random/{until}")
    suspend fun random(
        @Path("until") until: Int = 10
    ): List<Image>

    // /api/filters/0/20
    @POST("/api/filters/{offset}/{limit}")
    suspend fun filters(
        @Path("offset") offset: Int = 0,
        @Path("limit") limit: Int = 0,
        @Body map: Map<String, String?>,
    ): List<Image>

    companion object {
        const val BASE_URL = "https://binggallery.opalapi.com/"

        fun create(): GalleryService {
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
                .create(GalleryService::class.java)
        }
    }
}