package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.components.SearchBar
import app.dev.manabi.presentation.screens.attendance.AttendanceViewModel
import org.koin.compose.viewmodel.koinViewModel

val filterTabs = listOf("All", "On Track", "At Risk", "Can Miss", "Must Go")

@Composable
fun AttendanceListPane(onSubjectClick: (Attendance?) -> Unit) {
    var selectedFilter by remember { mutableStateOf("All") }
    val viewmodel: AttendanceViewModel = koinViewModel()
    val attendanceList by viewmodel.attendanceList.collectAsStateWithLifecycle()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "Attendance",
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // Add Attendance
            IconButton(onClick = {
                viewmodel.openSubject(null)
                onSubjectClick(null)
            }) {
                Icon(
                    imageVector = Icons.Filled.AddBox,
                    contentDescription = "New subject",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        // Search Bar
        SearchBar("Search")

        // Filter pills
        Row(
            modifier =
                Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            filterTabs.forEach { label ->
                val isActive = selectedFilter == label
                FilterChip(
                    selected = isActive,
                    onClick = { selectedFilter = label },
                    label = {
                        Text(
                            text = label,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 13.sp,
                        )
                    },
                    shape = RoundedCornerShape(50),
                    colors =
                        FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF6C63FF),
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    border =
                        FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isActive,
                            borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                            selectedBorderColor = Color.Transparent,
                            borderWidth = 0.5.dp,
                            selectedBorderWidth = 0.dp,
                        ),
                )
            }
        }

        // Subject List
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
        ) {
            if (attendanceList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No attendance records found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                attendanceList.forEach { subject ->
                    AttendanceItem(
                        subject = subject,
                        onClick = {
                            viewmodel.openSubject(subject)
                            onSubjectClick(subject)
                        },
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        thickness = 0.5.dp,
                    )
                }
            }
        }
    }
}
