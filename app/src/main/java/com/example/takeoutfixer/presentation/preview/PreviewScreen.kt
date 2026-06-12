package com.example.takeoutfixer.presentation.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeoutfixer.domain.models.TakeoutItem
import com.example.takeoutfixer.presentation.analysis.AnalysisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    onBack: () -> Unit,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fix Preview") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(items) { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    supportingContent = {
                        Text(if (item.jsonUri != null) "JSON Matched" else "No JSON found")
                    },
                    trailingContent = {
                        if (item.jsonUri != null) {
                            Text("Fixable", color = MaterialTheme.colorScheme.primary)
                        } else {
                            Text("Skip", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )
                Divider()
            }
        }
    }
}
