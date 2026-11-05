package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BorderDark
import com.example.ui.theme.FlowCyan
import com.example.ui.theme.FlowEmerald
import com.example.ui.theme.FlowPurple
import com.example.ui.theme.RobotoMonoFont
import com.example.ui.theme.SurfaceContainerDark
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.FlowAiViewModel

@Composable
fun ScriptTab(
    viewModel: FlowAiViewModel,
    onNavigateToStudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentProject by viewModel.currentProject.collectAsState()

    if (currentProject == null || currentProject?.shots?.isEmpty() == true) {
        EmptyScriptState(onNavigateToStudio = onNavigateToStudio, modifier = modifier)
        return
    }

    val project = currentProject!!
    val fullScript = project.buildFullScript()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Action Bar: Copy All, Share, Save
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Flow AI Script - ${project.title}", fullScript)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "Full Flow AI script copied to clipboard!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .weight(1.3f)
                    .height(48.dp)
                    .testTag("copy_all_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = FlowCyan,
                    contentColor = Color(0xFF00323B)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy All",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "COPY ALL SCRIPT",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            FilledTonalButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_SUBJECT, "Flow AI Animation Script: ${project.title}")
                        putExtra(Intent.EXTRA_TEXT, fullScript)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Flow AI Script"))
                },
                modifier = Modifier
                    .weight(0.9f)
                    .height(48.dp)
                    .testTag("share_script_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Share", style = MaterialTheme.typography.labelMedium)
            }

            OutlinedButton(
                onClick = { viewModel.saveCurrentProject() },
                modifier = Modifier
                    .weight(0.9f)
                    .height(48.dp)
                    .testTag("save_project_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = "Save",
                    tint = FlowPurple,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Save", style = MaterialTheme.typography.labelMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Script Preview Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceDark, RoundedCornerShape(14.dp))
                .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "FORMATTED FLOW AI SCRIPT",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = FlowEmerald,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Text(
                        text = "Ready to paste",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF070A0F), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0xFF1B2332), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = fullScript,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = RobotoMonoFont,
                            color = Color(0xFFE2E8F0),
                            lineHeight = 19.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun EmptyScriptState(
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
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = FlowCyan,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "No Script Generated Yet",
            style = MaterialTheme.typography.titleLarge.copy(
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Generate a storyboard in the Studio to create an exportable Flow AI animation script complete with character IDs, reference prompts, and 4 shots.",
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
