package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.screens.attendance.components.AttendanceRingCard
import app.dev.manabi.presentation.screens.attendance.components.AttendanceState
import app.dev.manabi.presentation.screens.attendance.components.ClassesNeededCard
import app.dev.manabi.presentation.screens.attendance.components.StepperSection
import kotlin.math.max


private val White = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAttendanceMobileScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {

    var state by remember { mutableStateOf(AttendanceState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Attendance") },
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
                value = "Rock mechanics",
                onValueChange = {},
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
                    percentage = state.percentage,
                    isOnTrack = state.isOnTrack
                )
                // Classes needed card
                ClassesNeededCard(
                    modifier = Modifier.weight(0.8f),
                    needed = state.classesNeeded,
                    requirement = state.requirement,
                    isOnTrack = state.isOnTrack
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
                    value = "${state.requirement}%",
                    onMinus = {
                        state = state.copy(requirement = max(0, state.requirement - 5))
                    },
                    onPlus = {
                        state = state.copy(requirement = minOf(100, state.requirement + 5))
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
                value = "${state.conducted}",
                onMinus = {
                    val newC = max(0, state.conducted - 1)
                    state = state.copy(
                        conducted = newC,
                        attended = minOf(state.attended, newC)
                    )
                },
                onPlus = {
                    state = state.copy(conducted = state.conducted + 1)
                },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Classes attended
            StepperSection(
                icon = Icons.Filled.CheckCircle,
                title = "Classes attended",
                pill = "yours",
                value = "${state.attended}",
                onMinus = {
                    state = state.copy(attended = max(0, state.attended - 1))
                },
                onPlus = {
                    state = state.copy(
                        attended = minOf(state.conducted, state.attended + 1)
                    )
                },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6C63FF)),
                onClick = { },
            ) {
                Text(
                    text = "Save changes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = White,
                    letterSpacing = 0.3.sp
                )
            }

        }
    }
}
