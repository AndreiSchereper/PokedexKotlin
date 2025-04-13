package nl.schereper.andrei.pokedex.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun VerticalScrollbar(
    listState: LazyGridState,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.LightGray.copy(alpha = 0.3f),
    thumbColor: Color = Color.DarkGray.copy(alpha = 0.9f)
) {
    val totalItems = listState.layoutInfo.totalItemsCount
    val visibleItems = listState.layoutInfo.visibleItemsInfo

    if (totalItems == 0 || visibleItems.isEmpty()) return

    val firstVisibleIndex = visibleItems.first().index
    val scrollProgress = firstVisibleIndex.toFloat() / totalItems
    val visibleRatio = visibleItems.size.toFloat() / totalItems

    val trackHeight = 300.dp // fixed height that looks good
    val thumbHeight = (visibleRatio * 300).dp.coerceAtLeast(32.dp)
    val thumbOffset = (scrollProgress * (300 - thumbHeight.value)).dp

    Box(
        modifier = modifier
            .height(trackHeight)
            .width(8.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        // Scroll track
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(trackColor)
        )

        // Scroll thumb
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(thumbHeight)
                .offset { IntOffset(0, thumbOffset.roundToPx()) }
                .clip(RoundedCornerShape(4.dp))
                .background(thumbColor)
        )
    }
}