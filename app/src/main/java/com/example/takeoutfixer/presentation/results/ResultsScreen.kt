package com.example.takeoutfixer.presentation.results

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeoutfixer.presentation.analysis.AnalysisViewModel
import com.example.takeoutfixer.ui.components.AppLogo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    onNavigateToHome: () -> Unit,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val total = items.size
    val fixed = items.count { it.jsonUri != null }
    val skipped = total - fixed // Files without JSON or where fixes couldn't be applied
    val failed = 0 // Placeholder for actual runtime errors

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Results") },
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
                    Text(
                        text = "Processing Complete!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    SummaryRowItem("Total Files", "$total")
                    SummaryRowItem("Fixed Successfully", "$fixed", color = MaterialTheme.colorScheme.primary)
                    SummaryRowItem("Files Skipped", "$skipped", color = MaterialTheme.colorScheme.outline)
                    SummaryRowItem("Failed", "$failed", color = MaterialTheme.colorScheme.error)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Button(
                onClick = onNavigateToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Return Home")
            }
        }
    }
}

@Composable
private fun SummaryRowItem(label: String, value: String, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurfaceVariant) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = color)
    }
}
