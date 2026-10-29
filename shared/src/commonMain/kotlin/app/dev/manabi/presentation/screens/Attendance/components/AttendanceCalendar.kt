package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.theme.primary
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

private val PresentColor = Color(0xFF4CAF50)
private val AbsentColor = Color(0xFFFF5252)
private val WeekDays = listOf("S", "M", "T", "W", "T", "F", "S")

/**
 * Month calendar that marks each day from [days] (keys in dd-MM-yyyy):
 * green circle for present (1), red circle for absent (0), nothing if not in the map.
 *
 * Tapping a day opens a menu to change its status when [onStatusChange] is set;
 * it is called with the date key and the new status (1, 0, or null to remove the entry).
 */
@Composable
fun AttendanceCalendar(
    days: Map<String, Int>,
    modifier: Modifier = Modifier,
    onStatusChange: ((date: String, status: Int?) -> Unit)? = null,
) {
    val today = remember { Clock.System.todayIn(TimeZone.currentSystemDefault()) }
    var visibleMonth by remember { mutableStateOf(LocalDate(today.year, today.month, 1)) }

    val daysInMonth = visibleMonth.daysUntil(visibleMonth.plus(1, DateTimeUnit.MONTH))
    // Sunday-first: Sunday = 0, Monday = 1, ...
    val leadingBlanks = visibleMonth.dayOfWeek.isoDayNumber % 7
    val cells: List<LocalDate?> =
        List(leadingBlanks) { null } + (0 until daysInMonth).map { visibleMonth.plus(it, DateTimeUnit.DAY) }

    Column(
        modifier =
            modifier
                .border(width = 2.dp, color = primary, shape = RoundedCornerShape(18.dp))
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .padding(12.dp),
    ) {
        // Month header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${visibleMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${visibleMonth.year}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.weight(1f).padding(start = 8.dp),
            )
            IconButton(onClick = { visibleMonth = visibleMonth.minus(1, DateTimeUnit.MONTH) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous month",
                    tint = primary,
                )
            }
            IconButton(onClick = { visibleMonth = visibleMonth.plus(1, DateTimeUnit.MONTH) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next month",
                    tint = primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Weekday labels
        Row(modifier = Modifier.fillMaxWidth()) {
            WeekDays.forEach { label ->
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Day grid
        cells.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                for (i in 0 until 7) {
                    val date = week.getOrNull(i)
                    Box(
                        modifier = Modifier.weight(1f).aspectRatio(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (date != null) {
                            val key = date.toKey()
                            CalendarDay(
                                date = date,
                                status = days[key],
                                isToday = date == today,
                                onStatusChange =
                                    onStatusChange?.let { callback ->
                                        { status -> callback(key, status) }
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    date: LocalDate,
    status: Int?,
    isToday: Boolean,
    onStatusChange: ((Int?) -> Unit)?,
) {
    var showMenu by remember { mutableStateOf(false) }

    val circleColor =
        when (status) {
            1 -> PresentColor
            0 -> AbsentColor
            else -> null
        }

    Box(
        contentAlignment = Alignment.Center,
        modifier =
            Modifier
                .size(36.dp)
                .clip(CircleShape)
                .then(
                    if (onStatusChange != null) {
                        Modifier.clickable { showMenu = true }
                    } else {
                        Modifier
                    },
                )
                .then(if (circleColor != null) Modifier.background(circleColor) else Modifier)
                .then(
                    if (isToday && circleColor == null) {
                        Modifier.border(1.dp, primary, CircleShape)
                    } else {
                        Modifier
                    },
                ),
    ) {
        Text(
            text = date.day.toString(),
            fontSize = 14.sp,
            fontWeight = if (isToday) FontWeight.SemiBold else FontWeight.Normal,
            color =
                when {
                    circleColor != null -> Color.White
                    isToday -> primary
                    else -> Color.Black
                },
        )

        if (onStatusChange != null) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                // Not marked: present / absent
                // Absent: present / remove
                // Present: absent / remove
                if (status != 1) {
                    DropdownMenuItem(
                        text = { Text("Mark present", color = PresentColor) },
                        onClick = {
                            showMenu = false
                            onStatusChange(1)
                        },
                    )
                }
                if (status != 0) {
                    DropdownMenuItem(
                        text = { Text("Mark absent", color = AbsentColor) },
                        onClick = {
                            showMenu = false
                            onStatusChange(0)
                        },
                    )
                }
                if (status != null) {
                    DropdownMenuItem(
                        text = { Text("Remove entry") },
                        onClick = {
                            showMenu = false
                            onStatusChange(null)
                        },
                    )
                }
            }
        }
    }
}

/** Formats as dd-MM-yyyy, matching the keys stored in [app.dev.manabi.domain.model.Attendance.days]. */
private fun LocalDate.toKey(): String =
    "${day.toString().padStart(2, '0')}-${month.number.toString().padStart(2, '0')}-$year"
