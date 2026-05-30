package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.screens.attendance.components.AttendanceListPane
import app.dev.manabi.presentation.screens.attendance.components.EditAttendancePane

@Composable
fun AttendanceDesktopScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row{
            // Attendance List Pane
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .clip(RoundedCornerShape(15.dp))
            ){
                AttendanceListPane(
                    onSubjectClick = {

                    }
                )
            }

            Spacer(Modifier.width(10.dp))

            // Attendance Detail Pane
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .clip(RoundedCornerShape(15.dp))
            ){
                EditAttendancePane(
                    isMobile = false
                )
            }
        }
    }
}