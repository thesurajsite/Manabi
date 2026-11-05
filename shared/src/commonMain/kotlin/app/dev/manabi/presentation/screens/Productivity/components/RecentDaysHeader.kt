package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

@Composable
fun RecentDaysHeader(
    hint: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Recent days",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = textPrimary,
            modifier = Modifier.weight(1f),
        )
        Text(text = hint, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textMuted)
    }
}

/** Shown in place of the day cards until the first day is planned. */
@Composable
fun NoPlannedDays(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
    ) {
        Text(text = "No days planned yet", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Text(
            text = "Use Plan a day to add your first tasks.",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textMuted,
            textAlign = TextAlign.Center,
        )
    }
}
