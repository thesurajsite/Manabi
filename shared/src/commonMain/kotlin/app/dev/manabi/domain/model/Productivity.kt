package app.dev.manabi.domain.model

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlin.math.roundToInt

// The rules behind these models are in presentation/screens/Productivity/PRODUCTIVITY_RULES.md

const val ON_TRACK_PERCENT = 60
private const val WINDOW_DAYS = 7
private const val COUNTED_DAYS = 6

data class ProductivityTask(
    val id: Long,
    val date: LocalDate,
    val text: String,
    val isDone: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

enum class DayStatus { NOT_STARTED, BEHIND, ON_TRACK, ALL_DONE }

data class ProductivityDay(
    val date: LocalDate,
    val tasks: List<ProductivityTask>,
) {
    val totalCount: Int get() = tasks.size
    val doneCount: Int get() = tasks.count { it.isDone }
    val leftCount: Int get() = totalCount - doneCount

    val percentage: Int
        get() = if (totalCount == 0) 0 else (doneCount * 100.0 / totalCount).roundToInt()

    val status: DayStatus
        get() = when {
            totalCount == 0 -> DayStatus.NOT_STARTED
            doneCount == totalCount -> DayStatus.ALL_DONE
            percentage >= ON_TRACK_PERCENT -> DayStatus.ON_TRACK
            else -> DayStatus.BEHIND
        }
}

data class ProductivityBar(
    val date: LocalDate,
    val percentage: Int,
    val isPlanned: Boolean,
)

data class ProductivitySummary(
    val average: Int,
    val bars: List<ProductivityBar>,
    val bestDay: ProductivityDay?,
)

/** Groups [tasks] into planned days, newest date first. */
fun toProductivityDays(tasks: List<ProductivityTask>): List<ProductivityDay> =
    tasks.groupBy { it.date }
        .map { (date, dayTasks) -> ProductivityDay(date, dayTasks) }
        .sortedByDescending { it.date }

/** Builds the Best 6 days Average for the 7 days ending on [today]. */
fun buildProductivitySummary(days: List<ProductivityDay>, today: LocalDate): ProductivitySummary {
    val daysByDate = days.associateBy { it.date }
    val bars = (WINDOW_DAYS - 1 downTo 0).map { daysAgo ->
        val date = today.minus(daysAgo, DateTimeUnit.DAY)
        val day = daysByDate[date]
        ProductivityBar(date = date, percentage = day?.percentage ?: 0, isPlanned = day != null)
    }

    val percentages = bars.map { it.percentage }
    val average = ((percentages.sum() - percentages.min()) / COUNTED_DAYS.toDouble()).roundToInt()

    // maxByOrNull keeps the first match, so the earliest date wins a tie.
    val bestDate = bars.filter { it.isPlanned }.maxByOrNull { it.percentage }?.date

    return ProductivitySummary(average = average, bars = bars, bestDay = daysByDate[bestDate])
}
