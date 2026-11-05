package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BorderDark
import com.example.ui.theme.FlowAmber
import com.example.ui.theme.FlowCyan
import com.example.ui.theme.FlowEmerald
import com.example.ui.theme.FlowPurple
import com.example.ui.theme.SurfaceContainerDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FlowAiUiState
import com.example.ui.viewmodel.FlowAiViewModel

@Composable
fun StudioTab(
    viewModel: FlowAiViewModel,
    onNavigateToStoryboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    val storyInput by viewModel.storyInput.collectAsState()
    val selectedStyle by viewModel.selectedStyle.collectAsState()
    val shotDuration by viewModel.shotDuration.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val scrollState = rememberScrollState()
    val isGenerating = uiState is FlowAiUiState.Generating

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Hero Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF131D2E), Color(0xFF1A1528))
                    )
                )
                .border(
                    1.dp,
                    Brush.horizontalGradient(listOf(FlowCyan.copy(alpha = 0.5f), FlowPurple.copy(alpha = 0.5f))),
                    RoundedCornerShape(20.dp)
                )
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF090D14))
                        .border(1.dp, FlowCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = R.drawable.flow_app_icon),
                        contentDescription = "Flow AI Icon",
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "FLOW AI",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = FlowCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(FlowPurple.copy(alpha = 0.25f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "16:9 ANIMATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = FlowPurple,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Consistent characters, 4-shot storyboards & motion prompts for Flow AI video",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Preset Stories section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = FlowAmber,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "SAMPLE STORY PRESETS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = FlowAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
            Text(
                text = "Tap to load",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Preset horizontal row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.storyPresets.forEach { preset ->
                val isSelected = storyInput == preset.story
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) FlowPurple.copy(alpha = 0.25f) else SurfaceContainerDark
                        )
                        .border(
                            1.dp,
                            if (isSelected) FlowPurple else BorderDark,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.loadPreset(preset) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = preset.icon, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = preset.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Story Input Field
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ENTER YOUR STORY",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = FlowCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
            if (storyInput.isNotBlank()) {
                Text(
                    text = "${storyInput.length} chars",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = storyInput,
            onValueChange = { viewModel.updateStory(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .testTag("story_input"),
            placeholder = {
                Text(
                    text = "Describe your story, characters, and key actions. Flow AI will turn it into 4 cinematic shots with locked character IDs...",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted)
                )
            },
            trailingIcon = {
                if (storyInput.isNotBlank()) {
                    IconButton(onClick = { viewModel.clearStory() }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear story",
                            tint = TextMuted
                        )
                    }
                }
            },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceDark,
                unfocusedContainerColor = SurfaceDark,
                focusedBorderColor = FlowCyan,
                unfocusedBorderColor = BorderDark,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = FlowCyan
            )
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Visual Art Style Selector
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Style,
                contentDescription = null,
                tint = FlowPurple,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "ANIMATION ART STYLE",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = FlowPurple,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.styleOptions.forEach { style ->
                val isSelected = selectedStyle == style
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) FlowCyan.copy(alpha = 0.2f) else SurfaceContainerDark)
                        .border(
                            1.dp,
                            if (isSelected) FlowCyan else BorderDark,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.setStyle(style) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = style,
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = if (isSelected) FlowCyan else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Shot Duration & Aspect Ratio Setting
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shot Duration
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = FlowEmerald,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SHOT DURATION",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = FlowEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    viewModel.durationOptions.forEach { sec ->
                        val isSelected = shotDuration == sec
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) FlowEmerald.copy(alpha = 0.25f) else SurfaceContainerDark)
                                .border(1.dp, if (isSelected) FlowEmerald else BorderDark, RoundedCornerShape(8.dp))
                                .clickable { viewModel.setShotDuration(sec) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${sec}s / shot",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) FlowEmerald else TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Aspect Ratio badge
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = FlowCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ASPECT RATIO",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = FlowCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FlowCyan.copy(alpha = 0.15f))
                        .border(1.dp, FlowCyan, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "16:9 Widescreen",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = FlowCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Generate Button
        Button(
            onClick = { viewModel.generatePrompts() },
            enabled = !isGenerating && storyInput.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("generate_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FlowCyan,
                contentColor = Color(0xFF002930),
                disabledContainerColor = Color(0xFF1E293B),
                disabledContentColor = Color(0xFF64748B)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFF002930),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GENERATING PROMPTS...",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GENERATE FLOW PROMPTS (4 SHOTS)",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }

        // Generating progress step indicator
        AnimatedVisibility(visible = isGenerating) {
            val generatingState = uiState as? FlowAiUiState.Generating
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .background(Color(0xFF090D14), RoundedCornerShape(12.dp))
                    .border(1.dp, FlowCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = generatingState?.step ?: "Generating Flow AI prompts...",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = FlowCyan,
                        fontWeight = FontWeight.Medium
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Analyzing character traits, setting 16:9 framing, and writing motion physics",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                )
            }
        }

        // Error card
        AnimatedVisibility(visible = uiState is FlowAiUiState.Error) {
            val errorState = uiState as? FlowAiUiState.Error
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
                    .background(Color(0xFF2D1216), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "Generation Error",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = errorState?.message ?: "Unknown error occurred.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFFFCA5A5))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { viewModel.generatePrompts() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Retry")
                }
            }
        }

        // Success quick action banner
        AnimatedVisibility(visible = uiState is FlowAiUiState.Success) {
            val project = (uiState as? FlowAiUiState.Success)?.project
            if (project != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(FlowEmerald.copy(alpha = 0.12f))
                        .border(1.dp, FlowEmerald, RoundedCornerShape(14.dp))
                        .clickable { onNavigateToStoryboard() }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✨ \"${project.title}\" Ready!",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = FlowEmerald,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "${project.characters.size} Characters • 4 Shots (${project.totalRuntimeSeconds}s)",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                        Button(
                            onClick = onNavigateToStoryboard,
                            colors = ButtonDefaults.buttonColors(containerColor = FlowEmerald),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("View Storyboard")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
