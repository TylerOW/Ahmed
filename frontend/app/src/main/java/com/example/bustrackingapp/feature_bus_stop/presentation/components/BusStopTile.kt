package com.example.bustrackingapp.feature_bus_stop.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bustrackingapp.R
import com.example.bustrackingapp.core.presentation.components.CustomImage
import com.example.bustrackingapp.ui.theme.Gray100
import com.example.bustrackingapp.ui.theme.NavyBlue500

@Composable
fun BusStopTile(
    modifier: Modifier = Modifier,
    stopNo: String,
    stopName: String,
    isFavorite: Boolean,
    onFavoriteClick: (String) -> Unit,
    onClick: () -> Unit,
    /**
     * Whether to show the favorite heart icon. The home screen hides the icon
     * while other screens display it.
     */
    showFavoriteIcon: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {
        CustomImage(
            drawableId = R.drawable.bus_stop,
            color = Gray100,
            backgroundColor = NavyBlue500,
            size = 54f
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stopName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stopNo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        if (showFavoriteIcon) {
            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Unfavorite stop" else "Favorite stop",
                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onFavoriteClick(stopNo) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BusStopTilePreview() {
    BusStopTile(
        stopNo = "UNITEN_A1",
        stopName = "Administration Building",
        isFavorite = true,
        onFavoriteClick = {},
        onClick = {}
    )
}
