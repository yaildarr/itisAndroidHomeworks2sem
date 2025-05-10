package ru.practice.homeworks.domain.repository

import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.wrapper.CatWrapper

interface CatDataRepository {

    suspend fun getRandomCat() : CatDataModel

    suspend fun getCatsById(id: String) : CatWrapper
}