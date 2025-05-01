package ru.practice.homeworks.domain.model

data class CatDataModel(
    val id : String,
    val url : String
) {

    companion object{
        val EMPTY = CatDataModel(
            id = "none",
            url = "none"
        )
    }
}