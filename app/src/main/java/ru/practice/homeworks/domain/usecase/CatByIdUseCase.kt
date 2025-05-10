package ru.practice.homeworks.domain.usecase

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.repository.CatDataRepository
import ru.practice.homeworks.domain.wrapper.CatWrapper
import javax.inject.Inject

class CatByIdUseCase @Inject constructor(
    private val catDataRepository: CatDataRepository
) {

    suspend operator fun invoke(id: String) : CatWrapper{
        return withContext(Dispatchers.IO) {
            val data = catDataRepository.getCatsById(id)
            return@withContext data
        }
    }
}