package app.dev.manabi.presentation.screens.Productivity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.presentation.screens.Productivity.components.NoPlannedDays
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityCalendarButton
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityDatePicker
import app.dev.manabi.presentation.screens.Productivity.components.ProductivityDayCard
import app.dev.manabi.presentation.screens.Productivity.components.ProductivitySummaryCard
import app.dev.manabi.presentation.screens.Productivity.components.RecentDaysHeader
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.screenBackground
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductivityMobileScreen(
    onNavigateToDay: (LocalDate) -> Unit,
    modifier: Modifier,
) {
    val viewModel: ProductivityViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isPickerOpen by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(screenBackground),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 14.dp, bottom = 10.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PRODUCTIVITY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp,
                        color = textMuted,
                    )
                    Text(
                        text = "Daily plans",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textPrimary,
                    )
                }
                ProductivityCalendarButton(
                    contentDescription = "Pick a date",
                    onClick = { isPickerOpen = true },
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 96.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    ProductivitySummaryCard(
                        summary = uiState.summary,
                        today = uiState.today,
                    )
                }
                item(span = { GridItemSpan(maxLineSpan) }) {
                    RecentDaysHeader(
                        hint = "Tap a day to see its tasks",
                        modifier = Modifier.padding(top = 4.dp, start = 2.dp, end = 2.dp),
                    )
                }
                if (uiState.days.isEmpty() && !uiState.isLoading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        NoPlannedDays()
                    }
                }
                items(items = uiState.days, key = { it.date.toString() }) { day ->
                    ProductivityDayCard(
                        day = day,
                        isToday = day.date == uiState.today,
                        isHighlighted = day.date == uiState.today,
                        onClick = { onNavigateToDay(day.date) },
                    )
                }
            }
        }

        Button(
            onClick = { isPickerOpen = true },
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primary),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 24.dp)
                .height(56.dp),
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(8.dp))
            Text(text = "Plan a day", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }

    if (isPickerOpen) {
        ProductivityDatePicker(
            initialDate = uiState.today,
            today = uiState.today,
            days = uiState.days,
            isWide = false,
            onDismiss = { isPickerOpen = false },
            onOpenDate = { date ->
                isPickerOpen = false
                onNavigateToDay(date)
            },
        )
    }
}
