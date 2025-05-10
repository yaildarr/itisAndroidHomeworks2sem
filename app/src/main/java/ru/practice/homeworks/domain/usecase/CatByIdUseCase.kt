package ru.practice.homeworks.domain.usecase

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.repository.CatDataRepository
import javax.inject.Inject

class CatByIdUseCase @Inject constructor(
    private val catDataRepository: CatDataRepository
) {

    suspend operator fun invoke(id: String) : CatDataModel{
        return withContext(Dispatchers.IO) {
            val data = catDataRepository.getCatsById(id)
            Log.d("MyLog",data.id+" "+data.url)
            return@withContext data
        }
    }
}