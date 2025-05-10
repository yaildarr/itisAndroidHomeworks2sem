package ru.practice.homeworks.domain.wrapper

import ru.practice.homeworks.domain.model.CatDataModel

data class CatWrapper(
    val catDataModel: CatDataModel,
    val source: DataSource
)

enum class DataSource {
    CACHE,
    API
}