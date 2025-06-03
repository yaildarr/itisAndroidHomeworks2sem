package ru.practice.homeworks.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ConvertAuthInterceptor @Inject constructor() : Interceptor{

    private val apiKey = "1ebf39af050a1e2b8e82da90"


    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val newUrl = originalHttpUrl.newBuilder()
            .addPathSegment(apiKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }

}