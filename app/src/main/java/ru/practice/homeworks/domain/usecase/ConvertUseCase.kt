package ru.practice.homeworks.domain.usecase

import ru.practice.homeworks.domain.repository.ConvertRepository
import javax.inject.Inject

class ConvertUseCase @Inject constructor(
    private val repository: ConvertRepository
){

    suspend operator fun invoke(from: String, to: String, amount: String) : String{
        val result = repository.convertCurrency(from,to)
        return (result.toDouble()*amount.toDouble()).toString()
    }
}