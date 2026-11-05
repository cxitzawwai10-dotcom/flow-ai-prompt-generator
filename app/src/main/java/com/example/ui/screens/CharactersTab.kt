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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CharacterCard
import com.example.ui.theme.BorderDark
import com.example.ui.theme.FlowCyan
import com.example.ui.theme.FlowPurple
import com.example.ui.theme.SurfaceContainerDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FlowAiViewModel

@Composable
fun CharactersTab(
    viewModel: FlowAiViewModel,
    onNavigateToStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProject by viewModel.currentProject.collectAsState()

    if (currentProject == null || currentProject?.characters?.isEmpty() == true) {
        EmptyCharactersState(onNavigateToStudio = onNavigateToStudio, modifier = modifier)
        return
    }

    val project = currentProject!!

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Consistency Guide Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF13192B), RoundedCornerShape(14.dp))
                    .border(1.dp, FlowPurple.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = FlowPurple,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Character Consistency in Flow AI",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = FlowPurple,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Every shot in the storyboard begins with the character's unique ID tag (e.g. [CHAR_1: ${project.characters.firstOrNull()?.name ?: "Name"}]). Use the Reference Prompts below to generate consistent key art / turnaround sheets in Midjourney or Flow AI before animating.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "IDENTIFIED CHARACTERS (${project.characters.size})",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextPrimary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
        }

        items(project.characters, key = { it.id }) { character ->
            CharacterCard(character = character)
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun EmptyCharactersState(
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
                imageVector = Icons.Default.PersonOutline,
                contentDescription = null,
                tint = FlowPurple,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "No Characters Extracted",
            style = MaterialTheme.typography.titleLarge.copy(
                color = TextPrimary,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Generate a story in the Studio to automatically extract characters with persistent IDs and turnaround reference prompts.",
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
            colors = ButtonDefaults.buttonColors(containerColor = FlowPurple)
        ) {
            Icon(imageVector = Icons.Default.PlayCircleOutline, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go to Studio")
        }
    }
}
