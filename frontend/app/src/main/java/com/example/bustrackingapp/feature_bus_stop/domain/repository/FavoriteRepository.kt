package com.example.bustrackingapp.feature_bus_stop.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun favorites(): Flow<Set<String>>
    suspend fun refresh()
    suspend fun toggle(stopNo: String)
}
