package ru.practice.homeworks.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import ru.practice.homeworks.data.remote.response.ConvertResponse

interface ConvertApi {

    @GET("pair/{from}/{to}")
    suspend fun convertCurrency(
        @Path("from") fromCurrency: String,
        @Path("to") toCurrency: String
    ): ConvertResponse

}