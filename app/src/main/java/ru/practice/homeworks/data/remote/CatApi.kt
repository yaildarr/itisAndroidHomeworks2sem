package ru.practice.homeworks.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practice.homeworks.data.remote.response.CatResponse


interface CatApi {

    @GET("search")
    suspend fun getRandomCat() : List<CatResponse>?

    @GET("search")
    suspend fun getCatsById(
        @Query("breed_ids") id: String,
    ) : List<CatResponse>?

}