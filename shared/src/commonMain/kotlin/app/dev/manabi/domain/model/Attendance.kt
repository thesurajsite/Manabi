package app.dev.manabi.domain.model

import kotlinx.serialization.Serializable
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

@Serializable
data class Attendance(
    val id: Long,
    val subjectName: String,
    val conducted: Int,
    val attended: Int,
    val requirement: Int,
    val days: List<String>,
    val teacher: String,
    val createdAt: String,
    val updatedAt: String,
) {
    val percentage: Int
        get() = if (conducted == 0) 0 else ((attended.toFloat() / conducted) * 100).roundToInt()

    val classesNeeded: Int
        get() {
            if (percentage >= requirement) return 0
            val r = requirement / 100f
            return if (r >= 1f) 0 else max(0, ceil((r * conducted - attended) / (1 - r)).toInt())
        }

    val canSkip: Int
        get() {
            if (percentage < requirement || requirement <= 0) return 0
            val r = requirement / 100f
            return max(0, ((attended.toFloat() / r) - conducted).toInt())
        }

    val isOnTrack: Boolean get() = percentage >= requirement
}
