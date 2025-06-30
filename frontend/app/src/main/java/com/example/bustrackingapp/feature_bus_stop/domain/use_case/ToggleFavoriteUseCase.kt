package com.example.bustrackingapp.feature_bus_stop.domain.use_case

import com.example.bustrackingapp.feature_bus_stop.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repo: FavoriteRepository
) {
    suspend operator fun invoke(stopNo: String) = repo.toggle(stopNo)

    suspend fun refresh() = repo.refresh()

    fun favorites() = repo.favorites()
}
