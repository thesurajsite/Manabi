# Productivity rules

The source of truth for how the Productivity feature handles dates, calculations and data.
Mobile and desktop follow the same rules: only the layout differs.

Design: [Productivity canvas](https://claude.ai/artifact/ApiZqTLTHzT6TvurAAfTF5)

## Terms

| Term | Meaning |
|---|---|
| **Task** | One to-do item that belongs to one date. |
| **Planned day** | A date with at least one task. |
| **Unplanned day** | A date with no tasks. It has no card and counts as 0% in the top card. |
| **Day card** | The card for a planned day in Recent days. |
| **Window** | The last 7 continuous calendar days, ending today. |

## Date rules

1. **A date is a calendar day, not a moment in time.** Use `LocalDate` in the domain and UI.
2. **Today is the device's local date.**
   ```kotlin
   Clock.System.todayIn(TimeZone.currentSystemDefault())
   ```
3. **Store dates as `yyyy-MM-dd`.** This text sorts correctly by date in SQL.
   Do not reuse the Attendance `dd-MM-yyyy` key, because it does not sort by date.
4. **Never convert a stored date between time zones.** A task saved on 2 Nov stays on 2 Nov,
   even if the user travels.
5. **Do all date maths with `LocalDate`,** such as `today.minus(6, DateTimeUnit.DAY)`.
   Never add or subtract milliseconds, because that breaks on daylight saving changes.
6. **Never keep today for the life of the app.** Read it again when the screen opens, when the
   app resumes, and when the date changes while the app is open.
7. **Any date can be planned.** Past days stay editable because a user may forget to update
   them, and future days can be planned ahead.
8. **Calendar weeks start on Sunday** in the date picker.
9. **Bars use weekday letters** (S, M, T, W, T, F, S), oldest day on the left and today on the right.

Display formats:

| Where | Format | Example |
|---|---|---|
| Day card | `EEE` + `d MMM` | `MON` / `2 Nov` |
| Day screen title | `EEEE, d MMM yyyy` | `Monday, 2 Nov 2026` |
| Best day and picked date | `EEE, d MMM` / `EEEE, d MMM` | `Thu, 29 Oct` / `Thursday, 5 Nov` |
| Picker month | `MMMM yyyy` | `November 2026` |

## Day card rules

1. Cards are never created automatically.
2. Picking a date in **Plan a day** writes nothing. It only opens that date.
   The button says **Open day** for a planned day and **Start plan** for an unplanned day.
3. The card appears when the date gets its first task.
4. The card disappears when the date loses its last task. The user stays on the day screen,
   which shows **No tasks yet**.
5. Recent days are sorted newest date first, so a day planned ahead sits above today.
6. Until the first day is planned, Recent days shows **No days planned yet**.
7. **Delete day** removes every task on that date at once, after the user confirms. It appears on
   the day screen only while the day has tasks. Mobile then returns to the days list, and desktop
   returns to today.

There is no day table, so these rules hold automatically: a day exists only while it has task rows.

## Calculations

All percentages are calculated when needed and **never stored**.

### Day percentage

```text
dayPercentage = round(doneTasks / totalTasks * 100)
```

An unplanned day is 0%. Rounding uses `roundToInt()`, so .5 rounds up.

### Best 6 days Average (top card)

1. Take the 7 dates from `today - 6` to `today`, including both ends.
2. Get each date's day percentage, counting unplanned days as 0%.
3. Remove the single lowest value. If several tie, remove only one of them.
4. Average the other 6 and round to a whole number.

```text
average = round((sum of 7 day percentages - lowest) / 6)
```

- Always divide by 6, however many days are planned.
- Use the rounded day percentages, not raw task counts.
- Today counts even if it is not finished yet.
- New users get no special handling: days before they joined count as 0%.

Example with today as 2 Nov 2026:

| Date | Done / total | Percentage |
|---|---|---|
| 27 Oct | 2 / 4 | 50 |
| 28 Oct | 4 / 5 | 80 |
| 29 Oct | 5 / 6 | 83 |
| 30 Oct | 5 / 7 | 71 |
| 31 Oct | 3 / 4 | 75 |
| 1 Nov | unplanned | 0 (removed) |
| 2 Nov | 3 / 4 | 75 |

`(50 + 80 + 83 + 71 + 75 + 75) / 6 = 72.33`, shown as **72%**.

### Best day

The planned day with the highest percentage in the window. On a tie, use the earliest date.
If no day in the window is planned, show **No plans in the last 7 days** instead.

### Status

| Condition | Chip | Colour |
|---|---|---|
| No tasks | Not started | Neutral |
| All tasks done | All done | Green chip, accent ring |
| 60% or more | On track | Green chip, accent ring |
| Below 60% | Behind | Red chip, red ring |

The same 60% limit applies to the day rings, top card ring, bars and picker dots.
An unplanned day is never shown as red: its bar is empty and its picker date has no dot.

### Bars

A planned day's bar height is `max(percentage, 6)` so that 0% stays visible.
An unplanned day's bar height is 0. The label above each bar shows the real percentage.

## Data

Save one row per task. Days, percentages and summaries all come from those rows.

### Room entity (`database/ProductivityTaskEntity.kt`)

```kotlin
@Entity(
    tableName = "ProductivityTask",
    indices = [Index("date")],
)
data class ProductivityTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,       // yyyy-MM-dd
    val text: String,       // trimmed, never blank
    val isDone: Boolean,
    val createdAt: String,  // ISO-8601 instant, same as Attendance
    val updatedAt: String,  // ISO-8601 instant, same as Attendance
)
```

Tasks within a day are shown in the order they were added (`ORDER BY id ASC`).

### Domain models (`domain/model/Productivity.kt`)

```kotlin
data class ProductivityTask(
    val id: Long,
    val date: LocalDate,
    val text: String,
    val isDone: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

data class ProductivityDay(
    val date: LocalDate,
    val tasks: List<ProductivityTask>,
) {
    val totalCount: Int
    val doneCount: Int
    val leftCount: Int
    val percentage: Int
    val status: DayStatus
}

enum class DayStatus { NOT_STARTED, BEHIND, ON_TRACK, ALL_DONE }

data class ProductivityBar(
    val date: LocalDate,
    val percentage: Int,
    val isPlanned: Boolean,
)

data class ProductivitySummary(
    val average: Int,
    val bars: List<ProductivityBar>,   // always 7, oldest first
    val bestDay: ProductivityDay?,     // null when the window has no planned day
)
```

`ProductivityDay` and `ProductivitySummary` are calculated from tasks and have no tables.
The calculations stay in the domain layer as plain functions that take `today` as a parameter,
so they can be unit tested with fixed dates.

### Database migration

Adding this table moves `ManabiDatabase` from version 2 to version 3.

**Warning:** every platform's database builder uses `fallbackToDestructiveMigration`, and no
migration is registered. Changing the version without a migration **deletes all attendance data**.
This must ship with the table:

```kotlin
@Database(
    entities = [AttendanceEntity::class, ProductivityTaskEntity::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
)
```

`ManabiDatabaseMigrationTest` proves this: it opens a real version 2 database and checks that the
attendance data survives. Any later version change needs its own migration and the same check.

## Code placement

| Code | Location |
|---|---|
| Screens and ViewModel | `presentation/screens/Productivity/` |
| Components used only by Productivity | `presentation/screens/Productivity/components/` |
| Components used by more than one feature | `presentation/components/` |
| Domain models, repository interface and use cases | `domain/model`, `domain/repository`, `domain/usecase` |
| Repository implementation | `data/repositoryImpl/` |
| Entity and DAO | `database/` |

Follow the Attendance pattern for mobile and desktop: `ProductivityScreen` picks
`ProductivityMobileScreen` or `ProductivityDesktopScreen`, and both use `ProductivityViewModel`.

- **Mobile** opens a day as its own route, `ProductivityDayMobileScreen`. Changing the date swaps
  the route, so Back always returns to the days list.
- **Desktop** shows the list and the open day side by side, and opens on today.
