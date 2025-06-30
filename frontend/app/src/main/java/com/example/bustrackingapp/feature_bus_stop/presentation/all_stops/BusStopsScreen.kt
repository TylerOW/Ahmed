package com.example.bustrackingapp.feature_bus_stop.presentation.all_stops

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlin.collections.emptySet
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bustrackingapp.core.presentation.components.CustomLoadingIndicator
import com.example.bustrackingapp.core.presentation.components.RefreshContainer
import com.example.bustrackingapp.core.util.LoggerUtil
import com.example.bustrackingapp.feature_bus_stop.domain.model.BusStopWithRoutes
import com.example.bustrackingapp.feature_bus_stop.presentation.components.BusStopTile
import com.example.bustrackingapp.ui.theme.NavyBlue300
import com.example.bustrackingapp.ui.theme.Red400
import com.example.bustrackingapp.ui.theme.White
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusStopsScreen(
    busStopsViewModel: BusStopsViewModel = hiltViewModel(),
    snackbarState: SnackbarHostState = remember { SnackbarHostState() },
    onStopItemClick: (String) -> Unit
) {
    val logger = LoggerUtil(c = "BusStopsScreen")

    // collect UI state and favorites from ViewModel
    val uiState by busStopsViewModel.uiState.collectAsState()
    val favorites by busStopsViewModel.favoriteStops.collectAsState(initial = emptySet())

    LaunchedEffect(key1 = uiState.error) {
        logger.info("Show Snackbar")
        uiState.error?.let { snackbarState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Bus Stops",
                    style = MaterialTheme.typography.headlineSmall
                )
            })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (uiState.error != null) Red400 else MaterialTheme.colorScheme.surface,
                    contentColor = if (uiState.error != null) White else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
               BusStopList(
      busStops         = uiState.busStops,
      isLoading        = uiState.isLoading,
      isRefreshing     = uiState.isRefreshing,
      onRefresh        = busStopsViewModel::getAllBusStops,
      favorites        = favorites,
      onFavoriteClick  = busStopsViewModel::toggleFavorite,
      onStopItemClick  = onStopItemClick
            )
        }
    }
}

@Composable
private fun BusStopList(
    busStops: List<BusStopWithRoutes>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: (Boolean, Boolean) -> Unit,
    favorites: Set<String>,
    onFavoriteClick: (String) -> Unit,
    onStopItemClick: (String) -> Unit
) {
    if (isLoading) {
        CustomLoadingIndicator()
        return
    }

    if (busStops.isEmpty()) {
        RefreshContainer(
            modifier = Modifier.fillMaxHeight(0.8f),
            message = "No Bus Stops Found!",
            onRefresh = { onRefresh(false, true) }
        )
        return
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh(false, true) }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(8.dp)
        ) {
            itemsIndexed(busStops) { index, item ->
                if (index == 0) {
                    Divider(color = NavyBlue300)
                }
                BusStopTile(
                    stopNo = item.stopNo,
                    stopName = item.name,
                    isFavorite = favorites.contains(item.stopNo),
                    onFavoriteClick = onFavoriteClick,
                    onClick = { onStopItemClick(item.stopNo) }
                )
                Divider(color = NavyBlue300)
            }
        }
    }
}

