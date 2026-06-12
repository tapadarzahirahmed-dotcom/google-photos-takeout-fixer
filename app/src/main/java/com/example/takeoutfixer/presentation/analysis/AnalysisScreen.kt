package com.example.takeoutfixer.presentation.analysis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.takeoutfixer.domain.models.FolderNode
import com.example.takeoutfixer.domain.models.ItemType
import com.example.takeoutfixer.ui.components.AppLogo
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    onNavigateToPreview: () -> Unit,
    onNavigateToProcessing: () -> Unit,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val folderStructure by viewModel.folderStructure.collectAsState()
    
    val totalMediaSize = items.sumOf { it.size }
    val totalJsonSize = items.sumOf { it.jsonSize }
    val overallTotalSize = totalMediaSize + totalJsonSize
    
    val images = items.filter { it.type == ItemType.IMAGE }
    val totalImageSize = images.sumOf { it.size }
    val imageMatched = images.count { it.jsonUri != null }
    val imageUnmatched = images.size - imageMatched

    val videos = items.filter { it.type == ItemType.VIDEO }
    val totalVideoSize = videos.sumOf { it.size }
    val videoMatched = videos.count { it.jsonUri != null }
    val videoUnmatched = videos.size - videoMatched

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Analysis Result") },
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
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 800.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Text(
                            "Summary",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                SummaryRow("Overall Total Items", "${items.size}")
                                SummaryRow("Overall Total Size", formatPreciseSize(overallTotalSize))
                                
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                                
                                Text("Images", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                                SummaryRow("Total Images", "${images.size}")
                                SummaryRow("Total Image Size", formatPreciseSize(totalImageSize))
                                SummaryRow("JSON Matches", "$imageMatched")
                                SummaryRow("Missing JSON", "$imageUnmatched", color = MaterialTheme.colorScheme.error)

                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Videos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                                SummaryRow("Total Videos", "${videos.size}")
                                SummaryRow("Total Video Size", formatPreciseSize(totalVideoSize))
                                SummaryRow("JSON Matches", "$videoMatched")
                                SummaryRow("Missing JSON", "$videoUnmatched", color = MaterialTheme.colorScheme.error)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            "Takeout Folder Structure",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .horizontalScroll(rememberScrollState())
                            ) {
                                folderStructure?.let { root ->
                                    Column {
                                        FolderTreeView(root, 0)
                                    }
                                } ?: Box(
                                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
                
                // Bottom Action Buttons
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = onNavigateToPreview,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Preview Fixes")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onNavigateToProcessing,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Apply Fixes")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FolderTreeView(node: FolderNode, depth: Int) {
    var isExpanded by remember { mutableStateOf(depth < 1) }

    Column(modifier = Modifier.padding(start = (if (depth > 0) 12 else 0).dp)) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (node.isDirectory) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Spacer(modifier = Modifier.width(20.dp))
            }
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Icon(
                imageVector = when {
                    node.isDirectory -> Icons.Default.Folder
                    node.type == ItemType.IMAGE -> Icons.Default.Photo
                    node.type == ItemType.VIDEO -> Icons.Default.VideoFile
                    else -> Icons.AutoMirrored.Filled.InsertDriveFile
                },
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = when {
                    node.isDirectory -> MaterialTheme.colorScheme.secondary
                    node.type == ItemType.IMAGE -> MaterialTheme.colorScheme.primary
                    node.type == ItemType.VIDEO -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.outline
                }
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = node.name,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                maxLines = 1,
                fontWeight = if (node.isDirectory) FontWeight.SemiBold else FontWeight.Normal
            )
        }

        if (node.isDirectory) {
            AnimatedVisibility(visible = isExpanded) {
                Column {
                    node.children.sortedByDescending { it.isDirectory }.forEach { child ->
                        FolderTreeView(child, depth + 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = color)
    }
}

private fun formatPreciseSize(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val exp = (Math.log(bytes.toDouble()) / Math.log(1024.0)).toInt()
    val pre = "KMGTPE"[exp - 1]
    return String.format(Locale.US, "%.2f %sB", bytes / Math.pow(1024.0, exp.toDouble()), pre)
}
