package app.dev.manabi.presentation.screens.attendance.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas

@Composable
fun CircularProgressBar(
    percent: Int,
    text: String,
    size: Dp = 120.dp,
    strokeWidth: Dp = 10.dp,
    color: Color = Color(0xFFA78BFA),
    trackColor: Color = Color(0x33A78BFA),
    textColor: Color = Color(0xFF6D28D9),
    fontSize: TextUnit = TextUnit.Unspecified,
    label: String? = null,
    animationDuration: Int = 600,
    modifier: Modifier = Modifier,
) {
    val clampedPercent = percent.coerceIn(0, 100)

    val animatedSweep by animateFloatAsState(
        targetValue = clampedPercent / 100f * 360f,
        animationSpec = tween(
            durationMillis = animationDuration,
            easing = FastOutSlowInEasing
        ),
        label = "sweep"
    )

    val computedFontSize = if (fontSize == TextUnit.Unspecified) (size.value * 0.22f).sp else fontSize

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(size)
        ) {
            Canvas(modifier = Modifier.size(size)) {
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                    size = Size(
                        this.size.width - strokeWidth.toPx(),
                        this.size.height - strokeWidth.toPx()
                    ),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )

                drawArc(
                    color = color,
                    startAngle = -90f,
                    sweepAngle = animatedSweep,
                    useCenter = false,
                    topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                    size = Size(
                        this.size.width - strokeWidth.toPx(),
                        this.size.height - strokeWidth.toPx()
                    ),
                    style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
                )
            }

            Text(
                text = text,
                color = textColor,
                fontSize = computedFontSize,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (label != null) {
            Text(
                text = label,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CircularProgressBarPreview() {
    val examples = listOf(
        Triple(25,  Color(0xFFF472B6) to Color(0x33F472B6), "Design"),
        Triple(61,  Color(0xFFA78BFA) to Color(0x33A78BFA), "Progress"),
        Triple(80,  Color(0xFF34D399) to Color(0x3334D399), "Complete"),
        Triple(95,  Color(0xFFFB923C) to Color(0x33FB923C), "Memory"),
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(24.dp)
    ) {
        examples.forEach { (pct, colors, lbl) ->
            CircularProgressBar(
                percent = pct,
                color = colors.first,
                trackColor = colors.second,
                textColor = colors.first,
                label = lbl,
                text = "65%"
            )
        }
    }
}