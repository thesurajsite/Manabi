package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.presentation.components.CircularProgressBar
import app.dev.manabi.presentation.components.StatusPill
import app.dev.manabi.presentation.screens.Productivity.toShortDate
import app.dev.manabi.presentation.screens.Productivity.toWeekdayLabel
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.primaryStrong
import app.dev.manabi.presentation.theme.primaryTint
import app.dev.manabi.presentation.theme.primaryTintBorder
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

/** One planned day in Recent days. [isHighlighted] tints today on mobile and the open day on desktop. */
@Composable
fun ProductivityDayCard(
    day: ProductivityDay,
    isToday: Boolean,
    isHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (isHighlighted) primaryTint else cardBackground)
            .border(1.dp, if (isHighlighted) primaryTintBorder else cardBorder, shape)
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            CircularProgressBar(
                percent = day.percentage,
                text = "${day.percentage}%",
                size = 56.dp,
                strokeWidth = 6.dp,
                color = day.ringColor,
                trackColor = primarySoft,
                textColor = textPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(Modifier.weight(1f))
            if (isToday) {
                StatusPill(
                    text = "Today",
                    containerColor = primarySoft,
                    contentColor = primaryStrong,
                    modifier = Modifier.align(Alignment.Top),
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = day.date.toWeekdayLabel(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.4.sp,
                color = textMuted,
            )
            Text(
                text = day.date.toShortDate(),
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textPrimary,
            )
            Text(
                text = "${day.doneCount} of ${day.totalCount} done",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textMuted,
            )
        }
    }
}
