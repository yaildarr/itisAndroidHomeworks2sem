package ru.practice.homeworks.data.remote.response

import com.google.gson.annotations.SerializedName

data class ConvertResponse(
    @SerializedName("conversion_rate")
    val rate: String
)