package com.example.bustrackingapp.feature_bus_stop.data.repository

import com.example.bustrackingapp.feature_bus_stop.data.api.FavoriteApiService
import com.example.bustrackingapp.feature_bus_stop.domain.repository.FavoriteRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val apiService: FavoriteApiService,
    private val dispatcher: CoroutineDispatcher
) : FavoriteRepository {

    private val favorites = MutableStateFlow<Set<String>>(emptySet())

    override fun favorites() = favorites.asStateFlow()

    override suspend fun refresh() = withContext(dispatcher) {
        try {
            val response = apiService.getFavorites()
            favorites.value = response.data.toSet()
        } catch (_: Exception) {
            // ignore errors and keep previous state
        }
    }

    override suspend fun toggle(stopNo: String) = withContext(dispatcher) {
        val current = favorites.value
        try {
            if (current.contains(stopNo)) {
                apiService.removeFavorite(stopNo)
                favorites.value = current - stopNo
            } else {
                apiService.addFavorite(mapOf("stopNo" to stopNo))
                favorites.value = current + stopNo
            }
        } catch (_: Exception) {
            // ignore network errors
        }
    }
}
