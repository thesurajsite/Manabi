package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.components.SectionCard
import app.dev.manabi.presentation.theme.absentFill
import app.dev.manabi.presentation.theme.absentText
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.presentFill
import app.dev.manabi.presentation.theme.presentText
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.textDisabled
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

private val WeekDaysShort = listOf("S", "M", "T", "W", "T", "F", "S")
private val WeekDaysLong = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

/**
 * "Attendance log" card: a month calendar that marks each day from [days] (keys in dd-MM-yyyy):
 * green for present (1), red for absent (0), nothing if not in the map.
 *
 * When [onStatusChange] is set, tapping a day opens a [DayActionPanel] below the grid;
 * it is called with the date key and the new status (1, 0, or null to remove the entry).
 * When it is null (unsaved subject), days can't be tapped.
 *
 * [isWide] (desktop) uses bigger days and longer weekday labels.
 */
@Composable
fun AttendanceCalendar(
    days: Map<String, Int>,
    modifier: Modifier = Modifier,
    isWide: Boolean = false,
    onStatusChange: ((date: String, status: Int?) -> Unit)? = null,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    var visibleMonth by remember { mutableStateOf(LocalDate(today.year, today.month, 1)) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val daysInMonth = visibleMonth.daysUntil(visibleMonth.plus(1, DateTimeUnit.MONTH))
    // Sunday-first: Sunday = 0, Monday = 1, ...
    val leadingBlanks = visibleMonth.dayOfWeek.isoDayNumber % 7
    val cells: List<LocalDate?> =
        List(leadingBlanks) { null } + (0 until daysInMonth).map { visibleMonth.plus(it, DateTimeUnit.DAY) }

    SectionCard(modifier = modifier, spacing = if (isWide) 14.dp else 12.dp) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "Attendance log",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
            )
            Text(
                text = if (onStatusChange != null) {
                    "Tap a date to mark it. Date changes save instantly."
                } else {
                    "Today is marked present. Save the subject to mark other days."
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = textMuted,
            )
        }

        // Month header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "${visibleMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${visibleMonth.year}",
                fontSize = if (isWide) 18.sp else 16.sp,
                fontWeight = if (isWide) FontWeight.ExtraBold else FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.weight(1f),
            )
            OutlinedButton(
                onClick = {
                    visibleMonth = LocalDate(today.year, today.month, 1)
                    selectedDate = null
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, cardBorder),
                contentPadding = PaddingValues(horizontal = 14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = textPrimary),
                modifier = Modifier.height(40.dp),
            ) {
                Text(text = "Today", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            MonthNavButton(
                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month",
                onClick = {
                    visibleMonth = visibleMonth.minus(1, DateTimeUnit.MONTH)
                    selectedDate = null
                },
            )
            MonthNavButton(
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month",
                onClick = {
                    visibleMonth = visibleMonth.plus(1, DateTimeUnit.MONTH)
                    selectedDate = null
                },
            )
        }

        // Weekday labels
        Row(modifier = Modifier.fillMaxWidth()) {
            (if (isWide) WeekDaysLong else WeekDaysShort).forEach { label ->
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Day grid
        Column(verticalArrangement = Arrangement.spacedBy(if (isWide) 8.dp else 4.dp)) {
            cells.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (i in 0 until 7) {
                        val date = week.getOrNull(i)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (date != null) {
                                CalendarDay(
                                    date = date,
                                    status = days[date.toKey()],
                                    isToday = date == today,
                                    isSelected = date == selectedDate,
                                    isEnabled = onStatusChange != null,
                                    size = if (isWide) 52.dp else 44.dp,
                                    onClick = { selectedDate = if (date == selectedDate) null else date },
                                )
                            }
                        }
                    }
                }
            }
        }

        val selected = selectedDate
        if (selected != null && onStatusChange != null) {
            DayActionPanel(
                date = selected,
                status = days[selected.toKey()],
                onStatusChange = { status ->
                    onStatusChange(selected.toKey(), status)
                    selectedDate = null
                },
            )
        }

        // Legend
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 4.dp),
        ) {
            LegendItem(label = "Present") {
                Box(Modifier.size(10.dp).clip(CircleShape).background(presentFill))
            }
            LegendItem(label = "Absent") {
                Box(Modifier.size(10.dp).clip(CircleShape).background(absentFill))
            }
            LegendItem(label = "Today") {
                Box(Modifier.size(10.dp).border(2.dp, primary, CircleShape))
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    status: Int?,
    isToday: Boolean,
    isSelected: Boolean,
    isEnabled: Boolean,
    size: Dp,
    onClick: () -> Unit,
) {
    val fill: Color? =
        when (status) {
            1 -> presentFill
            0 -> absentFill
            else -> null
        }
    val textColor =
        when {
            status == 1 -> presentText
            status == 0 -> absentText
            isToday -> primary
            isEnabled -> textPrimary
            else -> textDisabled
        }
    val statusLabel =
        when (status) {
            1 -> "present"
            0 -> "absent"
            else -> "not marked"
        }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .then(if (isSelected) Modifier.border(2.dp, textPrimary, CircleShape) else Modifier)
            .padding(3.dp)
            .clip(CircleShape)
            .then(if (fill != null) Modifier.background(fill) else Modifier)
            // Today keeps its ring even when marked, so it can always be found
            .then(if (isToday) Modifier.border(2.dp, primary, CircleShape) else Modifier)
            .clickable(enabled = isEnabled, onClick = onClick)
            .semantics { contentDescription = "${date.day} ${date.month.name.lowercase()}, $statusLabel" },
    ) {
        Text(
            text = date.day.toString(),
            fontSize = if (size > 44.dp) 15.sp else 14.sp,
            fontWeight = if (fill != null || isToday) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
        )
    }
}

@Composable
private fun MonthNavButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    OutlinedIconButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, cardBorder),
        modifier = Modifier.size(40.dp),
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription, tint = textPrimary)
    }
}

@Composable
private fun LegendItem(
    label: String,
    indicator: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        indicator()
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textMuted)
    }
}

/** Formats as dd-MM-yyyy, matching the keys stored in [app.dev.manabi.domain.model.Attendance.days]. */
private fun LocalDate.toKey(): String =
    "${day.toString().padStart(2, '0')}-${month.number.toString().padStart(2, '0')}-$year"
