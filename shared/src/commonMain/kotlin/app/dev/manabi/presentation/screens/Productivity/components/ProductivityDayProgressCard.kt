package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.DayStatus
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.presentation.components.CircularProgressBar
import app.dev.manabi.presentation.components.SectionCard
import app.dev.manabi.presentation.components.StatusPill
import app.dev.manabi.presentation.theme.doneStatBackground
import app.dev.manabi.presentation.theme.leftStatBackground
import app.dev.manabi.presentation.theme.leftStatText
import app.dev.manabi.presentation.theme.neutralSoft
import app.dev.manabi.presentation.theme.presentText
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

/** Ring, status chip and headline for one day. [isWide] (desktop) adds the Done, Left and Planned tiles. */
@Composable
fun ProductivityDayProgressCard(
    day: ProductivityDay,
    isWide: Boolean,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier, spacing = 16.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressBar(
                percent = day.percentage,
                text = "${day.percentage}%",
                size = 88.dp,
                strokeWidth = 8.dp,
                color = day.ringColor,
                trackColor = primarySoft,
                textColor = textPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                StatusPill(
                    text = day.status.label,
                    containerColor = day.status.chipBackground,
                    contentColor = day.status.chipText,
                )
                Text(
                    text = day.headline(isWide),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textPrimary,
                )
            }
        }

        if (isWide) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DayStatTile(
                    value = day.doneCount,
                    label = "Done",
                    background = doneStatBackground,
                    valueColor = presentText,
                    labelColor = presentText,
                    modifier = Modifier.weight(1f),
                )
                DayStatTile(
                    value = day.leftCount,
                    label = "Left",
                    background = leftStatBackground,
                    valueColor = leftStatText,
                    labelColor = leftStatText,
                    modifier = Modifier.weight(1f),
                )
                DayStatTile(
                    value = day.totalCount,
                    label = "Planned",
                    background = neutralSoft,
                    valueColor = textPrimary,
                    labelColor = textMuted,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

private fun ProductivityDay.headline(isWide: Boolean): String = when (status) {
    DayStatus.NOT_STARTED -> "Add your first task for this day"
    DayStatus.ALL_DONE -> "Every task is done. Nice work"
    DayStatus.BEHIND, DayStatus.ON_TRACK ->
        if (isWide) {
            "$leftCount of $totalCount tasks left for this day"
        } else {
            "$doneCount of $totalCount done, $leftCount left"
        }
}

@Composable
private fun DayStatTile(
    value: Int,
    label: String,
    background: Color,
    valueColor: Color,
    labelColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 10.dp),
    ) {
        Text(text = value.toString(), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = valueColor)
        Text(
            text = label,
            fontSize = 12.sp,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
            softWrap = false,
        )
    }
}
