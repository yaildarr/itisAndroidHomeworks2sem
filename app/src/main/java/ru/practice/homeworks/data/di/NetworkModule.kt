package ru.practice.homeworks.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practice.homeworks.data.remote.CatApi
import ru.practice.homeworks.data.remote.ConvertApi
import ru.practice.homeworks.data.remote.interceptor.CatAuthInterceptor
import ru.practice.homeworks.data.remote.interceptor.ConvertAuthInterceptor
import javax.inject.Named

import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    val catUrl = "https://api.thecatapi.com/v1/images/"

    val convertUrl = "https://v6.exchangerate-api.com/v6/1ebf39af050a1e2b8e82da90/"

    @Provides
    @Singleton
    fun provideConvertApi(
        @Named("convert") convertHttpClient: OkHttpClient,
        gson: Gson
    ) : ConvertApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder =  Retrofit.Builder()
            .baseUrl(convertUrl)
            .client(convertHttpClient)
            .addConverterFactory(gsonFactory)

        return builder.build().create(ConvertApi::class.java)
    }



    @Provides
    @Singleton
    fun provideCatApi(
        @Named("cat") catHttpClient: OkHttpClient,
        gson: Gson
    ) : CatApi {
        val gsonFactory = GsonConverterFactory.create(gson)

        val builder =  Retrofit.Builder()
            .baseUrl(catUrl)
            .client(catHttpClient)
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
    @Named("cat")
    fun provideCatOkHttpClient(
        authInterceptor : CatAuthInterceptor
    ) : OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(authInterceptor)
        return builder.build()
    }

    @Provides
    @Named("convert")
    fun provideConvertHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

}