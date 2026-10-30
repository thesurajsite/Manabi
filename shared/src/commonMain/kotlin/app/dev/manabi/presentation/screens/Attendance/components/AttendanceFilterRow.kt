package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.textMuted

/** Filter values understood by AttendanceViewModel.updateFilter. */
val filterTabs = listOf("All", "On Track", "At Risk", "Can Miss", "Must Go")

/** Single line of filter chips that scrolls horizontally. */
@Composable
fun AttendanceFilterRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        filterTabs.forEach { label ->
            FilterPill(
                label = label,
                isSelected = selectedFilter == label,
                onClick = { onFilterSelected(label) },
            )
        }
    }
}

@Composable
private fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(50)
    val padding = PaddingValues(horizontal = 14.dp)
    val pillModifier = Modifier.height(34.dp)

    if (isSelected) {
        Button(
            onClick = onClick,
            shape = shape,
            contentPadding = padding,
            colors = ButtonDefaults.buttonColors(containerColor = primary, contentColor = Color.White),
            elevation = null,
            modifier = pillModifier,
        ) {
            Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            shape = shape,
            contentPadding = padding,
            border = BorderStroke(1.dp, cardBorder),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = cardBackground, contentColor = textMuted),
            modifier = pillModifier,
        ) {
            Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
