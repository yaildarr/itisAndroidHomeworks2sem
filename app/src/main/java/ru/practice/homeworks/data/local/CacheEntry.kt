package ru.practice.homeworks.data.local


data class CacheEntry<T>(
    val result : T,
    val date : Long
)