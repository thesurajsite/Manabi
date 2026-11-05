package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.ON_TRACK_PERCENT
import app.dev.manabi.domain.model.ProductivityBar
import app.dev.manabi.domain.model.ProductivitySummary
import app.dev.manabi.presentation.components.CircularProgressBar
import app.dev.manabi.presentation.components.SectionCard
import app.dev.manabi.presentation.components.StatusPill
import app.dev.manabi.presentation.screens.Productivity.toWeekdayLetter
import app.dev.manabi.presentation.screens.Productivity.toWeekdayShortDate
import app.dev.manabi.presentation.theme.absentRed
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.neutralSoft
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.primaryStrong
import app.dev.manabi.presentation.theme.screenBackground
import app.dev.manabi.presentation.theme.summaryRingTrack
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary
import kotlinx.datetime.LocalDate

private const val NoPlansText = "No plans in the last 7 days"
private const val MinimumBarPercent = 6

/** Best 6 days Average: a ring, the best day and the last 7 days as bars. */
@Composable
fun ProductivitySummaryCard(
    summary: ProductivitySummary,
    today: LocalDate,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier, spacing = 16.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressBar(
                percent = summary.average,
                text = "${summary.average}%",
                size = 88.dp,
                strokeWidth = 8.dp,
                color = summary.ringColor,
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
                    text = "Best 6 days Average",
                    containerColor = primarySoft,
                    contentColor = primaryStrong,
                )
                val bestDay = summary.bestDay
                if (bestDay != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(text = "Best day", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textMuted)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = "${bestDay.percentage}%",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = textPrimary,
                            )
                            Text(
                                text = bestDay.date.toWeekdayShortDate(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textMuted,
                            )
                        }
                    }
                } else {
                    Text(
                        text = NoPlansText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textPrimary,
                    )
                }
            }
        }
        ProductivityWeekBars(bars = summary.bars, today = today, trackColor = neutralSoft)
    }
}

/** The compact summary shown at the top of the desktop list pane. */
@Composable
fun ProductivitySummaryPanel(
    summary: ProductivitySummary,
    today: LocalDate,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(screenBackground)
            .padding(14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            CircularProgressBar(
                percent = summary.average,
                text = "${summary.average}%",
                size = 52.dp,
                strokeWidth = 6.dp,
                color = summary.ringColor,
                trackColor = summaryRingTrack,
                textColor = textPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "BEST 6 DAYS AVERAGE",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.4.sp,
                    color = textMuted,
                )
                val bestDay = summary.bestDay
                if (bestDay != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(text = "Best day", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textMuted)
                        Text(
                            text = "${bestDay.percentage}%",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = textPrimary,
                        )
                        Text(
                            text = bestDay.date.toWeekdayShortDate(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textMuted,
                        )
                    }
                } else {
                    Text(text = NoPlansText, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                }
            }
        }
        ProductivityWeekBars(bars = summary.bars, today = today, trackColor = cardBackground)
    }
}

private val ProductivitySummary.ringColor: Color
    get() = if (bestDay != null && average < ON_TRACK_PERCENT) absentRed else primary

@Composable
private fun ProductivityWeekBars(
    bars: List<ProductivityBar>,
    today: LocalDate,
    trackColor: Color,
) {
    val description = bars.joinToString(prefix = "Daily completion for the last 7 days: ") { "${it.percentage}%" }

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clearAndSetSemantics { contentDescription = description },
    ) {
        bars.forEach { bar ->
            val isToday = bar.date == today
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "${bar.percentage}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textMuted,
                    maxLines = 1,
                )
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .widthIn(max = 28.dp)
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(trackColor),
                ) {
                    if (bar.isPlanned) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(maxOf(bar.percentage, MinimumBarPercent) / 100f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(barColor(bar.percentage)),
                        )
                    }
                }
                Text(
                    text = bar.date.toWeekdayLetter(),
                    fontSize = 12.sp,
                    fontWeight = if (isToday) FontWeight.ExtraBold else FontWeight.SemiBold,
                    color = if (isToday) primary else textMuted,
                )
            }
        }
    }
}
