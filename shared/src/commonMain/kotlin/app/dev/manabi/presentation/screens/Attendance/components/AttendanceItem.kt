package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AttendanceItem(){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Magenta)
            .clip(RoundedCornerShape(10.dp))
    ){
        Row{
            // % Circle
            Box(
                modifier = Modifier.size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Red),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "33%",
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.width(10.dp))

            // Subject Name
            Text(
                text = "Rock\nMechanics",
                color = Color.Black,
                fontSize = 18.sp
            )

            Spacer(Modifier.width(10.dp))

            Text(
                text = "C: 26 | A: 20",
                color = Color.Black,
                fontSize = 18.sp
            )

        }

    }

}


@Preview
@Composable
private fun AttendanceItemPreview() {
    AttendanceItem()
}