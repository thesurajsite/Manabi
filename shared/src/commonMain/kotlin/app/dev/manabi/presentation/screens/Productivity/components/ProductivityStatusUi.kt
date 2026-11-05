package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.ui.graphics.Color
import app.dev.manabi.domain.model.DayStatus
import app.dev.manabi.domain.model.ON_TRACK_PERCENT
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.presentation.theme.absentRed
import app.dev.manabi.presentation.theme.absentSoft
import app.dev.manabi.presentation.theme.absentText
import app.dev.manabi.presentation.theme.neutralChip
import app.dev.manabi.presentation.theme.neutralChipText
import app.dev.manabi.presentation.theme.onTrackDot
import app.dev.manabi.presentation.theme.presentSoft
import app.dev.manabi.presentation.theme.presentText
import app.dev.manabi.presentation.theme.primary

internal val DayStatus.label: String
    get() = when (this) {
        DayStatus.NOT_STARTED -> "Not started"
        DayStatus.BEHIND -> "Behind"
        DayStatus.ON_TRACK -> "On track"
        DayStatus.ALL_DONE -> "All done"
    }

internal val DayStatus.chipBackground: Color
    get() = when (this) {
        DayStatus.NOT_STARTED -> neutralChip
        DayStatus.BEHIND -> absentSoft
        DayStatus.ON_TRACK, DayStatus.ALL_DONE -> presentSoft
    }

internal val DayStatus.chipText: Color
    get() = when (this) {
        DayStatus.NOT_STARTED -> neutralChipText
        DayStatus.BEHIND -> absentText
        DayStatus.ON_TRACK, DayStatus.ALL_DONE -> presentText
    }

/** Red only for a planned day below the on-track limit. */
internal val ProductivityDay.ringColor: Color
    get() = if (status == DayStatus.BEHIND) absentRed else primary

internal val ProductivityDay.calendarDotColor: Color
    get() = if (percentage < ON_TRACK_PERCENT) absentRed else onTrackDot

internal fun barColor(percentage: Int): Color =
    if (percentage < ON_TRACK_PERCENT) absentRed else primary
