package com.example.qoutes_fight.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.qoutes_fight.data.Quote
import com.example.qoutes_fight.ui.theme.HeartRed

@Composable
fun HomeScreen(
    currentQuote: Quote,
    categories: List<String>,
    selectedCategory: String?,
    onNextQuote: () -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onSelectCategory: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "✨ Quote of the Moment",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { onSelectCategory(null) },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { onSelectCategory(category) },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quote card with animation
        AnimatedContent(
            targetState = currentQuote,
            transitionSpec = {
                (fadeIn() + scaleIn(
                    initialScale = 0.92f,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )).togetherWith(fadeOut() + scaleOut(targetScale = 0.92f))
            },
            label = "quote_animation",
            modifier = Modifier.weight(1f)
        ) { quote ->
            QuoteCard(
                quote = quote,
                onToggleFavorite = onToggleFavorite,
                onShare = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "\"${quote.displayText}\"\n— ${quote.author}\n\nShared via Quotes Fight 🥊"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Next quote button
        androidx.compose.material3.FilledTonalButton(
            onClick = onNextQuote,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Next Quote",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text("Next Quote", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun QuoteCard(
    quote: Quote,
    onToggleFavorite: (Int) -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "❝",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = quote.displayText,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = MaterialTheme.typography.headlineSmall.lineHeight
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "— ${quote.author}",
                style = MaterialTheme.typography.titleMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category badge
            androidx.compose.material3.AssistChip(
                onClick = {},
                label = {
                    Text(
                        text = quote.category,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onToggleFavorite(quote.id) }) {
                    Icon(
                        imageVector = if (quote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (quote.isFavorite) "Remove from favorites" else "Add to favorites",
                        tint = if (quote.isFavorite) HeartRed else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = onShare) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}
