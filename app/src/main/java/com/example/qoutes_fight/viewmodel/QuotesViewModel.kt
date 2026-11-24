package com.example.qoutes_fight.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.qoutes_fight.data.Quote
import com.example.qoutes_fight.data.QuotesRepository

class QuotesViewModel : ViewModel() {

    var currentQuote by mutableStateOf(QuotesRepository.getRandomQuote())
        private set

    var fightQuoteLeft by mutableStateOf<Quote?>(null)
        private set

    var fightQuoteRight by mutableStateOf<Quote?>(null)
        private set

    var favorites by mutableStateOf(QuotesRepository.getFavorites())
        private set

    var leaderboard by mutableStateOf(QuotesRepository.getLeaderboard())
        private set

    var allQuotes by mutableStateOf(QuotesRepository.getQuotes())
        private set

    var categories by mutableStateOf(QuotesRepository.getCategories())
        private set

    var selectedCategory by mutableStateOf<String?>(null)
        private set

    var fightResult by mutableStateOf<Quote?>(null)
        private set

    var totalFights by mutableIntStateOf(0)
        private set

    init {
        loadNewFight()
    }

    fun nextQuote() {
        currentQuote = if (selectedCategory != null) {
            QuotesRepository.getQuotesByCategory(selectedCategory!!).random()
        } else {
            QuotesRepository.getRandomQuote()
        }
    }

    fun toggleFavorite(quoteId: Int) {
        QuotesRepository.toggleFavorite(quoteId)
        favorites = QuotesRepository.getFavorites()
        // Refresh current quote if it was toggled
        currentQuote = QuotesRepository.getQuotes().first { it.id == currentQuote.id }
        allQuotes = QuotesRepository.getQuotes()
    }

    fun selectCategory(category: String?) {
        selectedCategory = category
        if (category != null) {
            currentQuote = QuotesRepository.getQuotesByCategory(category).random()
        } else {
            currentQuote = QuotesRepository.getRandomQuote()
        }
    }

    fun loadNewFight() {
        fightResult = null
        val pair = QuotesRepository.getRandomPair()
        fightQuoteLeft = pair.first
        fightQuoteRight = pair.second
    }

    fun selectWinner(winnerId: Int) {
        val loserId = if (winnerId == fightQuoteLeft?.id) {
            fightQuoteRight?.id
        } else {
            fightQuoteLeft?.id
        }
        if (loserId != null) {
            QuotesRepository.recordWin(winnerId, loserId)
            totalFights++
            fightResult = QuotesRepository.getQuotes().first { it.id == winnerId }
            leaderboard = QuotesRepository.getLeaderboard()
            allQuotes = QuotesRepository.getQuotes()
        }
    }
}
