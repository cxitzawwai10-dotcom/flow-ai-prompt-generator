package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.model.FlowProject
import com.example.ui.components.ShotCard
import com.example.ui.components.TimelineOverview
import com.example.ui.theme.BorderDark
import com.example.ui.theme.FlowCyan
import com.example.ui.theme.FlowPurple
import com.example.ui.theme.SurfaceContainerDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FlowAiViewModel

@Composable
fun StoryboardTab(
    viewModel: FlowAiViewModel,
    onNavigateToStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsState()
    var selectedShotIndex by remember { mutableStateOf<Int?>(null) }

    if (currentProject == null || currentProject?.shots?.isEmpty() == true) {
        EmptyStoryboardState(onNavigateToStudio = onNavigateToStudio, modifier = modifier)
        return
    }

    val project = currentProject!!
    val displayedShots = if (selectedShotIndex != null && selectedShotIndex!! in project.shots.indices) {
        listOf(project.shots[selectedShotIndex!!])
    } else {
        project.shots
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Project Header Card
            ProjectHeaderCard(
                project = project,
                onSave = { viewModel.saveCurrentProject() }
            )
        }

        item {
            // Interactive Timeline
            TimelineOverview(
                shots = project.shots,
                selectedShotIndex = selectedShotIndex,
                onShotSelect = { selectedShotIndex = it }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedShotIndex != null) "SHOT ${selectedShotIndex!! + 1} OF 4" else "ALL 4 STORYBOARD SHOTS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                if (selectedShotIndex != null) {
                    FilledTonalButton(
                        onClick = { selectedShotIndex = null },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Show All 4", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        items(displayedShots, key = { it.shotNumber }) { shot ->
            ShotCard(shot = shot)
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun ProjectHeaderCard(
    project: FlowProject,
    onSave: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainerDark, RoundedCornerShape(16.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = project.visualStyle,
                        style = MaterialTheme.typography.labelSmall.copy(color = FlowCyan)
                    )
                }

                FilledTonalButton(
                    onClick = onSave,
                    modifier = Modifier.testTag("save_project_header_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = "Save Storyboard",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save", style = MaterialTheme.typography.labelSmall)
                }
            }

            if (project.logline.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = project.logline,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .background(FlowPurple.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "16:9 Aspect Ratio",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = FlowPurple,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .background(FlowCyan.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${project.totalRuntimeSeconds}s Total (${project.shotDurationSeconds}s/shot)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = FlowCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStoryboardState(
    onNavigateToStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(SurfaceContainerDark, RoundedCornerShape(20.dp))
                .border(1.dp, BorderDark, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MovieFilter,
                contentDescription = null,
                tint = FlowCyan,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "No Storyboard Generated Yet",
            style = MaterialTheme.typography.titleLarge.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Head to the Studio tab, enter a story or pick a preset, and tap Generate to create your 4-shot Flow AI animation storyboard.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondary,
                lineHeight = 22.sp
            ),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNavigateToStudio,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FlowCyan, contentColor = Color(0xFF003038))
        ) {
            Icon(imageVector = Icons.Default.PlayCircleOutline, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go to Studio")
        }
    }
}
