package ru.practice.homeworks.data.repository

import ru.practice.homeworks.data.remote.ConvertApi
import ru.practice.homeworks.domain.repository.ConvertRepository
import javax.inject.Inject

class ConvertRepositoryImpl @Inject constructor(
    private val api: ConvertApi
) : ConvertRepository {
    override suspend fun convertCurrency(
        from: String,
        to: String,
    ): String {
        return api.convertCurrency(from,to).rate
    }
}