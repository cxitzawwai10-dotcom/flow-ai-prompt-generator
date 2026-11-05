package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StoryboardShot
import com.example.ui.theme.BorderDark
import com.example.ui.theme.FlowAmber
import com.example.ui.theme.FlowCyan
import com.example.ui.theme.FlowEmerald
import com.example.ui.theme.FlowPurple
import com.example.ui.theme.SurfaceContainerDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ShotCard(
    shot: StoryboardShot,
    modifier: Modifier = Modifier,
    isInitiallyExpanded: Boolean = true
) {
    var isExpanded by remember { mutableStateOf(isInitiallyExpanded) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("shot_card_${shot.shotNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceContainerDark
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(BorderDark)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Shot Number + Title + Expand Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(FlowCyan.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                            .border(1.dp, FlowCyan.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${shot.shotNumber}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = FlowCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = shot.shotTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Shot ${shot.shotNumber} of 4",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted
                            )
                        )
                    }
                }

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse shot" else "Expand shot",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metadata Badges (Timeline, 16:9, Camera)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Timeline badge
                Row(
                    modifier = Modifier
                        .background(FlowAmber.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timeline",
                        tint = FlowAmber,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = shot.timeline,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = FlowAmber,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                // Aspect Ratio 16:9 badge
                Row(
                    modifier = Modifier
                        .background(FlowPurple.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = "Aspect ratio",
                        tint = FlowPurple,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "16:9",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = FlowPurple,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Camera Technique badge
                if (shot.cameraType.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .background(FlowEmerald.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = FlowEmerald,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = shot.cameraType,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = FlowEmerald,
                                fontWeight = FontWeight.Medium
                            ),
                            maxLines = 1
                        )
                    }
                }
            }

            // Narrative Beat summary
            if (shot.narrativeBeat.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = shot.narrativeBeat,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 19.sp
                    )
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // First Frame Prompt
                    CopyablePromptBox(
                        title = "First Frame Prompt",
                        badgeText = "KEYFRAME • 16:9",
                        promptText = shot.firstFramePrompt,
                        accentColor = FlowCyan,
                        testTagSuffix = "shot_${shot.shotNumber}_first_frame"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Motion Prompt
                    CopyablePromptBox(
                        title = "Flow AI Motion Prompt",
                        badgeText = "ANIMATION • 24FPS",
                        promptText = shot.motionPrompt,
                        accentColor = FlowEmerald,
                        testTagSuffix = "shot_${shot.shotNumber}_motion"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Flow Parameters
                    CopyablePromptBox(
                        title = "Flow AI Parameter Flags",
                        badgeText = "SYNTAX",
                        promptText = shot.flowAiParameters,
                        accentColor = FlowPurple,
                        testTagSuffix = "shot_${shot.shotNumber}_params"
                    )
                }
            }
        }
    }
}
