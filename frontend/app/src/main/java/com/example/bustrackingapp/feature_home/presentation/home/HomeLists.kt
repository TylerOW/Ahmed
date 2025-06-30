package com.example.bustrackingapp.feature_home.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bustrackingapp.core.presentation.components.CustomLoadingIndicator
import com.example.bustrackingapp.core.presentation.components.RefreshContainer
import com.example.bustrackingapp.feature_bus.domain.models.BusWithRoute
import com.example.bustrackingapp.feature_bus_stop.domain.model.BusStopWithRoutes
import com.example.bustrackingapp.feature_bus_stop.presentation.components.BusStopTile
import com.example.bustrackingapp.feature_home.presentation.components.BusTile
import com.example.bustrackingapp.ui.theme.NavyBlue300
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NearbyBusesList(
    modifier: Modifier = Modifier,
    buses: () -> List<BusWithRoute>,
    isLoading: Boolean,
    onRefresh: (Boolean, Boolean) -> Unit,
    onBusClick: (String) -> Unit
) {
    if (isLoading) {
        CustomLoadingIndicator(modifier = modifier)
        return
    }
    if (buses().isEmpty()) {
        RefreshContainer(
            modifier = Modifier.fillMaxHeight(0.3f),
            message = "No Nearby Buses Found!",
            onRefresh = { onRefresh(false, true) }
        )
        return
    }
    LazyRow(contentPadding = PaddingValues(8.dp)) {
        items(buses()) { bus ->
            BusTile(
                routeNo = bus.route.routeNo,
                routeName = bus.route.name,
                vehNo   = bus.vehNo,
                onClick = { onBusClick(bus.vehNo) }
            )
            Spacer(modifier = Modifier.width(14.dp))
        }
    }
}

@Composable
fun NearbyBusStopsList(
    modifier: Modifier = Modifier,
    busStops: () -> List<BusStopWithRoutes>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: (Boolean, Boolean) -> Unit,
    onBusStopClick: (String) -> Unit
) {
    if (isLoading) {
        CustomLoadingIndicator(modifier = modifier)
        return
    }
    if (busStops().isEmpty()) {
        RefreshContainer(
            modifier = Modifier.fillMaxHeight(0.4f),
            message = "No Nearby Bus Stops Found!",
            onRefresh = { onRefresh(false, true) }
        )
        return
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { onRefresh(false, true) }
    ) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(busStops()) { stop ->
                BusStopTile(
                    stopNo         = stop.stopNo,
                    stopName       = stop.name,
                    isFavorite     = false,
                    onFavoriteClick = {},
                    onClick        = { onBusStopClick(stop.stopNo) },
                    showFavoriteIcon = false
                )
                Divider(color = NavyBlue300)
            }
        }
    }
}

