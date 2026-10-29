package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.components.SectionCard
import app.dev.manabi.presentation.components.StatusPill
import app.dev.manabi.presentation.theme.absentSoft
import app.dev.manabi.presentation.theme.absentText
import app.dev.manabi.presentation.theme.neutralSoft
import app.dev.manabi.presentation.theme.presentSoft
import app.dev.manabi.presentation.theme.presentText
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

private val MissedTileBackground = Color(0xFFFDF3F3)
private val AttendedTileBackground = Color(0xFFF1FAF4)

/** Attendance percentage ring, on-track status and the attended / missed / conducted counts. */
@Composable
fun AttendanceOverviewCard(
    percentage: Int,
    requirement: Int,
    isOnTrack: Boolean,
    canSkip: Int,
    classesNeeded: Int,
    attended: Int,
    missed: Int,
    conducted: Int,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier, spacing = 16.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressBar(
                percent = percentage,
                text = "$percentage%",
                size = 88.dp,
                strokeWidth = 8.dp,
                color = primary,
                trackColor = primarySoft,
                textColor = textPrimary,
                fontSize = 22.sp,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isOnTrack) {
                    StatusPill(text = "On track", containerColor = presentSoft, contentColor = presentText)
                } else {
                    StatusPill(text = "Below target", containerColor = absentSoft, contentColor = absentText)
                }
                Text(
                    text = overviewHeadline(isOnTrack, canSkip, classesNeeded, requirement),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp,
                    color = textPrimary,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatTile(
                value = attended,
                label = "Attended",
                containerColor = AttendedTileBackground,
                valueColor = presentText,
                labelColor = presentText,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                value = missed,
                label = "Missed",
                containerColor = MissedTileBackground,
                valueColor = absentText,
                labelColor = absentText,
                modifier = Modifier.weight(1f),
            )
            StatTile(
                value = conducted,
                label = "Conducted",
                containerColor = neutralSoft,
                valueColor = textPrimary,
                labelColor = textMuted,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun StatTile(
    value: Int,
    label: String,
    containerColor: Color,
    valueColor: Color,
    labelColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = value.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = valueColor,
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = labelColor,
        )
    }
}

private fun overviewHeadline(
    isOnTrack: Boolean,
    canSkip: Int,
    classesNeeded: Int,
    requirement: Int,
): String =
    when {
        isOnTrack && canSkip > 0 ->
            "You can skip $canSkip ${if (canSkip == 1) "class" else "classes"} and stay above $requirement%"
        isOnTrack -> "Right at the line. Attend the next class to stay safe"
        else -> "Attend the next $classesNeeded ${if (classesNeeded == 1) "class" else "classes"} to reach $requirement%"
    }
