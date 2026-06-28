package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.attendance.components.AttendanceRingCard
import app.dev.manabi.presentation.screens.attendance.components.ClassesNeededCard
import app.dev.manabi.presentation.screens.attendance.components.StepperSection
import org.koin.compose.viewmodel.koinViewModel

private val White = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAttendanceMobileScreen(
    attendance: Attendance?,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AttendanceViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Initialize the ViewModel state if it's the first time or if the attendance object changed
    LaunchedEffect(attendance) {
        viewModel.openSubject(attendance)
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Subject") },
            text = { Text("Are you sure you want to delete this subject?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        viewModel.deleteAttendance(uiState.id, onNavigateUp)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (attendance == null) "Add Attendance" else "Edit Attendance") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val color = Color(0xFF6D28D9)
            OutlinedTextField(
                value = uiState.subjectName,
                onValueChange = { viewModel.updateSubjectName(it) },
                placeholder = { Text("Subject Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = color,
                        shape = RoundedCornerShape(14.dp)
                    ),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(5.dp))

            // Status cards row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Attendance percent card
                AttendanceRingCard(
                    modifier = Modifier.weight(0.8f),
                    percentage = uiState.percentage,
                    isOnTrack = uiState.isOnTrack
                )
                // Classes needed card
                ClassesNeededCard(
                    modifier = Modifier.weight(0.8f),
                    needed = uiState.classesNeeded,
                    canSkip = uiState.canSkip,
                    requirement = uiState.requirement,
                    isOnTrack = uiState.isOnTrack
                )
            }

            Spacer(Modifier.height(48.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                // Requirement section
                StepperSection(
                    icon = Icons.Filled.TrackChanges,
                    title = "Requirement",
                    pill = "target",
                    value = "${uiState.requirement}%",
                    onMinus = {
                        viewModel.updateRequirement(uiState.requirement - 5)
                    },
                    onPlus = {
                        viewModel.updateRequirement(uiState.requirement + 5)
                    },
                    modifier = Modifier
                )
            }

            Spacer(Modifier.height(5.dp))

            // Classes conducted
            StepperSection(
                icon = Icons.Filled.School,
                title = "Classes conducted",
                pill = "total",
                value = "${uiState.conducted}",
                onMinus = {
                    viewModel.updateConducted(uiState.conducted - 1)
                },
                onPlus = {
                    viewModel.updateConducted(uiState.conducted + 1)
                },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Classes attended
            StepperSection(
                icon = Icons.Filled.CheckCircle,
                title = "Classes attended",
                pill = "yours",
                value = "${uiState.attended}",
                onMinus = {
                    viewModel.updateAttended(uiState.attended - 1)
                },
                onPlus = {
                    viewModel.updateAttended(uiState.attended + 1)
                },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF6C63FF)),
                    onClick = {
                        viewModel.saveAttendance(onSuccess = onNavigateUp)
                    },
                ) {
                    Text(
                        text = if (attendance == null) "Add Attendance" else "Save changes",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = White,
                        letterSpacing = 0.3.sp
                    )
                }

                if (attendance != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { showDeleteConfirmation = true },
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.White, RoundedCornerShape(14.dp))
                            .border(1.dp, Color.Red, RoundedCornerShape(14.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}
