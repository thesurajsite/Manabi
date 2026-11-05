package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.presentation.components.MonthNavButton
import app.dev.manabi.presentation.components.StatusPill
import app.dev.manabi.presentation.screens.Productivity.toMonthTitle
import app.dev.manabi.presentation.screens.Productivity.toWeekdayFullDate
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.neutralChip
import app.dev.manabi.presentation.theme.neutralChipText
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.primaryStrong
import app.dev.manabi.presentation.theme.taskDivider
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus

private val WeekDaysShort = listOf("S", "M", "T", "W", "T", "F", "S")
private val WeekDaysLong = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

/**
 * Plan a day: a bottom sheet on mobile and a dialog on desktop ([isWide]).
 * Picking a date writes nothing; [onOpenDate] only opens it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductivityDatePicker(
    initialDate: LocalDate,
    today: LocalDate,
    days: List<ProductivityDay>,
    isWide: Boolean,
    onDismiss: () -> Unit,
    onOpenDate: (LocalDate) -> Unit,
) {
    if (isWide) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            DatePickerContent(
                initialDate = initialDate,
                today = today,
                days = days,
                isWide = true,
                onDismiss = onDismiss,
                onOpenDate = onOpenDate,
                modifier = Modifier
                    .width(420.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(cardBackground)
                    .padding(24.dp),
            )
        }
    } else {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = cardBackground,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            DatePickerContent(
                initialDate = initialDate,
                today = today,
                days = days,
                isWide = false,
                onDismiss = onDismiss,
                onOpenDate = onOpenDate,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
            )
        }
    }
}

@Composable
private fun DatePickerContent(
    initialDate: LocalDate,
    today: LocalDate,
    days: List<ProductivityDay>,
    isWide: Boolean,
    onDismiss: () -> Unit,
    onOpenDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var pickedDate by remember { mutableStateOf(initialDate) }
    var visibleMonth by remember { mutableStateOf(LocalDate(initialDate.year, initialDate.month, 1)) }
    val daysByDate = remember(days) { days.associateBy { it.date } }
    val pickedDay = daysByDate[pickedDate]

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(text = "Plan a day", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = textPrimary)
                Text(
                    text = "Pick a date to open its tasks or start a new plan.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = textMuted,
                )
            }
            if (isWide) {
                IconButton(onClick = onDismiss, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = textPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = visibleMonth.toMonthTitle(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.weight(1f),
            )
            if (isWide) {
                OutlinedButton(
                    onClick = {
                        pickedDate = today
                        visibleMonth = LocalDate(today.year, today.month, 1)
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, cardBorder),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = textPrimary),
                    modifier = Modifier.height(40.dp),
                ) {
                    Text(text = "Today", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
            MonthNavButton(
                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month",
                onClick = { visibleMonth = visibleMonth.minus(1, DateTimeUnit.MONTH) },
            )
            MonthNavButton(
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month",
                onClick = { visibleMonth = visibleMonth.plus(1, DateTimeUnit.MONTH) },
            )
        }

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

        MonthGrid(
            month = visibleMonth,
            today = today,
            pickedDate = pickedDate,
            daysByDate = daysByDate,
            isWide = isWide,
            onPick = { pickedDate = it },
        )

        HorizontalDivider(color = taskDivider)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = pickedDate.toWeekdayFullDate(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.weight(1f),
            )
            if (pickedDay != null) {
                StatusPill(
                    text = "${pickedDay.doneCount} of ${pickedDay.totalCount} done",
                    containerColor = primarySoft,
                    contentColor = primaryStrong,
                )
            } else {
                StatusPill(
                    text = "Nothing planned",
                    containerColor = neutralChip,
                    contentColor = neutralChipText,
                )
            }
        }

        val buttonHeight = if (isWide) 44.dp else 48.dp
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(containerColor = neutralChip, contentColor = textPrimary),
                modifier = Modifier.weight(1f).height(buttonHeight),
            ) {
                Text(text = "Cancel", fontSize = if (isWide) 14.sp else 15.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { onOpenDate(pickedDate) },
                shape = RoundedCornerShape(12.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(containerColor = primary),
                modifier = Modifier.weight(1f).height(buttonHeight),
            ) {
                Text(
                    text = if (pickedDay != null) "Open day" else "Start plan",
                    fontSize = if (isWide) 14.sp else 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun MonthGrid(
    month: LocalDate,
    today: LocalDate,
    pickedDate: LocalDate,
    daysByDate: Map<LocalDate, ProductivityDay>,
    isWide: Boolean,
    onPick: (LocalDate) -> Unit,
) {
    val daysInMonth = month.daysUntil(month.plus(1, DateTimeUnit.MONTH))
    // Sunday-first: Sunday = 0, Monday = 1, ...
    val leadingBlanks = month.dayOfWeek.isoDayNumber % 7
    val cells: List<LocalDate?> =
        List(leadingBlanks) { null } + (0 until daysInMonth).map { month.plus(it, DateTimeUnit.DAY) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        cells.chunked(7).forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 0 until 7) {
                    val date = week.getOrNull(i)
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
                        if (date != null) {
                            MonthDay(
                                date = date,
                                day = daysByDate[date],
                                isToday = date == today,
                                isPicked = date == pickedDate,
                                width = if (isWide) 44.dp else 40.dp,
                                onClick = { onPick(date) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthDay(
    date: LocalDate,
    day: ProductivityDay?,
    isToday: Boolean,
    isPicked: Boolean,
    width: Dp,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    val textColor = when {
        isPicked -> cardBackground
        isToday -> primary
        else -> textPrimary
    }
    val weight = when {
        isPicked || isToday -> FontWeight.ExtraBold
        day != null -> FontWeight.Bold
        else -> FontWeight.Medium
    }
    val dotColor = when {
        day == null -> Color.Transparent
        isPicked -> cardBackground
        else -> day.calendarDotColor
    }
    val description = buildString {
        append("${date.day} ${date.month.name.lowercase()}")
        if (isToday) append(", today")
        if (day != null) append(", ${day.totalCount} tasks planned")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
        modifier = Modifier
            .size(width = width, height = 44.dp)
            .clip(shape)
            .then(if (isPicked) Modifier.background(primary) else Modifier)
            .then(if (isToday && !isPicked) Modifier.border(1.5.dp, primary, shape) else Modifier)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description },
    ) {
        Text(text = date.day.toString(), fontSize = 14.sp, fontWeight = weight, color = textColor)
        Box(Modifier.size(5.dp).clip(CircleShape).background(dotColor))
    }
}
