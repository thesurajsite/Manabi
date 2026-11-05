package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.domain.model.ProductivityTask
import app.dev.manabi.presentation.screens.Productivity.toDayTitle
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.screenBackground
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

private val TwoColumnMinWidth = 704.dp

/** Right desktop pane: the open day's progress and tasks. */
@Composable
fun ProductivityDayDesktopPane(
    day: ProductivityDay,
    draft: String,
    onDraftChange: (String) -> Unit,
    onAddTask: () -> Unit,
    onToggleTask: (ProductivityTask) -> Unit,
    onRemoveTask: (ProductivityTask) -> Unit,
    onChangeDate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(screenBackground),
    ) {
        val isTwoColumn = maxWidth >= TwoColumnMinWidth
        val sidePadding = if (isTwoColumn) 32.dp else 16.dp

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = sidePadding, end = sidePadding, top = 18.dp, bottom = 14.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "PRODUCTIVITY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.4.sp,
                        color = textMuted,
                    )
                    Text(
                        text = day.date.toDayTitle(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textPrimary,
                    )
                }
                OutlinedButton(
                    onClick = onChangeDate,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, cardBorder),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = cardBackground,
                        contentColor = textPrimary,
                    ),
                    modifier = Modifier.height(44.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Change date", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            val taskList: @Composable (Modifier) -> Unit = { cardModifier ->
                ProductivityTaskListCard(
                    day = day,
                    onToggleTask = onToggleTask,
                    onRemoveTask = onRemoveTask,
                    contentPadding = 12.dp,
                    footer = {
                        ProductivityTaskComposer(
                            draft = draft,
                            onDraftChange = onDraftChange,
                            onAdd = onAddTask,
                            isWide = true,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    },
                    modifier = cardModifier,
                )
            }
            val cardsModifier = Modifier.padding(start = sidePadding, end = sidePadding, bottom = 32.dp)

            if (isTwoColumn) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = cardsModifier,
                ) {
                    ProductivityDayProgressCard(day = day, isWide = true, modifier = Modifier.weight(1f))
                    taskList(Modifier.weight(1.25f))
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = cardsModifier,
                ) {
                    ProductivityDayProgressCard(day = day, isWide = true)
                    taskList(Modifier)
                }
            }
        }
    }
}
