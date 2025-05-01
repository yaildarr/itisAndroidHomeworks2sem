package ru.practice.homeworks.data.repository

import ru.practice.homeworks.data.mapper.ApiResponceMapper
import ru.practice.homeworks.data.remote.CatApi
import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.repository.CatDataRepository
import javax.inject.Inject


class CatDataRepositoryImpl @Inject constructor(
    private val catApi: CatApi,
    private val mapper: ApiResponceMapper
) : CatDataRepository {
    override suspend fun getRandomCat(): CatDataModel {
        val response = catApi.getRandomCat()?.firstOrNull()
        if (response == null) return CatDataModel.EMPTY

        return mapper.mapToCatDataModel(response)
    }

    override suspend fun getCatsById(id: String): CatDataModel {
        val response = catApi.getCatsById(id)?.firstOrNull()
        if (response == null) return CatDataModel.EMPTY

        return mapper.mapToCatDataModel(response)
    }
}