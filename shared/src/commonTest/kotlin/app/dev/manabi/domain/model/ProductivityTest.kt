package app.dev.manabi.domain.model

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProductivityTest {

    private fun day(date: String, done: Int, total: Int) = ProductivityDay(
        date = LocalDate.parse(date),
        tasks = List(total) { index ->
            ProductivityTask(
                id = index.toLong(),
                date = LocalDate.parse(date),
                text = "Task $index",
                isDone = index < done,
                createdAt = "",
                updatedAt = "",
            )
        },
    )

    private val today = LocalDate.parse("2026-11-02")

    @Test
    fun percentageRoundsHalfUp() {
        assertEquals(13, day("2026-11-02", done = 1, total = 8).percentage)
        assertEquals(67, day("2026-11-02", done = 2, total = 3).percentage)
    }

    @Test
    fun dayWithNoTasksIsZeroPercent() {
        assertEquals(0, day("2026-11-02", done = 0, total = 0).percentage)
    }

    @Test
    fun statusFollowsTheSixtyPercentLimit() {
        assertEquals(DayStatus.NOT_STARTED, day("2026-11-02", done = 0, total = 0).status)
        assertEquals(DayStatus.BEHIND, day("2026-11-02", done = 1, total = 2).status)
        assertEquals(DayStatus.ON_TRACK, day("2026-11-02", done = 3, total = 5).status)
        assertEquals(DayStatus.ALL_DONE, day("2026-11-02", done = 4, total = 4).status)
    }

    @Test
    fun summaryMatchesTheRulesExample() {
        val days = listOf(
            day("2026-11-02", done = 3, total = 4),
            day("2026-10-31", done = 3, total = 4),
            day("2026-10-30", done = 5, total = 7),
            day("2026-10-29", done = 5, total = 6),
            day("2026-10-28", done = 4, total = 5),
            day("2026-10-27", done = 2, total = 4),
        )

        val summary = buildProductivitySummary(days, today)

        assertEquals(72, summary.average)
        assertEquals(LocalDate.parse("2026-10-29"), summary.bestDay?.date)
        assertEquals(listOf(50, 80, 83, 71, 75, 0, 75), summary.bars.map { it.percentage })
    }

    @Test
    fun barsAreSevenContinuousDaysEndingToday() {
        val bars = buildProductivitySummary(emptyList(), today).bars

        assertEquals(7, bars.size)
        assertEquals(LocalDate.parse("2026-10-27"), bars.first().date)
        assertEquals(today, bars.last().date)
    }

    @Test
    fun noPlannedDaysGiveZeroAndNoBestDay() {
        val summary = buildProductivitySummary(emptyList(), today)

        assertEquals(0, summary.average)
        assertNull(summary.bestDay)
    }

    @Test
    fun onlyOneLowestDayIsDropped() {
        // Five unplanned days tie for lowest, so four of them still count: 200 / 6 = 33.
        val days = listOf(
            day("2026-11-02", done = 1, total = 1),
            day("2026-11-01", done = 1, total = 1),
        )

        assertEquals(33, buildProductivitySummary(days, today).average)
    }

    @Test
    fun daysOutsideTheWindowAreIgnored() {
        val days = listOf(
            day("2026-10-26", done = 1, total = 1),
            day("2026-11-03", done = 1, total = 1),
        )

        val summary = buildProductivitySummary(days, today)

        assertEquals(0, summary.average)
        assertNull(summary.bestDay)
    }

    @Test
    fun earliestDateWinsABestDayTie() {
        val days = listOf(
            day("2026-11-02", done = 1, total = 1),
            day("2026-10-30", done = 2, total = 2),
        )

        assertEquals(LocalDate.parse("2026-10-30"), buildProductivitySummary(days, today).bestDay?.date)
    }

    @Test
    fun windowCrossesMonthAndYearBoundaries() {
        val bars = buildProductivitySummary(emptyList(), LocalDate.parse("2027-01-02")).bars

        assertEquals(LocalDate.parse("2026-12-27"), bars.first().date)
    }

    @Test
    fun tasksGroupIntoDaysNewestFirst() {
        val tasks = day("2026-10-30", done = 1, total = 2).tasks + day("2026-11-02", done = 0, total = 1).tasks

        val days = toProductivityDays(tasks)

        assertEquals(listOf("2026-11-02", "2026-10-30"), days.map { it.date.toString() })
        assertEquals(2, days.last().totalCount)
    }
}
