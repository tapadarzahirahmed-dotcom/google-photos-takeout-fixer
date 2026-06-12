package com.example.takeoutfixer.presentation.scan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeoutfixer.presentation.home.HomeViewModel
import com.example.takeoutfixer.ui.components.AppLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    onNavigateToAnalysis: () -> Unit,
    scanViewModel: ScanViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel() // Shared logic would be better
) {
    val progress by scanViewModel.progress.collectAsState()
    val selectedUri by homeViewModel.selectedFolderUri.collectAsState()

    LaunchedEffect(selectedUri) {
        android.util.Log.e("TakeoutScan", "ScanScreen: selectedUri=$selectedUri, progress=$progress")
        if (selectedUri != null && progress == null) {
            android.util.Log.e("TakeoutScan", "ScanScreen: Triggering scan")
            scanViewModel.startScan(selectedUri!!)
        }
    }

    if (progress?.isComplete == true) {
        val navigateToAnalysis = remember { onNavigateToAnalysis }
        LaunchedEffect(Unit) { navigateToAnalysis() }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Scanning...") },
                actions = { 
                    AppLogo(size = 72.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            ) 
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .widthIn(max = 600.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
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
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Files scanned: ${progress?.totalFiles ?: 0}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Items found: ${progress?.itemsFound ?: 0}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = progress?.currentFile ?: "Initializing...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
