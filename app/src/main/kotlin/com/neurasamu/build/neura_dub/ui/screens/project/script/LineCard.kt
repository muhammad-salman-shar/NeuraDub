package com.neurasamu.build.neura_dub.ui.screens.project.script

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.neurasamu.build.neura_dub.data.local.CharacterEntity
import com.neurasamu.build.neura_dub.data.local.LineEntity
import com.neurasamu.build.neura_dub.data.local.ScriptText
import kotlin.math.roundToInt

@Composable
fun LineCard(
    line: LineEntity,
    character: CharacterEntity?,
    framerateMilli: Int,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "#${line.orderIndex + 1}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = line.markerType,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${ScriptText.formatTimecode(line.timeInMs, framerateMilli)} → ${ScriptText.formatTimecode(line.timeOutMs, framerateMilli)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (character != null) {
                    val dotColor = parseColorOr(character.colorHex, MaterialTheme.colorScheme.primary)
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(dotColor, CircleShape)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "(no character)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = line.originalText.ifBlank { "(no original)" },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (line.adaptedText.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = line.adaptedText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Syl: ${line.syllableCount}", style = MaterialTheme.typography.labelSmall)
                Text("Flaps: ${line.flapCount}", style = MaterialTheme.typography.labelSmall)
                DeltaBadge(line.flapDeltaPct)
                Spacer(Modifier.weight(1f))
                StatusBadge(line.status)
            }
        }
    }
}

private fun parseColorOr(hex: String, fallback: Color): Color = try {
    Color(android.graphics.Color.parseColor(hex))
} catch (_: Exception) {
    fallback
}

@Composable
private fun DeltaBadge(pct: Float) {
    val (bg, label) = when {
        pct <= 5f -> Color(0xFF2E7D32) to "OK ${pct.roundToInt()}%"
        pct <= 10f -> Color(0xFFF9A825) to "WARN ${pct.roundToInt()}%"
        else -> Color(0xFFC62828) to "OVER ${pct.roundToInt()}%"
    }
    Surface(color = bg, shape = RoundedCornerShape(4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun StatusBadge(status: String) {
    val bg = when (status) {
        LineEntity.STATUS_APPROVED -> Color(0xFF2E7D32)
        LineEntity.STATUS_RETAKE -> Color(0xFFE65100)
        LineEntity.STATUS_READY -> Color(0xFF1565C0)
        else -> Color(0xFF616161)
    }
    Surface(color = bg, shape = RoundedCornerShape(4.dp)) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
