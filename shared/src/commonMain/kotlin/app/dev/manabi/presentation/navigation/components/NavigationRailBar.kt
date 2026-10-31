package app.dev.manabi.presentation.navigation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.navigation.NavigationItem
import app.dev.manabi.presentation.navigation.Screen
import app.dev.manabi.presentation.navigation.navigationItems
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.textMuted
import manabi.shared.generated.resources.Res
import manabi.shared.generated.resources.manabi_logo
import org.jetbrains.compose.resources.painterResource

/** Desktop side navigation: app mark on top, then an icon + label item per main screen. */
@Composable
fun NavigationRailBar(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(cardBackground)
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AppMark(modifier = Modifier.padding(bottom = 16.dp))

            navigationItems.forEach { item ->
                RailItem(
                    item = item,
                    selected = currentScreen == item.screen,
                    onClick = { onNavigate(item.screen) },
                )
            }
        }
        VerticalDivider(color = cardBorder)
    }
}

@Composable
private fun AppMark(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.manabi_logo),
        contentDescription = "Manabi",
        modifier = modifier.size(44.dp),
    )
}

@Composable
private fun RailItem(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val contentColor = if (selected) primary else textMuted

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(14.dp))
            .then(if (selected) Modifier.background(primarySoft) else Modifier)
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(vertical = 10.dp),
    ) {
        Image(
            painter = painterResource(if (selected) item.selectedIcon else item.unselectedIcon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(contentColor),
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = item.label,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            color = contentColor,
            maxLines = 1,
        )
    }
}
