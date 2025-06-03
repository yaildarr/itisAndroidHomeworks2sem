package ru.practice.homeworks.domain.repository

interface ConvertRepository {
    suspend fun convertCurrency(from : String, to : String) : String
}