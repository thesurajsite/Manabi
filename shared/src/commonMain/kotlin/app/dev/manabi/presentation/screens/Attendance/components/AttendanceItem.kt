package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.model.AttendanceStatus
import app.dev.manabi.presentation.theme.absentFill
import app.dev.manabi.presentation.theme.absentRed
import app.dev.manabi.presentation.theme.absentText
import app.dev.manabi.presentation.theme.neutralSoft
import app.dev.manabi.presentation.theme.presentFill
import app.dev.manabi.presentation.theme.presentText
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.primaryTint
import app.dev.manabi.presentation.theme.primaryTintBorder
import app.dev.manabi.presentation.theme.textDisabled
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

/**
 * One subject row in the attendance list: % ring, name, teacher and attended/conducted.
 * When [isExpanded], shows today's Present / Absent buttons; the one matching [status] is disabled.
 * [isSelected] highlights the row (the subject open in the desktop detail pane).
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun AttendanceItem(
    subject: Attendance,
    onItemClick: (Attendance) -> Unit,
    onChevronClick: (Attendance) -> Unit,
    onPresentClick: (Attendance) -> Unit,
    onAbsentClick: (Attendance) -> Unit,
    isExpanded: Boolean = false,
    isSelected: Boolean = false,
    status: AttendanceStatus = AttendanceStatus.NOT_MARKED,
) {
    val shape = RoundedCornerShape(14.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .then(
                if (isSelected) {
                    Modifier.background(primaryTint).border(1.dp, primaryTintBorder, shape)
                } else {
                    Modifier
                },
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { onItemClick(subject) })
                .padding(start = 12.dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CircularProgressBar(
                percent = subject.percentage,
                text = "${subject.percentage}%",
                size = 48.dp,
                strokeWidth = 4.dp,
                color = if (subject.isOnTrack) primary else absentRed,
                trackColor = primarySoft,
                textColor = textPrimary,
                fontSize = 12.sp,
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.subjectName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subject.teacher,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = textMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row {
                    Text(
                        text = subject.attended.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textPrimary,
                    )
                    Text(
                        text = "/${subject.conducted}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textMuted,
                    )
                }
                Text(
                    text = "attended",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textMuted,
                )
            }

            IconButton(onClick = { onChevronClick(subject) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Open ${subject.subjectName}",
                    tint = textMuted,
                )
            }
        }

        // Expanded content - today's Present / Absent buttons
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Today",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textMuted,
                    modifier = Modifier.weight(1f),
                )
                TodayButton(
                    label = "Present",
                    icon = Icons.Filled.Check,
                    containerColor = presentFill,
                    contentColor = presentText,
                    enabled = status != AttendanceStatus.PRESENT,
                    onClick = { onPresentClick(subject) },
                )
                TodayButton(
                    label = "Absent",
                    icon = Icons.Filled.Close,
                    containerColor = absentFill,
                    contentColor = absentText,
                    enabled = status != AttendanceStatus.ABSENT,
                    onClick = { onAbsentClick(subject) },
                )
            }
        }
    }
}

@Composable
private fun TodayButton(
    label: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        elevation = null,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = neutralSoft,
            disabledContentColor = textDisabled,
        ),
        modifier = Modifier.height(36.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
