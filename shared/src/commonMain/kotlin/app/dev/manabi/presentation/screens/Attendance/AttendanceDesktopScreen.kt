package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.components.ScreenHeader

@Composable
fun AttendanceDesktopScreen(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScreenHeader(
            title = "Attendance",
            subtitle = "Desktop layout"
        )

        Row{
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .clip(RoundedCornerShape(15.dp))
            ){
                SubjectListScreen()
            }
            Box(modifier = Modifier.weight(0.6f)){

            }
        }
    }
}