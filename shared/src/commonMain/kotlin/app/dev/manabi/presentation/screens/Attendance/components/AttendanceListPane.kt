package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.domain.model.AttendanceStatus
import app.dev.manabi.presentation.components.SearchBar
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

/**
 * Attendance list column: title with New button, search bar, filter row and subject list.
 * Stateless; used as the whole mobile screen and as the left pane on desktop.
 */
@Composable
fun AttendanceListPane(
    attendanceList: List<Attendance>,
    searchQuery: String,
    selectedFilter: String,
    todayStatusFor: (Attendance) -> AttendanceStatus,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelected: (String) -> Unit,
    onItemClick: (Attendance) -> Unit,
    onChevronClick: (Attendance) -> Unit,
    onPresentClick: (Attendance) -> Unit,
    onAbsentClick: (Attendance) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedSubjectId: Long? = null,
) {
    // Track expanded state per subject
    val expandedSubjects = remember { mutableStateMapOf<Long, Boolean>() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cardBackground)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Attendance",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textPrimary,
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(containerColor = primarySoft, contentColor = primary),
                modifier = Modifier.height(40.dp),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(text = "New", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            placeHolder = "Search subjects or teachers",
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        AttendanceFilterRow(
            selectedFilter = selectedFilter,
            onFilterSelected = onFilterSelected,
            modifier = Modifier.padding(horizontal = 20.dp),
        )

        if (attendanceList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Text(
                    text = "No subjects found",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textMuted,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(
                    items = attendanceList,
                    key = { it.id },
                ) { subject ->
                    val isExpanded = expandedSubjects[subject.id] ?: false

                    AttendanceItem(
                        subject = subject,
                        isExpanded = isExpanded,
                        isSelected = subject.id == selectedSubjectId,
                        status = todayStatusFor(subject),
                        onItemClick = {
                            // Toggle expansion on item click
                            expandedSubjects[subject.id] = !isExpanded
                            onItemClick(subject)
                        },
                        onChevronClick = onChevronClick,
                        onPresentClick = onPresentClick,
                        onAbsentClick = onAbsentClick,
                    )
                }
            }
        }
    }
}
