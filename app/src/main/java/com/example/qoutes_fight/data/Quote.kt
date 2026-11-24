package com.example.qoutes_fight.data

data class Quote(
    val id: Int,
    val text: String,
    val author: String,
    val category: String,
    var wins: Int = 0,
    var losses: Int = 0,
    var isFavorite: Boolean = false
) {
    val winRate: Float
        get() = if (wins + losses > 0) wins.toFloat() / (wins + losses) else 0f
}
