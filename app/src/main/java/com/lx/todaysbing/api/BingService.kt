package com.lx.todaysbing.api

import com.lx.todaysbing.data.bing.HPImageArchiveResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BingService {

    // "https://global.bing.com/"
    // "https://www.bing.com/"
    // "https://cn.bing.com/"

    // http://www.bing.com/HPImageArchive.aspx?format=xml&idx=0&n=1&mkt=en-US   &video=1

    // 国内版
    // https://cn.bing.com/HPImageArchive.aspx?format=js&idx=2&n=1&nc=1614827483205&pid=hp&FORM=BEHPTB&uhd=1&uhdwidth=3840&uhdheight=2160
    // 国际版
    // https://cn.bing.com/HPImageArchive.aspx?format=hp&idx=2&n=1&nc=1614826841636&pid=hp&FORM=BEHPTB&ensearch=1&quiz=1&og=1&uhd=1&uhdwidth=3840&uhdheight=2160&IG=F1C2139BAD1A4E46A3FBD4781C29B89F&IID=SERP.1052

    // format hp xml js
    // n 0-8

    @GET("HPImageArchive.aspx")
    suspend fun getHPImageArchive(
        @Query("format") format: String? = null,
        @Query("n") n: Int,
        @Query("mkt") mkt: String? = null,
    ): HPImageArchiveResponse

    companion object {
//        const val BASE_URL = "https://cn.bing.com/"
        const val BASE_URL = "https://global.bing.com/"
//        const val BASE_URL = "https://www.bing.com/"

        fun create(): BingService {
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
                .create(BingService::class.java)
        }
    }
}