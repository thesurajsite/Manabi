package app.dev.manabi.presentation.screens.Productivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.AddTask
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.cardBorder
import app.dev.manabi.presentation.theme.fieldBackground
import app.dev.manabi.presentation.theme.fieldBorder
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.textDisabled
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

/** Add-a-task field. Mobile shows it as a bar below the screen, desktop inside the Tasks card. */
@Composable
fun ProductivityTaskComposer(
    draft: String,
    onDraftChange: (String) -> Unit,
    onAdd: () -> Unit,
    isWide: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().background(cardBackground)) {
        HorizontalDivider(color = cardBorder)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isWide) {
                        Modifier.padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 4.dp)
                    } else {
                        Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp)
                    }
                ),
        ) {
            TaskField(
                draft = draft,
                onDraftChange = onDraftChange,
                onAdd = onAdd,
                isWide = isWide,
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = onAdd,
                enabled = draft.isNotBlank(),
                shape = RoundedCornerShape(if (isWide) 12.dp else 14.dp),
                contentPadding = if (isWide) PaddingValues(horizontal = 20.dp) else PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    contentColor = cardBackground,
                    disabledContainerColor = primary,
                    disabledContentColor = cardBackground,
                ),
                modifier = if (isWide) Modifier.height(48.dp) else Modifier.size(52.dp),
            ) {
                if (isWide) {
                    Text(text = "Add", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(
                        imageVector = Icons.Filled.ArrowUpward,
                        contentDescription = "Add task",
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskField(
    draft: String,
    onDraftChange: (String) -> Unit,
    onAdd: () -> Unit,
    isWide: Boolean,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(if (isWide) 12.dp else 14.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .height(if (isWide) 48.dp else 52.dp)
            .clip(shape)
            .background(fieldBackground)
            .border(1.dp, if (isFocused) primary else fieldBorder, shape)
            .padding(horizontal = 14.dp),
    ) {
        Icon(
            imageVector = if (isWide) Icons.Filled.Add else Icons.Outlined.AddTask,
            contentDescription = null,
            tint = textMuted,
            modifier = Modifier.size(18.dp),
        )
        BasicTextField(
            value = draft,
            onValueChange = onDraftChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textPrimary),
            cursorBrush = SolidColor(primary),
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { onAdd() }),
            modifier = Modifier
                .weight(1f)
                .onPreviewKeyEvent { event ->
                    val isEnter = event.key == Key.Enter || event.key == Key.NumPadEnter
                    if (isEnter && event.type == KeyEventType.KeyDown) {
                        onAdd()
                        true
                    } else {
                        false
                    }
                },
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (draft.isEmpty()) {
                        Text(
                            text = if (isWide) "Add a task and press Enter" else "Add a task",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = textDisabled,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}
