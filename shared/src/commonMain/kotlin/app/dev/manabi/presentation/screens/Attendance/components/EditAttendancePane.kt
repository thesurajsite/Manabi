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
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

private val Purple600  = Color(0xFF6C5CE7)
private val Purple800  = Color(0xFF3C3489)
private val Purple100  = Color(0xFFEEEDFE)
private val Purple200  = Color(0xFFAFA9EC)

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
fun EditAttendancePane(
    isMobile: Boolean,
    onBack: () -> Unit = {}
) {
    var state by remember { mutableStateOf(AttendanceState()) }
    var lastUpdated by remember { mutableStateOf("21-May 18:59") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar (Update Subject)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Update Subject",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                ),
            )
        }

        val color = Color(0xFF6D28D9)
        OutlinedTextField(
            value = "Rock mechanics",
            onValueChange = {},
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

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxWidth(0.8f)
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

        Spacer(Modifier.height(10.dp))

        if(isMobile){
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
                modifier = Modifier.weight(0.8f)
            )

            Spacer(modifier = Modifier.height(10.dp))

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
                modifier = Modifier.weight(0.8f)
            )
        }
        else{ // Is Desktop
            Row(
                modifier = Modifier.fillMaxWidth(0.8f)
            ){
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
                    modifier = Modifier.weight(0.8f)
                )

                Spacer(modifier = Modifier.width(10.dp))

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
                    modifier = Modifier.weight(0.8f)
                )
            }

        }

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
            percent = 61,
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
    bgColor: Color,
    chipColor: Color,
    iconColor: Color,
    titleColor: Color,
    pillColor: Color,
    valueColor: Color,
    btnColor: Color,
    minusBtnTextColor: Color,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .border(width = 2.dp, color = iconColor, shape = RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
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
                    .background(chipColor)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = titleColor,
                letterSpacing = 0.4.sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.weight(1f))
            // Pill badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(chipColor)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = pill,
                    fontSize = 11.sp,
                    color = pillColor,
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
                    .background(btnColor)
            ) {
                Text(
                    text = "−",
                    fontSize = 22.sp,
                    color = minusBtnTextColor,
                    fontWeight = FontWeight.Normal
                )
            }

            // Value display
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(chipColor)
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = valueColor
                )
            }

            // Plus button
            IconButton(
                onClick = onPlus,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(btnColor)
            ) {
                Text(
                    text = "+",
                    fontSize = 22.sp,
                    color = White,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}