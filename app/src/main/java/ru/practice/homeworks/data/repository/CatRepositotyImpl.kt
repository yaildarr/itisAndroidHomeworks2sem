package ru.practice.homeworks.data.repository

import ru.practice.homeworks.data.local.CacheEntry
import ru.practice.homeworks.data.local.CatCache
import ru.practice.homeworks.data.mapper.ApiResponceMapper
import ru.practice.homeworks.data.remote.CatApi
import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.repository.CatDataRepository
import ru.practice.homeworks.domain.wrapper.CatWrapper
import ru.practice.homeworks.domain.wrapper.DataSource
import javax.inject.Inject


class CatDataRepositoryImpl @Inject constructor(
    private val catApi: CatApi,
    private val mapper: ApiResponceMapper,
    private val catCache: CatCache<CatDataModel>
) : CatDataRepository {
    override suspend fun getRandomCat(): CatDataModel {
        val response = catApi.getRandomCat()?.firstOrNull()
        if (response == null) return CatDataModel.EMPTY

        return mapper.mapToCatDataModel(response)
    }

    override suspend fun getCatsById(id: String): CatWrapper {

        val cacheResult = catCache.getCache(id)
        if (cacheResult != null){
            return CatWrapper(catDataModel = cacheResult, source = DataSource.CACHE)
        }
        catCache.trackIntermediateRequest(id)

        val response = catApi.getCatsById(id)?.firstOrNull()
        if (response == null){
            return CatWrapper(catDataModel = CatDataModel.EMPTY, source = DataSource.API)
        }
        val data = mapper.mapToCatDataModel(response)
        catCache.saveToCache(id,data)
        return CatWrapper(catDataModel = data, source = DataSource.API)
    }
}