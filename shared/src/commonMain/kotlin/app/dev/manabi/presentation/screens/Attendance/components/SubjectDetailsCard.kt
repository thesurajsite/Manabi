package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.components.SectionCard
import app.dev.manabi.presentation.theme.fieldBackground
import app.dev.manabi.presentation.theme.fieldBorder
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.textDisabled
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

@Composable
fun SubjectDetailsCard(
    subjectName: String,
    teacherName: String,
    onSubjectNameChange: (String) -> Unit,
    onTeacherNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier) {
        LabeledTextField(
            label = "Subject",
            value = subjectName,
            onValueChange = onSubjectNameChange,
            placeholder = "e.g. Data Structures",
            icon = Icons.AutoMirrored.Outlined.MenuBook,
            isEmphasized = true,
        )
        LabeledTextField(
            label = "Teacher",
            value = teacherName,
            onValueChange = onTeacherNameChange,
            placeholder = "e.g. Dr. Mehta",
            icon = Icons.Outlined.Person,
        )
    }
}

@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isEmphasized: Boolean = false,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = textMuted,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = textDisabled) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = textMuted,
                    modifier = Modifier.size(20.dp),
                )
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontWeight = if (isEmphasized) FontWeight.SemiBold else FontWeight.Medium,
                color = textPrimary,
            ),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primary,
                unfocusedBorderColor = fieldBorder,
                focusedContainerColor = fieldBackground,
                unfocusedContainerColor = fieldBackground,
                cursorColor = primary,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
