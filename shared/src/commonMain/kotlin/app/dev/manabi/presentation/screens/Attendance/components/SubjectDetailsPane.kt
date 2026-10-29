package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.Attendance

@Composable
fun SubjectDetailsPane(
    attendance: Attendance,
    onPresentClick: (Attendance) -> Unit,
    onAbsentClick: (Attendance) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        // Subject header
        Text(
            text = attendance.subjectName,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "C: ${attendance.conducted} | A: ${attendance.attended}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${attendance.percentage}%",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (attendance.isOnTrack) Color(0xFF4CAF50) else Color(0xFFFF5252),
                ),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = { onPresentClick(attendance) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            ) {
                Text(text = "Mark Present")
            }
            Button(
                onClick = { onAbsentClick(attendance) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
            ) {
                Text(text = "Mark Absent")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Attendance History
        Text(
            text = "Attendance History",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))

        val historyEntries = attendance.days.entries.toList().sortedByDescending { it.key }

        if (historyEntries.isEmpty()) {
            Text(
                text = "No attendance records yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(items = historyEntries, key = { it.key }) { (date, status) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = date,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f),
                        )
                        Icon(
                            imageVector = if (status == 1) Icons.Filled.CheckCircle else Icons.Filled.Close,
                            contentDescription = if (status == 1) "Present" else "Absent",
                            tint = if (status == 1) Color(0xFF4CAF50) else Color(0xFFFF5252),
                        )
                    }
                }
            }
        }
    }
}
