package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.components.StatusPill
import app.dev.manabi.presentation.theme.absentFill
import app.dev.manabi.presentation.theme.absentSoft
import app.dev.manabi.presentation.theme.absentText
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.neutralSoft
import app.dev.manabi.presentation.theme.presentFill
import app.dev.manabi.presentation.theme.presentSoft
import app.dev.manabi.presentation.theme.presentText
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary
import kotlinx.datetime.LocalDate

/**
 * Actions for a tapped calendar date, based on its current [status]
 * (1 = present, 0 = absent, null = not marked):
 * - not marked: present / absent
 * - absent: present / remove
 * - present: absent / remove
 */
@Composable
fun DayActionPanel(
    date: LocalDate,
    status: Int?,
    onStatusChange: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HorizontalDivider(color = cardBorder)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = date.toDisplayTitle(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.weight(1f),
            )
            when (status) {
                1 -> StatusPill(text = "Present", containerColor = presentSoft, contentColor = presentText)
                0 -> StatusPill(text = "Absent", containerColor = absentSoft, contentColor = absentText)
                else -> StatusPill(text = "Not marked", containerColor = neutralSoft, contentColor = textMuted)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (status != 1) {
                ActionButton(
                    label = "Mark present",
                    containerColor = presentFill,
                    contentColor = presentText,
                    onClick = { onStatusChange(1) },
                    modifier = Modifier.weight(1f),
                )
            }
            if (status != 0) {
                ActionButton(
                    label = "Mark absent",
                    containerColor = absentFill,
                    contentColor = absentText,
                    onClick = { onStatusChange(0) },
                    modifier = Modifier.weight(1f),
                )
            }
            if (status != null) {
                ActionButton(
                    label = "Remove entry",
                    containerColor = neutralSoft,
                    contentColor = textPrimary,
                    onClick = { onStatusChange(null) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        elevation = null,
        modifier = modifier.height(44.dp),
    ) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

/** e.g. "Thursday, 15 October" */
private fun LocalDate.toDisplayTitle(): String =
    "${dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}, $day ${month.name.lowercase().replaceFirstChar { it.uppercase() }}"
