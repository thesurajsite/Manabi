package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.model.AttendanceStatus

@Suppress("ktlint:standard:function-naming")
@Composable
fun AttendanceItem(
    subject: Attendance,
    onItemClick: (Attendance) -> Unit,
    onChevronClick: (Attendance) -> Unit,
    onPresentClick: (Attendance) -> Unit,
    onAbsentClick: (Attendance) -> Unit,
    isExpanded: Boolean = false,
    status: AttendanceStatus = AttendanceStatus.NOT_MARKED,
) {
    val presentEnabled = status != AttendanceStatus.PRESENT
    val absentEnabled = status != AttendanceStatus.ABSENT

    val percentage = if (subject.conducted > 0) (subject.attended * 100) / subject.conducted else 0

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header row - always visible
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onItemClick(subject) })
                    .padding(vertical = 10.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Circular Progress Bar
            CircularProgressBar(
                percent = percentage,
                size = 60.dp,
                strokeWidth = 5.dp,
                color = Color(0xFF6C63FF),
                trackColor = Color(0x33A78BFA),
                textColor = Color(0xFF6C63FF),
                text = "$percentage%",
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Name + subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.subjectName,
                    style =
                        MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(2.dp))

                // Teacher Name
                Text(
                    text = subject.teacher,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Stats: C: xx | A: xx
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatLabel(label = "C:", value = subject.conducted.toString())
                    Box(
                        modifier =
                            Modifier
                                .padding(horizontal = 6.dp)
                                .width(1.dp)
                                .height(14.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant),
                    )
                    StatLabel(label = "A:", value = subject.attended.toString())
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            IconButton(onClick = { onChevronClick(subject) }) {
                Text(
                    text = "›",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Expanded content - Present/Absent buttons
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(
                        onClick = { onPresentClick(subject) },
                        enabled = presentEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50),
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Present",
                            modifier = Modifier.size(18.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onAbsentClick(subject) },
                        enabled = absentEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF5252),
                            contentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Absent",
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun StatLabel(
    label: String,
    value: String,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
