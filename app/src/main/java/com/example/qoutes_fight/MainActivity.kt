package com.example.qoutes_fight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.qoutes_fight.ui.screens.FavoritesScreen
import com.example.qoutes_fight.ui.screens.FightScreen
import com.example.qoutes_fight.ui.screens.HomeScreen
import com.example.qoutes_fight.ui.screens.LeaderboardScreen
import com.example.qoutes_fight.ui.theme.QoutesfightTheme
import com.example.qoutes_fight.viewmodel.QuotesViewModel

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QoutesfightTheme {
                QuotesFightApp()
            }
        }
    }
}

@Composable
fun QuotesFightApp(viewModel: QuotesViewModel = viewModel()) {
    val navItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("Fight", Icons.Filled.Star, Icons.Outlined.Star),
        BottomNavItem("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavItem("Ranks", Icons.Filled.Star, Icons.Outlined.Star),
    )

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                (fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)) +
                        scaleIn(initialScale = 0.96f)).togetherWith(
                    fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium)) +
                            scaleOut(targetScale = 0.96f)
                )
            },
            label = "tab_transition",
            modifier = Modifier.padding(innerPadding)
        ) { tab ->
            when (tab) {
                0 -> HomeScreen(
                    currentQuote = viewModel.currentQuote,
                    categories = viewModel.categories,
                    selectedCategory = viewModel.selectedCategory,
                    onNextQuote = viewModel::nextQuote,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onSelectCategory = viewModel::selectCategory
                )
                1 -> FightScreen(
                    quoteLeft = viewModel.fightQuoteLeft,
                    quoteRight = viewModel.fightQuoteRight,
                    fightResult = viewModel.fightResult,
                    totalFights = viewModel.totalFights,
                    onSelectWinner = viewModel::selectWinner,
                    onNewFight = viewModel::loadNewFight
                )
                2 -> FavoritesScreen(
                    favorites = viewModel.favorites,
                    onToggleFavorite = viewModel::toggleFavorite
                )
                3 -> LeaderboardScreen(
                    leaderboard = viewModel.leaderboard
                )
            }
        }
    }
}
