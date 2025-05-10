package ru.practice.homeworks.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthInterceptor @Inject constructor() : Interceptor{
    private val apiKey = "live_MTM1GWF1QezSA2TDOktJqkfjVNTTldqTk68jE1b2als4LnnpJbK0M1WIqWULS2wF"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
            .addHeader("x-api-key",apiKey)

        return chain.proceed(builder.build())
    }

}