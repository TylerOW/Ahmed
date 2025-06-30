package com.example.bustrackingapp.feature_bus_routes.presentation.bus_routes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bustrackingapp.core.presentation.components.CustomLoadingIndicator
import com.example.bustrackingapp.core.util.LoggerUtil
import com.example.bustrackingapp.feature_bus_routes.domain.models.BusRouteWithStops
import com.example.bustrackingapp.feature_bus_routes.presentation.components.BusRouteTile
import com.example.bustrackingapp.feature_bus_routes.presentation.components.ZoomableShuttleMap
import com.example.bustrackingapp.ui.theme.Blue500
import com.example.bustrackingapp.ui.theme.NavyBlue300
import com.example.bustrackingapp.ui.theme.Red400
import com.example.bustrackingapp.ui.theme.White
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusRoutesScreen(
    busRoutesViewModel : BusRoutesViewModel = hiltViewModel(),
    snackbarState : SnackbarHostState = remember {
        SnackbarHostState()
    },
    onRouteItemClick : (String)->Unit
){
    val focusManager = LocalFocusManager.current
    val logger = LoggerUtil(c = "BusRoutesScreen")
    LaunchedEffect(key1 = busRoutesViewModel.uiState.error){
        logger.info("Show Snackbar")

        if(busRoutesViewModel.uiState.error!=null){
            snackbarState.showSnackbar(busRoutesViewModel.uiState.error!!)
        }
    }

    Scaffold(
        modifier = Modifier
            .clickable(
                // Remove Ripple Effect
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "UNITEN Internal Shuttle Service",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
            ){
                if(busRoutesViewModel.uiState.error!=null){
                    Snackbar(
                        snackbarData = it,
                        containerColor = Red400,
                        contentColor = White,
                    )
                }else{
                    Snackbar(snackbarData = it)
                }
            }
        },

        ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            BusRouteList(
                busRoutes = { busRoutesViewModel.uiState.busRoutes },
                isLoading = { busRoutesViewModel.uiState.isLoading },
                isRefreshing = { busRoutesViewModel.uiState.isRefreshing },
                onRefresh = busRoutesViewModel::getAllBusRoutes,
                onRouteItemClick = onRouteItemClick
            )
        }
    }
}

@Composable
private fun ShuttleRulesInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Service Hours:",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "Monday – Friday, 07:30 – 22:30",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Route:",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "ATM/Library → Admin → Murni → BW → Amanah → DSS → ILMU → COE → ATM/Library",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Departure:",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "Every 30 minutes",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Exceptions (No Service During):",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "• 12:30 – 14:30 (Friday Prayer)",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "• 19:30 – 20:00 (Maghrib Prayer)",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "• Semester Breaks",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "The shuttle service is completely free of charge.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun BusRouteList(
    busRoutes: () -> List<BusRouteWithStops>,
    isLoading: () -> Boolean,
    isRefreshing: () -> Boolean,
    onRefresh: (isLoading: Boolean, isRefreshing: Boolean) -> Unit,
    onRouteItemClick: (String) -> Unit
){
    if(isLoading()){
        return CustomLoadingIndicator()
    }

    Column {
        ShuttleRulesInfo(modifier = Modifier.padding(horizontal = 8.dp))
        ZoomableShuttleMap(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally)
        )

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing()),
            onRefresh = { onRefresh(false, true) },
        ) {
            LazyColumn(
                content = {
                    itemsIndexed(busRoutes()) { index, item ->
                        if (index == 0) {
                            Divider(color = NavyBlue300)
                        }
                        BusRouteTile(
                            routeNo = item.routeNo,
                            routeName = item.name,
                            totalStops = item.stops.size,
                            onClick = { onRouteItemClick(item.routeNo) }
                        )
                        Divider(color = NavyBlue300)
                    }
                },
                contentPadding = PaddingValues(8.dp)
            )
        }
    }
}
