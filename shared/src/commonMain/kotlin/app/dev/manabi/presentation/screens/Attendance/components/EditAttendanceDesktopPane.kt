package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.dev.manabi.domain.model.Attendance
import app.dev.manabi.presentation.screens.attendance.AttendanceViewModel
import app.dev.manabi.presentation.theme.primary
import org.koin.compose.viewmodel.koinViewModel

private val Teal400    = Color(0xFF1D9E75)
private val Danger     = Color(0xFFFF6B6B)
private val White      = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAttendanceDesktopPane(
    attendance: Attendance?,
    isMobile: Boolean,
    onBack: () -> Unit = {}
) {
    val viewModel: AttendanceViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete Subject") },
            text = { Text("Are you sure you want to delete this subject?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        viewModel.deleteAttendance(uiState.id, onBack)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar (Update Subject)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Text(
                text = if (attendance == null) "Add Subject" else "Update Subject",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )
            )

            // Close Subject Details Pane
            IconButton(
                onClick = { viewModel.closeSubject() },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        val color = Color(0xFF6D28D9)
        OutlinedTextField(
            value = uiState.subjectName,
            onValueChange = { viewModel.updateSubjectName(it) },
            placeholder = { Text("Subject Name") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
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

        Spacer(modifier = Modifier.height(20.dp))

        // Status cards row
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                requirement = uiState.requirement,
                isOnTrack = uiState.isOnTrack
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(0.8f)
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

        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.8f)
        ){
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
                modifier = Modifier.weight(0.8f)
            )

            Spacer(modifier = Modifier.width(10.dp))

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
                modifier = Modifier.weight(0.8f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
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
                    viewModel.saveAttendance(onSuccess = {})
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


@Composable
fun AttendanceRingCard(
    modifier: Modifier = Modifier,
    percentage: Int,
    isOnTrack: Boolean
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 2.dp,
                color =  Color(0xFF6D28D9),
                shape = RoundedCornerShape(18.dp)
            )
            .background(White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Circular Progress Bar
        CircularProgressBar(
            percent = percentage,
            size = 68.dp,
            strokeWidth = 5.dp,
            color = Color(0xFF6C63FF),
            trackColor = Color(0x33A78BFA),
            textColor = Color(0xFF6C63FF),
            text = "$percentage%",
        )

        Text(
            text = "ATTENDANCE",
            fontSize = 9.sp,
            color = Color.Black,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



@Composable
fun ClassesNeededCard(
    modifier: Modifier = Modifier,
    needed: Int,
    requirement: Int,
    isOnTrack: Boolean
) {
    val badgeColor by animateColorAsState(
        targetValue = if (isOnTrack) Teal400 else Danger,
        animationSpec = tween(400),
        label = "badgeColor"
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 2.dp,
                color =  Color(0xFF6D28D9),
                shape = RoundedCornerShape(18.dp)
            )
            .background(White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(badgeColor)
        ) {
            Text(
                text = "$needed",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = White
            )
        }

        Text(
            text = "CLASSES NEEDED",
            fontSize = 9.sp,
            color = Color.Black,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun StepperSection(
    icon: ImageVector,
    title: String,
    pill: String,
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .border(width = 2.dp, color = primary, shape = RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(primary)
            .padding(14.dp)
    ) {
        // Header row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$title",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                letterSpacing = 0.4.sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.weight(1f))

            // Pill badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$pill",
                    fontSize = 11.sp,
                    color = primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Stepper row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.wrapContentWidth()
        ) {
            // Minus button
            IconButton(
                onClick = onMinus,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Text(
                    text = "−",
                    fontSize = 22.sp,
                    color = primary,
                    fontWeight = FontWeight.Normal
                )
            }

            // Value display
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "$value",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = primary
                )
            }

            // Plus button
            IconButton(
                onClick = onPlus,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                Text(
                    text = "+",
                    fontSize = 22.sp,
                    color = primary,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}