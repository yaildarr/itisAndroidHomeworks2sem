package ru.practice.homeworks.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practice.homeworks.data.remote.CatApi
import ru.practice.homeworks.data.remote.interceptor.AuthInterceptor

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    val url = "https://api.thecatapi.com/v1/images/"


    @Provides
    @Singleton
    fun provideCatApi(
        okHttpClient: OkHttpClient,
        gson: Gson
    ) : CatApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder =  Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)

        return builder.build().create(CatApi::class.java)
    }

    @Provides
    fun provideGson() : Gson{
        return GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .create()
    }

    @Provides
    fun provideOkHttpClient(
        authInterceptor : AuthInterceptor
    ) : OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(authInterceptor)
        return builder.build()
    }
}