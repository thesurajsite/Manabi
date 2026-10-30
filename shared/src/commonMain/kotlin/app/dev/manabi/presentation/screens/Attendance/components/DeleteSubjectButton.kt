package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.theme.absentRed

private val DeleteBorder = Color(0xFFF1D0D0)

/**
 * Outlined red delete button. Icon-only by default (mobile top bar);
 * [showLabel] adds the "Delete" text (desktop top bar).
 */
@Composable
fun DeleteSubjectButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean = false,
) {
    if (showLabel) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, DeleteBorder),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = absentRed),
            contentPadding = PaddingValues(horizontal = 14.dp),
            modifier = modifier.height(44.dp),
        ) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(text = "Delete", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    } else {
        OutlinedIconButton(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, DeleteBorder),
            modifier = modifier.size(44.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete subject",
                tint = absentRed,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
