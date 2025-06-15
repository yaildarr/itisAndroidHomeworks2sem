package ru.practice.homeworks.domain.usecase

import ru.practice.homeworks.domain.repository.PushRepository
import javax.inject.Inject

class HandleBackgroundUseCase @Inject constructor(
    private val repository: PushRepository
) {
    suspend fun invoke(data : String){
        repository.saveBackground(data)
    }
}