package com.example.budgetly.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.annotations.PreviewApi
import kotlinx.coroutines.FlowPreview
/**
 * Main screen that lists all presentation demo screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresentationMainScreen(
    onNavigateToScreen: (PresentationScreens) -> Unit
) {
    val presentationTopics = listOf(
//        PresentationTopic(
//            screen = PresentationScreens.SideEffectsDemo,
//            title = "Side-effects",
//            description = "DisposableEffect, produceState, derivedStateOf, snapshotFlow",
//            icon = Icons.Default.Settings
//        ),
        PresentationTopic(
            screen = PresentationScreens.ModifierDemo,
            title = "Modifier",
            description = "Modifier Order, Chaining, Composed modifier",
            icon = Icons.Default.Build
        ),
        PresentationTopic(
            screen = PresentationScreens.ListDemo,
            title = "Lists",
            description = "LazyColumn, LazyRow, LazyVerticalGrid, LazyPagingItems",
            icon = Icons.Default.List
        ),
        PresentationTopic(
            screen = PresentationScreens.TextDemo,
            title = "Text Input",
            description = "TextField, OutlinedTextField",
            icon = Icons.Default.Edit
        ),
        PresentationTopic(
            screen = PresentationScreens.ImageDemo,
            title = "Images & Icons",
            description = "Icon, Image with Coil",
            icon = Icons.Default.Image
        ),
//        PresentationTopic(
//            screen = PresentationScreens.StateHoistingDemo,
//            title = "State Hoisting",
//            description = "Pattern for managing state in Compose",
//            icon = Icons.Default.TrendingUp
//        ),
        PresentationTopic(
            screen = PresentationScreens.ThemingDemo,
            title = "Theming",
            description = "Material Theme (Color, Typography, Shape, Dimens), Custom Theme",
            icon = Icons.Default.Palette
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Jetpack Compose Presentation") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(presentationTopics) { topic ->
                PresentationTopicCard(
                    topic = topic,
                    onClick = { onNavigateToScreen(topic.screen) }
                )
            }
        }
    }
}

data class PresentationTopic(
    val screen: PresentationScreens,
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun PresentationTopicCard(
    topic: PresentationTopic,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = topic.icon,
                contentDescription = topic.title,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = topic.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

