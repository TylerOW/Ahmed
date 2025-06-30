package com.example.bustrackingapp.feature_bus_stop.presentation.stop_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bustrackingapp.core.util.Resource
import com.example.bustrackingapp.feature_bus.domain.use_cases.GetBusesForStopUseCase
import com.example.bustrackingapp.feature_bus_stop.domain.use_case.GetBusStopUseCase
import com.example.bustrackingapp.feature_bus_stop.domain.use_case.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StopDetailsViewModel @Inject constructor(
    private val getBusStopUseCase: GetBusStopUseCase,
    private val getBusesForStopUseCase: GetBusesForStopUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel(){
    var uiState by mutableStateOf(StopDetailsUiState())
        private set

    private val favoriteStops = toggleFavoriteUseCase.favorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    init {
        // Observe favorites so the UI updates when they change
        viewModelScope.launch { toggleFavoriteUseCase.refresh() }
        viewModelScope.launch {
            favoriteStops.collect { favs ->
                uiState.stop?.stopNo?.let { no ->
                    val fav = favs.contains(no)
                    if (uiState.isFavorite != fav) {
                        uiState = uiState.copy(isFavorite = fav)
                    }
                }
            }
        }
    }

    fun getBusStopDetails(stopNo : String, isLoading : Boolean = false, isRefreshing : Boolean = false){
        if(uiState.isLoading || uiState.isRefreshing){
            return
        }
        getBusStopUseCase(stopNo).onEach { result ->
            uiState = when (result) {
                is Resource.Success -> {
                    val fav = favoriteStops.value.contains(result.data?.stopNo)
                    uiState.copy(
                        stop = result.data,
                        isLoading = false,
                        isRefreshing = false,
                        error = null,
                        isFavorite = fav
                    )
                }
                is Resource.Error -> uiState.copy(
                    error = result.message,
                    isLoading = false,
                    isRefreshing = false
                )
                is Resource.Loading -> uiState.copy(
                    isLoading = isLoading,
                    isRefreshing = isRefreshing,
                    error = null
                )
            }
        }
            .launchIn(viewModelScope)
    }

    fun toggleFavorite() {
        val stopNo = uiState.stop?.stopNo ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(stopNo)
        }
    }
}