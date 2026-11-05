package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StoryboardShot
import com.example.ui.theme.FlowAmber
import com.example.ui.theme.FlowCyan
import com.example.ui.theme.FlowPurple
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TimelineOverview(
    shots: List<StoryboardShot>,
    selectedShotIndex: Int?,
    onShotSelect: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSeconds = shots.sumOf { it.durationSeconds }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = FlowAmber,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ANIMATION TIMELINE (16:9)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = FlowAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(FlowPurple.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Total: ${totalSeconds}s",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = FlowPurple,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Visual Filmstrip / Timeline Segments
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF090D14))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            shots.forEachIndexed { index, shot ->
                val isSelected = selectedShotIndex == index
                val segmentColor = when (index) {
                    0 -> FlowCyan
                    1 -> FlowPurple
                    2 -> FlowAmber
                    else -> Color(0xFF10B981)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .background(
                            if (isSelected) segmentColor.copy(alpha = 0.35f) else segmentColor.copy(alpha = 0.12f)
                        )
                        .border(
                            if (isSelected) 2.dp else 0.dp,
                            if (isSelected) segmentColor else Color.Transparent,
                            RoundedCornerShape(4.dp)
                        )
                        .clickable {
                            if (selectedShotIndex == index) {
                                onShotSelect(null) // unselect, show all
                            } else {
                                onShotSelect(index)
                            }
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Shot ${shot.shotNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) segmentColor else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                        Text(
                            text = "${shot.durationSeconds}s",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legend / quick hint
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "00:00",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
            )
            Text(
                text = if (selectedShotIndex != null) "Tap shot again to show all 4" else "Tap a shot to isolate",
                style = MaterialTheme.typography.labelSmall.copy(color = FlowCyan.copy(alpha = 0.7f), fontSize = 10.sp)
            )
            Text(
                text = String.format("00:%02d", totalSeconds),
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
            )
        }
    }
}
