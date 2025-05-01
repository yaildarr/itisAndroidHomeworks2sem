package ru.practice.homeworks.domain.usecase

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.repository.CatDataRepository
import javax.inject.Inject

class RandomCatUseCase @Inject constructor(
    private val catDataRepository: CatDataRepository
) {

    suspend operator fun invoke() : CatDataModel{
        return withContext(Dispatchers.IO) {
            val data = catDataRepository.getRandomCat()
            Log.d("MyLog",data.id+" "+data.url)
            return@withContext data
        }
    }
}