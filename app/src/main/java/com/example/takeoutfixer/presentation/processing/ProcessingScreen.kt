package com.example.takeoutfixer.presentation.processing

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeoutfixer.ui.components.AppLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcessingScreen(
    onNavigateToResults: () -> Unit,
    outputUri: android.net.Uri? = null,
    viewModel: ProcessingViewModel = hiltViewModel()
) {
    val progress by viewModel.progress.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startFixing(outputUri)
    }

    if (progress?.isComplete == true) {
        val navigateToResults = remember { onNavigateToResults }
        LaunchedEffect(Unit) { navigateToResults() }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Processing...") },
                actions = { 
                    AppLogo(size = 72.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            ) 
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Warning Message
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Do not go back.",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Doing so will cancel the fixing process.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "You can switch apps in the meantime.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Background Card for Progress
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val percent = if (progress != null && progress!!.totalItems > 0) {
                        progress!!.processedItems.toFloat() / progress!!.totalItems.toFloat()
                    } else 0f
                    
                    LinearProgressIndicator(
                        progress = percent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        strokeCap = StrokeCap.Round
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Processed: ${progress?.processedItems ?: 0} / ${progress?.totalItems ?: 0}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = progress?.currentFile ?: "Waiting...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
