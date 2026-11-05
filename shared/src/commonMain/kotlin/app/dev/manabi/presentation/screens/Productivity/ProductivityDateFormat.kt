package app.dev.manabi.presentation.screens.Productivity

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

private val ShortDate = LocalDate.Format {
    day(Padding.NONE)
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
}

private val WeekdayShortDate = LocalDate.Format {
    dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
    chars(", ")
    day(Padding.NONE)
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
}

private val WeekdayFullDate = LocalDate.Format {
    dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
    chars(", ")
    day(Padding.NONE)
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
}

private val DayTitle = LocalDate.Format {
    dayOfWeek(DayOfWeekNames.ENGLISH_FULL)
    chars(", ")
    day(Padding.NONE)
    char(' ')
    monthName(MonthNames.ENGLISH_ABBREVIATED)
    char(' ')
    year()
}

private val MonthTitle = LocalDate.Format {
    monthName(MonthNames.ENGLISH_FULL)
    char(' ')
    year()
}

/** `2 Nov` */
internal fun LocalDate.toShortDate(): String = format(ShortDate)

/** `MON` */
internal fun LocalDate.toWeekdayLabel(): String =
    DayOfWeekNames.ENGLISH_ABBREVIATED.names[dayOfWeek.ordinal].uppercase()

/** `M`, used under the bars. */
internal fun LocalDate.toWeekdayLetter(): String =
    DayOfWeekNames.ENGLISH_ABBREVIATED.names[dayOfWeek.ordinal].take(1)

/** `Thu, 29 Oct` */
internal fun LocalDate.toWeekdayShortDate(): String = format(WeekdayShortDate)

/** `Thursday, 29 Oct` */
internal fun LocalDate.toWeekdayFullDate(): String = format(WeekdayFullDate)

/** `Monday, 2 Nov 2026` */
internal fun LocalDate.toDayTitle(): String = format(DayTitle)

/** `November 2026` */
internal fun LocalDate.toMonthTitle(): String = format(MonthTitle)
