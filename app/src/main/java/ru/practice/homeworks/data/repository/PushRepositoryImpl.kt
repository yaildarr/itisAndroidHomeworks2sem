package ru.practice.homeworks.data.repository

import ru.practice.homeworks.data.local.PushLocalData
import ru.practice.homeworks.domain.repository.PushRepository
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(
    private val localData : PushLocalData
) : PushRepository {
    override suspend fun saveBackground(data: String) {
        localData.savePush(data)
    }
}