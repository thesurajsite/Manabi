package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.domain.model.ProductivityDay
import app.dev.manabi.domain.model.ProductivityTask
import app.dev.manabi.presentation.components.SectionCard
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.presentFill
import app.dev.manabi.presentation.theme.presentText
import app.dev.manabi.presentation.theme.taskCheckboxBorder
import app.dev.manabi.presentation.theme.taskDivider
import app.dev.manabi.presentation.theme.textDisabled
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

/** The Tasks card. [footer] holds the desktop composer; mobile keeps its composer below the screen. */
@Composable
fun ProductivityTaskListCard(
    day: ProductivityDay,
    onToggleTask: (ProductivityTask) -> Unit,
    onRemoveTask: (ProductivityTask) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: Dp = 8.dp,
    footer: (@Composable () -> Unit)? = null,
) {
    SectionCard(modifier = modifier, spacing = 0.dp, contentPadding = contentPadding) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 6.dp),
        ) {
            Text(
                text = "Tasks",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textPrimary,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = if (day.totalCount == 1) "1 task" else "${day.totalCount} tasks",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textMuted,
            )
        }

        if (day.tasks.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 20.dp, bottom = 24.dp),
            ) {
                Text(text = "No tasks yet", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text(
                    text = "Add what you want to get done on this day.",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = textMuted,
                    textAlign = TextAlign.Center,
                )
            }
        }

        day.tasks.forEachIndexed { index, task ->
            if (index > 0) HorizontalDivider(color = taskDivider)
            ProductivityTaskRow(
                task = task,
                onToggle = { onToggleTask(task) },
                onRemove = { onRemoveTask(task) },
            )
        }

        footer?.invoke()
    }
}

@Composable
private fun ProductivityTaskRow(
    task: ProductivityTask,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .padding(end = 2.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .toggleable(value = task.isDone, role = Role.Checkbox, onValueChange = { onToggle() })
                .semantics { contentDescription = task.text },
        ) {
            val shape = RoundedCornerShape(7.dp)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(22.dp)
                    .clip(shape)
                    .background(if (task.isDone) presentFill else cardBackground)
                    .then(if (task.isDone) Modifier else Modifier.border(2.dp, taskCheckboxBorder, shape)),
            ) {
                if (task.isDone) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = presentText,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }

        Text(
            text = task.text,
            fontSize = 15.sp,
            fontWeight = if (task.isDone) FontWeight.Medium else FontWeight.SemiBold,
            color = if (task.isDone) textMuted else textPrimary,
            textDecoration = if (task.isDone) TextDecoration.LineThrough else null,
            modifier = Modifier.weight(1f),
        )

        IconButton(onClick = onRemove, modifier = Modifier.size(40.dp)) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Remove ${task.text}",
                tint = textDisabled,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}
