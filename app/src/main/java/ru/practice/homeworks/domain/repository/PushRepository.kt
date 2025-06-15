package ru.practice.homeworks.domain.repository

interface PushRepository {
    suspend fun saveBackground(data : String)
}