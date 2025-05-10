package ru.practice.homeworks.data.local

import javax.inject.Inject

class CatCache<T> @Inject constructor() {

    private val cacheMap = mutableMapOf<String, CacheEntry<T>>()

    private val queryCount = mutableMapOf<String, Int>()

    private val cacheTimeout = 5L

    fun getCache(query: String) : T?{

        val entry = cacheMap[query] ?: return null

        val isExpired = System.currentTimeMillis() - entry.date > cacheTimeout * 60_000

        val isOverCount = queryCount.getOrDefault(key = query, defaultValue = 0) >= 3

        if (isExpired || isOverCount){
            clearIntermediateFor(query)
            return null
        }

        clearIntermediateFor(query)

        return entry.result

    }

    fun saveToCache(query: String, result: T){
        cacheMap[query] = CacheEntry(result, System.currentTimeMillis())
    }

    private fun clearIntermediateFor(key: String) {
        queryCount.keys.forEach { k ->
            if (k != key) {
                queryCount[k] = 0
            }
        }
    }

    fun trackIntermediateRequest(key: String) {
        queryCount.forEach { (k, v) ->
            if (k != key) {
                queryCount[k] = v + 1
            }
        }
        queryCount.putIfAbsent(key, 0)
    }
}
