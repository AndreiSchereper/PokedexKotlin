package nl.schereper.andrei.pokedex.views.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import androidx.compose.ui.unit.IntOffset

@Composable
fun VerticalScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.LightGray.copy(alpha = 0.3f),
    thumbColor: Color = Color.DarkGray.copy(alpha = 0.8f)
) {
    val totalItems = listState.layoutInfo.totalItemsCount
    val visibleItems = listState.layoutInfo.visibleItemsInfo
    if (totalItems == 0 || visibleItems.isEmpty()) return

    val firstVisibleIndex = visibleItems.first().index

    // Proportion of list visible
    val visibleRatio = visibleItems.size.toFloat() / totalItems
    val scrollProgress = firstVisibleIndex.toFloat() / totalItems

    // Ensure the thumb is always at least 32dp tall
    val minThumbHeight = 32.dp
    val calculatedThumbHeight = (visibleRatio * 300).dp // scale it up
    val thumbHeight = maxOf(calculatedThumbHeight, minThumbHeight)

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(8.dp)
            .padding(end = 4.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(trackColor, shape = RoundedCornerShape(4.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(thumbHeight)
                .offset {
                    val trackHeight = 300.dp.toPx() - thumbHeight.toPx()
                    IntOffset(x = 0, y = (scrollProgress * trackHeight).roundToInt())
                }
                .background(thumbColor, shape = RoundedCornerShape(4.dp))
        )
    }
}