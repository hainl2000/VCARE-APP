package com.example.vcare_app.api

import com.example.vcare_app.data.sharepref.SharePrefManager
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        println("Outgoing request to ${request.url()}")
        if (checkRequiredToken(request)) {
            val accessToken = SharePrefManager.getAccessToken()
            request.newBuilder().header("Authorization", "Bearer ${accessToken}")
        }
        return chain.proceed(request)
    }

    private fun checkRequiredToken(request: Request): Boolean {
        if (request.url().toString().contains("auth/login") || request.url().toString()
                .contains("auth/register")
        ) {
            return false
        }
        return true
    }
}

object ApiClient {
    private const val baseUrl = "http://35.240.154.21/api/"

    private val okHttpClient: OkHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(RequestInterceptor)
        .build()

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}