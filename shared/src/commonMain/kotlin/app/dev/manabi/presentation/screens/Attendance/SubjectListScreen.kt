package app.dev.manabi.presentation.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Data ────────────────────────────────────────────────────────────────────

data class Subject(
    val name: String,
    val subtitle: String,
    val emoji: String,
    val iconBg: Color,
    val attended: Int,
    val conducted: Int,
) {
    val percentage: Int
        get() = if (conducted == 0) 0 else ((attended * 100f) / conducted).toInt()
}

val sampleSubjects = listOf(
    Subject("Rock Mechanics",     "Geology · Sem 6",  "📐", Color(0xFFEDE9FE), attended = 16, conducted = 26),
    Subject("Structural Analysis","Civil · Sem 6",    "🏗️", Color(0xFFFEF3C7), attended = 22, conducted = 31),
    Subject("Fluid Mechanics",    "Civil · Sem 5",    "💧", Color(0xFFDCFCE7), attended = 11, conducted = 18),
    Subject("Geotechnical Engg",  "Civil · Sem 6",    "⚡", Color(0xFFFFE4E6), attended = 29, conducted = 40),
    Subject("Hydrology",          "Civil · Sem 5",    "🌊", Color(0xFFE0F2FE), attended = 19, conducted = 23),
    Subject("Engineering Geology","Civil · Sem 4",    "🔬", Color(0xFFF3E8FF), attended = 9,  conducted = 15),
)

val filterTabs = listOf("All", "Favourites", "Studies")

// ── Screen ──────────────────────────────────────────────────────────────────

@Composable
fun SubjectListScreen(
    subjects: List<Subject> = sampleSubjects,
    onSubjectClick: (Subject) -> Unit = {},
) {
    var selectedFilter by remember { mutableStateOf("All") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Top bar ──────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Subjects",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                ),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.AddBox,
                    contentDescription = "New subject",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // ── Search bar ───────────────────────────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Search or start a new chat",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ── Filter pills ─────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filterTabs.forEach { label ->
                val isActive = selectedFilter == label
                FilterChip(
                    selected = isActive,
                    onClick = { selectedFilter = label },
                    label = {
                        Text(
                            text = label,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF6C63FF),
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isActive,
                        borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        selectedBorderColor = Color.Transparent,
                        borderWidth = 0.5.dp,
                        selectedBorderWidth = 0.dp
                    )
                )
            }
        }

        // ── Subject list ─────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {
            subjects.forEach { subject ->
                SubjectItem(
                    subject = subject,
                    onClick = { onSubjectClick(subject) }
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    thickness = 0.5.dp
                )
            }
        }
    }
}

// ── Subject row item ─────────────────────────────────────────────────────────

@Composable
fun SubjectItem(
    subject: Subject,
    onClick: () -> Unit,
) {
    val pct = subject.percentage
    val pctColor = when {
        pct >= 75 -> Color(0xFF22C55E)
        pct >= 60 -> Color(0xFFF59E0B)
        else      -> Color(0xFFEF4444)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoji icon box
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(subject.iconBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = subject.emoji,
                fontSize = 22.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Name + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = subject.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subject.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Stats: C | A  and  percentage
        Column(horizontalAlignment = Alignment.End) {
            // C: xx | A: xx
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatLabel(label = "C", value = subject.conducted.toString())
                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .width(1.dp)
                        .height(14.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
                StatLabel(label = "A", value = subject.attended.toString())
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Percentage badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(pctColor.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "$pct%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = pctColor
                )
            }
        }
    }
}

// ── Small helper ─────────────────────────────────────────────────────────────

@Composable
private fun StatLabel(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// ── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SubjectListScreenPreview() {
    MaterialTheme {
        SubjectListScreen()
    }
}