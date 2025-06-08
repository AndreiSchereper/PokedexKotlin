package nl.schereper.andrei.pokedex.views.details.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset

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
        indicator        = { pos ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(pos[selected]).height(3.dp),
                color = accent
            )
        }
    ) {
        tabs.forEachIndexed { i, t ->
            Tab(selected = selected == i, onClick = { onSelect(i) }) {
                Text(
                    t,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color      = if (selected == i) accent
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier   = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}