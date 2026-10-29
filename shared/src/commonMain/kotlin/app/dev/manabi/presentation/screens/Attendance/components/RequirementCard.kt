package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.dev.manabi.presentation.components.SectionCard
import app.dev.manabi.presentation.theme.absentRed
import app.dev.manabi.presentation.theme.cardBackground
import app.dev.manabi.presentation.theme.neutralSoft
import app.dev.manabi.presentation.theme.primary
import app.dev.manabi.presentation.theme.primarySoft
import app.dev.manabi.presentation.theme.textMuted
import app.dev.manabi.presentation.theme.textPrimary

private const val REQUIREMENT_STEP = 5

/** Required-attendance stepper with a bar comparing the current percentage to the target. */
@Composable
fun RequirementCard(
    requirement: Int,
    percentage: Int,
    isOnTrack: Boolean,
    onRequirementChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(primarySoft),
            ) {
                Icon(
                    imageVector = Icons.Filled.TrackChanges,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Requirement",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                )
                Text(
                    text = "Minimum attendance to keep",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = textMuted,
                )
            }

            RequirementStepper(
                requirement = requirement,
                onDecrease = { onRequirementChange(requirement - REQUIREMENT_STEP) },
                onIncrease = { onRequirementChange(requirement + REQUIREMENT_STEP) },
            )
        }

        TargetProgressBar(
            percentage = percentage,
            requirement = requirement,
            isOnTrack = isOnTrack,
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "You · $percentage%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textMuted,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "Target · $requirement%",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = textMuted,
            )
        }
    }
}

@Composable
private fun RequirementStepper(
    requirement: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(neutralSoft)
            .padding(2.dp),
    ) {
        StepperButton(icon = Icons.Filled.Remove, contentDescription = "Decrease requirement", onClick = onDecrease)
        Text(
            text = "$requirement%",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 48.dp),
        )
        StepperButton(icon = Icons.Filled.Add, contentDescription = "Increase requirement", onClick = onIncrease)
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = cardBackground,
            contentColor = textPrimary,
        ),
        modifier = Modifier.size(44.dp),
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription, modifier = Modifier.size(20.dp))
    }
}

/** Track filled up to [percentage], with a marker at [requirement]. */
@Composable
private fun TargetProgressBar(
    percentage: Int,
    requirement: Int,
    isOnTrack: Boolean,
) {
    BoxWithConstraints(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(18.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(primarySoft),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percentage.coerceIn(0, 100) / 100f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(if (isOnTrack) primary else absentRed),
            )
        }

        val markerWidth = 3.dp
        Box(
            modifier = Modifier
                .offset(x = maxWidth * (requirement.coerceIn(0, 100) / 100f) - markerWidth / 2)
                .width(markerWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(2.dp))
                .background(textPrimary),
        )
    }
}
