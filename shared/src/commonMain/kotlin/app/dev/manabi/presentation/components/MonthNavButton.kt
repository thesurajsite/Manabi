package app.dev.manabi.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.textPrimary

/** Square outlined button that moves a calendar to the previous or next month. */
@Composable
fun MonthNavButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    OutlinedIconButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, cardBorder),
        modifier = Modifier.size(40.dp),
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription, tint = textPrimary)
    }
}
