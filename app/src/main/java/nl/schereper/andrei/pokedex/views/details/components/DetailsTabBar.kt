package nl.schereper.andrei.pokedex.views.details.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

/**
 * Re-usable three-tab bar used in the details screen.
 *
 * • [tabs]         – tab titles in display order
 * • [selected]     – currently-active index
 * • [onSelect]     – callback when user taps another tab
 * • [accent]       – colour derived from the first Pokémon type
 */
@Composable
fun DetailsTabBar(
    tabs: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    accent: Color
) {
    TabRow(
        selectedTabIndex = selected,
        containerColor   = Color.Transparent,
        contentColor     = accent,
        /* slim 3-dp indicator in accent colour */
        indicator = { positions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(positions[selected])
                    .height(3.dp),
                color = accent
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selected == index,
                onClick  = { onSelect(index) }
            ) {
                Text(
                    text        = title,
                    style       = MaterialTheme.typography.titleMedium,
                    fontWeight  = FontWeight.Bold,
                    color       = if (selected == index) accent
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier    = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}