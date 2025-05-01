package ru.practice.homeworks.data.remote.response

import com.google.gson.annotations.SerializedName

data class CatResponse(
    @SerializedName("id")
    val id : String,
    @SerializedName("url")
    val url : String,
)