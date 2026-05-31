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
import androidx.compose.material3.MaterialTheme
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
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

private val Pink50     = Color(0xFFFFF0F3)
private val Pink100    = Color(0xFFFFD6DE)
private val Pink600    = Color(0xFFD4537E)
private val Pink800    = Color(0xFF72243E)
private val Pink900    = Color(0xFF993556)

private val Blue50     = Color(0xFFEEF6FF)
private val Blue100    = Color(0xFFC8E0FA)
private val Blue400    = Color(0xFF378ADD)
private val Blue800    = Color(0xFF0C447C)
private val Blue900    = Color(0xFF185FA5)

private val Teal50     = Color(0xFFEDFBF5)
private val Teal100    = Color(0xFFB2EDDA)
private val Teal400    = Color(0xFF1D9E75)
private val Teal800    = Color(0xFF085041)
private val Teal900    = Color(0xFF0F6E56)

private val Danger     = Color(0xFFFF6B6B)
private val White      = Color(0xFFFFFFFF)
private val BgScreen   = Color(0xFFF4F3FF)
private val TextMuted  = Color(0xFF888888)
private val TextLight  = Color(0xFFBBBBBB)

data class AttendanceState(
    val requirement: Int = 75,
    val conducted: Int = 26,
    val attended: Int = 16
) {
    val percentage: Int
        get() = if (conducted == 0) 0 else ((attended.toFloat() / conducted) * 100).roundToInt()

    val classesNeeded: Int
        get() {
            if (percentage >= requirement) return 0
            val r = requirement / 100f
            return max(0, ceil((r * conducted - attended) / (1 - r)).toInt())
        }

    val isOnTrack: Boolean get() = percentage >= requirement
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAttendanceMobileScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    var state by remember { mutableStateOf(AttendanceState()) }
    var lastUpdated by remember { mutableStateOf("21-May 18:59") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Attendance") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                // Attendance ring card
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
                // Requirement section — pink
                StepperSection(
                    icon = Icons.Filled.TrackChanges,
                    title = "Requirement",
                    pill = "target",
                    value = "${state.requirement}%",
                    bgColor = Pink50,
                    chipColor = Pink100,
                    iconColor = Pink900,
                    titleColor = Pink900,
                    pillColor = Pink600,
                    valueColor = Pink800,
                    btnColor = Pink100,
                    minusBtnTextColor = Pink900,
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

            // Classes conducted — blue
            StepperSection(
                icon = Icons.Filled.School,
                title = "Classes conducted",
                pill = "total",
                value = "${state.conducted}",
                bgColor = Blue50,
                chipColor = Blue100,
                iconColor = Blue900,
                titleColor = Blue900,
                pillColor = Blue400,
                valueColor = Blue800,
                btnColor = Blue100,
                minusBtnTextColor = Blue900,
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

            // Classes attended — teal
            StepperSection(
                icon = Icons.Filled.CheckCircle,
                title = "Classes attended",
                pill = "yours",
                value = "${state.attended}",
                bgColor = Teal50,
                chipColor = Teal100,
                iconColor = Teal900,
                titleColor = Teal900,
                pillColor = Teal400,
                valueColor = Teal800,
                btnColor = Teal100,
                minusBtnTextColor = Teal900,
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
