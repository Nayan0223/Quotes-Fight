package com.example.qoutes_fight.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.qoutes_fight.data.Quote
import com.example.qoutes_fight.ui.theme.FightBlue
import com.example.qoutes_fight.ui.theme.FightRed
import com.example.qoutes_fight.ui.theme.GoldAccent
import com.example.qoutes_fight.ui.theme.WinGreen

@Composable
fun FightScreen(
    quoteLeft: Quote?,
    quoteRight: Quote?,
    fightResult: Quote?,
    totalFights: Int,
    onSelectWinner: (Int) -> Unit,
    onNewFight: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🥊 Quote Fight!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Tap the quote you like more!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (totalFights > 0) {
            Text(
                text = "⚔️ Total Fights: $totalFights",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fight result
        AnimatedVisibility(
            visible = fightResult != null,
            enter = fadeIn() + scaleIn(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            fightResult?.let { winner ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = WinGreen.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🏆", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "Winner: ${winner.author}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = WinGreen
                        )
                    }
                }
            }
        }

        // Fight area
        if (quoteLeft != null && quoteRight != null) {
            // Left quote
            FightQuoteCard(
                quote = quoteLeft,
                label = "🔴 Fighter A",
                borderColor = FightRed,
                isWinner = fightResult?.id == quoteLeft.id,
                onClick = { onSelectWinner(quoteLeft.id) },
                modifier = Modifier.weight(1f)
            )

            // VS divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                val vsScale by animateFloatAsState(
                    targetValue = if (fightResult == null) 1f else 0.8f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "vs_scale"
                )
                Text(
                    text = "⚔️ VS ⚔️",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent,
                    modifier = Modifier.scale(vsScale)
                )
            }

            // Right quote
            FightQuoteCard(
                quote = quoteRight,
                label = "🔵 Fighter B",
                borderColor = FightBlue,
                isWinner = fightResult?.id == quoteRight.id,
                onClick = { onSelectWinner(quoteRight.id) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // New fight button
        FilledTonalButton(
            onClick = onNewFight,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "New Fight",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = if (fightResult != null) "Next Fight!" else "New Fight",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun FightQuoteCard(
    quote: Quote,
    label: String,
    borderColor: androidx.compose.ui.graphics.Color,
    isWinner: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isWinner) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    OutlinedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        border = BorderStroke(
            width = if (isWinner) 3.dp else 1.5.dp,
            color = if (isWinner) WinGreen else borderColor
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isWinner) {
                WinGreen.copy(alpha = 0.08f)
            } else {
                borderColor.copy(alpha = 0.05f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (isWinner) WinGreen else borderColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${quote.text}\"",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "— ${quote.author}",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isWinner) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🏆 Winner!",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = WinGreen
                )
            }
        }
    }
}
