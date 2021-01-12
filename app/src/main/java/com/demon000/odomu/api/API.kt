package com.demon000.odomu.api

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

public class API(baseURL: String, gson: Gson) {
    private val ACCESS_TOKEN_HEADER_NAME = "Access-Token"
    private val REFRESH_TOKEN_HEADER_NAME = "Refresh-Token"

    public val retrofit: Retrofit
    public var accessTokenObserver = MutableLiveData<String>()
    public var refreshTokenObserver = MutableLiveData<String>()

    init {
        val gsonConverter = GsonConverterFactory.create(gson)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val authInterceptor = Interceptor { chain ->
            val requestBuilder: Request.Builder = chain.request().newBuilder()

            if (accessTokenObserver.value != null) {
                requestBuilder.addHeader(ACCESS_TOKEN_HEADER_NAME, accessTokenObserver.value!!)
            }

            if (accessTokenObserver.value != null) {
                requestBuilder.addHeader(REFRESH_TOKEN_HEADER_NAME, refreshTokenObserver.value!!)
            }

            val request = requestBuilder.build()

            val response = chain.proceed(request)
                .newBuilder()
                .build()

            val accessToken = response.headers[ACCESS_TOKEN_HEADER_NAME]
            if (accessToken != null) {
                accessTokenObserver.postValue(accessToken)
            }

            val refreshToken = response.headers[REFRESH_TOKEN_HEADER_NAME]
            if (refreshToken != null) {
                refreshTokenObserver.postValue(refreshToken)
            }

            response
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .readTimeout(100, TimeUnit.SECONDS)
            .connectTimeout(100, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverter)
            .build()
    }
}
