package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.Subject

@Composable
fun SubjectItem(
    subject: Subject,
    onClick: () -> Unit,
) {
    val percentage = (subject.attended * 100) / subject.conducted
    val pctColor = when {
        percentage >= 75 -> Color(0xFF22C55E)
        percentage >= 60 -> Color(0xFFF59E0B)
        else      -> Color(0xFFEF4444)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular Progress Bar
        CircularProgressBar(
            percent = 61,
            size = 60.dp,
            strokeWidth = 5.dp,
            color = Color(0xFF6C63FF),
            trackColor = Color(0x33A78BFA),
            textColor = Color(0xFF6C63FF),
            text = "$percentage%",
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Name + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = subject.subjectName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))

            // Teacher Name
            Text(
                text = "Richi Prasad",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Stats: C | A  and  percentage
        Column(horizontalAlignment = Alignment.End) {
            // C: xx | A: xx
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatLabel(label = "C:", value = subject.conducted.toString())
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .width(1.dp)
                        .height(14.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
                StatLabel(label = "A:", value = subject.attended.toString())
            }

            Spacer(modifier = Modifier.height(4.dp))

//            // Classes Needed
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(50))
//                    .background(Color.Red)
//                    .padding(horizontal = 8.dp, vertical = 2.dp)
//            ) {
//                Text(
//                    text = "2",
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = pctColor
//                )
//            }
        }
    }
}

@Composable
fun StatLabel(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}