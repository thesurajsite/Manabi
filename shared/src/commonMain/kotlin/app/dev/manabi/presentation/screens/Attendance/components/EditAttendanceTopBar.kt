package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

/**
 * Top bar of the edit attendance screen.
 * Mobile passes [onBack] (leading back arrow); desktop passes [onClose] (trailing close).
 * The delete button is shown only when [onDelete] is set; [isWide] shows its label.
 */
@Composable
fun EditAttendanceTopBar(
    title: String,
    modifier: Modifier = Modifier,
    isWide: Boolean = false,
    onBack: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = if (onBack != null) 4.dp else if (isWide) 32.dp else 20.dp,
                end = if (isWide) 28.dp else 12.dp,
                top = if (isWide) 16.dp else 8.dp,
                bottom = 8.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack, modifier = Modifier.size(44.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = textPrimary,
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "ATTENDANCE",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.4.sp,
                color = textMuted,
            )
            Text(
                text = title,
                fontSize = if (isWide) 22.sp else 19.sp,
                fontWeight = if (isWide) FontWeight.ExtraBold else FontWeight.Bold,
                color = textPrimary,
            )
        }

        if (onDelete != null) {
            DeleteSubjectButton(onClick = onDelete, showLabel = isWide)
        }

        if (onClose != null) {
            IconButton(onClick = onClose, modifier = Modifier.size(44.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = textPrimary,
                )
            }
        }
    }
}
