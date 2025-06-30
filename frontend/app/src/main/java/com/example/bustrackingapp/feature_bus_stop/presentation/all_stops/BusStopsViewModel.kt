package com.example.bustrackingapp.feature_bus_stop.presentation.all_stops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bustrackingapp.core.util.Resource
import com.example.bustrackingapp.feature_bus_stop.domain.use_case.BusStopUseCases
import com.example.bustrackingapp.feature_bus_stop.presentation.all_stops.BusStopsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BusStopsViewModel @Inject constructor(
    private val busStopUseCases: BusStopUseCases
) : ViewModel() {

  // 1) UI state for the list
  private val _uiState = MutableStateFlow(BusStopsUiState(isLoading = true))
  val uiState: StateFlow<BusStopsUiState> = _uiState

  // 2) Expose the favorites Flow straight from DataStore
  val favoriteStops: Flow<Set<String>> = busStopUseCases.toggleFavorite.favorites()

  init {
    viewModelScope.launch { busStopUseCases.toggleFavorite.refresh() }
    getAllBusStops(isLoading = true)
  }

  /** Fetch all stops and mark favorites */
  fun getAllBusStops(isLoading: Boolean = false, isRefreshing: Boolean = false) {
    busStopUseCases.getAllBusStops()
      .onStart { _uiState.update { it.copy(isLoading = isLoading, error = null) } }
      .onEach { result ->
        _uiState.update { state ->
          when (result) {
            is Resource.Success -> {
              // We’ll enrich stops in the UI layer since favorites live in another Flow
              state.copy(
                busStops     = result.data ?: emptyList(),
                isLoading    = false,
                isRefreshing = false,
                error        = null
              )
            }
            is Resource.Error -> state.copy(
              error        = result.message,
              isLoading    = false,
              isRefreshing = false
            )
            is Resource.Loading -> state.copy(
              isLoading    = isLoading,
              isRefreshing = isRefreshing,
              error        = null
            )
          }
        }
      }
      .launchIn(viewModelScope)
  }

  /** Toggle a stop in favorites, then refresh the list */
  fun toggleFavorite(stopNo: String) {
    viewModelScope.launch {
      busStopUseCases.toggleFavorite(stopNo)
      getAllBusStops(isRefreshing = true)
    }
  }
}

