package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.screens.Productivity.ProductivityUiState
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.textPrimary
import kotlinx.datetime.LocalDate

/** Left desktop pane: title, Plan a day, the Best 6 days Average and Recent days. */
@Composable
fun ProductivityListDesktopPane(
    uiState: ProductivityUiState,
    activeDate: LocalDate,
    onPlanDay: () -> Unit,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxSize()
            .background(cardBackground)
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Productivity",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textPrimary,
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = onPlanDay,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                modifier = Modifier.height(40.dp),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(text = "Plan a day", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        ProductivitySummaryPanel(summary = uiState.summary, today = uiState.today)

        RecentDaysHeader(hint = "Click a day to open it")

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f),
        ) {
            if (uiState.days.isEmpty() && !uiState.isLoading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    NoPlannedDays()
                }
            }
            items(items = uiState.days, key = { it.date.toString() }) { day ->
                ProductivityDayCard(
                    day = day,
                    isToday = day.date == uiState.today,
                    isHighlighted = day.date == activeDate,
                    onClick = { onDayClick(day.date) },
                )
            }
        }
    }
}
