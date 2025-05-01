package ru.practice.homeworks.domain.repository

import ru.practice.homeworks.domain.model.CatDataModel

interface CatDataRepository {

    suspend fun getRandomCat() : CatDataModel

    suspend fun getCatsById(id: String) : CatDataModel
}