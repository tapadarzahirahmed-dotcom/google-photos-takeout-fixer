package com.example.takeoutfixer.presentation.home

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeoutfixer.R
import com.example.takeoutfixer.ui.components.AppLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToScan: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val selectedUri by viewModel.selectedFolderUri.collectAsState()
    val outputUri by viewModel.outputFolderUri.collectAsState()
    var showInstructions by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "heartBeat")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartScale"
    )
    
    val sourceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let { viewModel.onFolderSelected(it) }
    }

    val outputLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let { viewModel.onOutputFolderSelected(it) }
    }

    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            title = { Text("About Google Photos Takeout Fixer") },
            text = {
                val scrollState = rememberScrollState()
                Column(modifier = Modifier.verticalScroll(scrollState)) {
                    Text(
                        text = "A lightweight, fast, and secure Android utility to restore metadata (Date Taken, GPS, Descriptions) to photos and videos exported via Google Takeout.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("🌟 Features", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    FeatureItem("Parallel Scanning", "High-speed O(N) scanning using Coroutines to handle thousands of files instantly.")
                    FeatureItem("Robust JSON Matching", "Handles Google Takeout's inconsistent naming, suffixes, and filename truncations.")
                    FeatureItem("Comprehensive Metadata Fixes", "")
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text("• Images: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text("Restores Date Taken, GPS (including Altitude), and Captions/Descriptions using androidx.exifinterface.", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• Videos: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text("Restores Creation Time and Descriptions using FFmpeg without re-encoding (no quality loss).", style = MaterialTheme.typography.bodySmall)
                    }
                    FeatureItem("Safety First", "Replicates original folder structures in a dedicated output directory to keep your originals untouched.")
                    FeatureItem("Material 3 UI", "Clean, responsive interface with Dark Mode support and dynamic coloring.")
                    FeatureItem("Social Connectivity", "Integrated links to LinkedIn and GitHub directly in the app.")
                    FeatureItem("Real-time Progress", "System notifications and in-app progress tracking.")
                }
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("Close")
                }
            }
        )
    }

    if (showInstructions) {
        AlertDialog(
            onDismissRequest = { showInstructions = false },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            title = { Text("How to use the app") },
            text = {
                Column {
                    InstructionStep(1, "Unzip your Google Photos Takeout zip files into a folder.")
                    InstructionStep(2, "Select that unzipped folder as your Source Folder.")
                    InstructionStep(3, "Select an empty Output Folder where you want the fixed files to be saved.")
                    InstructionStep(4, "Click 'Start Scan' to analyze your files and metadata.")
                    InstructionStep(5, "Review the matches in 'Preview Fixes'.")
                    InstructionStep(6, "Click 'Apply Fixes' and wait for the process to complete. Do not go back!")
                }
            },
            confirmButton = {
                TextButton(onClick = { showInstructions = false }) {
                    Text("Got it")
                }
            }
        )
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { },
                actions = {
                    IconButton(onClick = { uriHandler.openUri("https://www.linkedin.com/in/zahir49") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_linkedin),
                            contentDescription = "LinkedIn"
                        )
                    }
                    IconButton(onClick = { uriHandler.openUri("https://github.com/tapadarzahirahmed-dotcom") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_github),
                            contentDescription = "GitHub"
                        )
                    }
                    IconButton(onClick = { showAbout = true }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                    IconButton(onClick = { showInstructions = true }) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "How to use")
                    }
                }
            ) 
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                AppLogo(size = 220.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Google Photos Takeout Fixer",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Created By - Zahir",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                // Source Folder
                OutlinedCard(
                    onClick = { sourceLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("1. Select Takeout Folder", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = selectedUri?.path ?: "Not selected",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Output Folder
                OutlinedCard(
                    onClick = { outputLauncher.launch(null) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("2. Select Output Folder", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = outputUri?.path ?: "Default: Same as source (Overwrites!)",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                }
                
                if (selectedUri != null) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = onNavigateToScan,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Scan")
                    }
                }
            }

            // Bottom Support Section
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "This app is free to use. If it has made your day a little easier, you can support future development",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(24.dp * scale)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(onClick = { uriHandler.openUri("https://razorpay.me/@zahir") }) {
                        Text("Razorpay")
                    }
                    
                    Text(
                        "|",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                    
                    TextButton(onClick = {
                        val upiUri = Uri.parse("upi://pay?pa=zahirahmedt-1@okicici&pn=Zahir+Ahmed&cu=INR")
                        val intent = Intent(Intent.ACTION_VIEW, upiUri)
                        val chooser = Intent.createChooser(intent, "Pay with...")
                        context.startActivity(chooser)
                    }) {
                        Text("UPI")
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureItem(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "• $title",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
private fun InstructionStep(number: Int, text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = "$number. ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
