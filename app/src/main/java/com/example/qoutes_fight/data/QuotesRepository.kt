package com.example.qoutes_fight.data

object QuotesRepository {

    private val allQuotes = mutableListOf(
        // Motivation
        Quote(1, "The only way to do great work is to love what you do.", "Steve Jobs", "Motivation"),
        Quote(2, "Believe you can and you're halfway there.", "Theodore Roosevelt", "Motivation"),
        Quote(3, "It does not matter how slowly you go as long as you do not stop.", "Confucius", "Motivation"),
        Quote(4, "Success is not final, failure is not fatal: it is the courage to continue that counts.", "Winston Churchill", "Motivation"),
        Quote(5, "The future belongs to those who believe in the beauty of their dreams.", "Eleanor Roosevelt", "Motivation"),
        Quote(6, "Don't watch the clock; do what it does. Keep going.", "Sam Levenson", "Motivation"),
        Quote(7, "Everything you've ever wanted is on the other side of fear.", "George Addair", "Motivation"),
        Quote(8, "Hardships often prepare ordinary people for an extraordinary destiny.", "C.S. Lewis", "Motivation"),

        // Life
        Quote(9, "In the middle of every difficulty lies opportunity.", "Albert Einstein", "Life"),
        Quote(10, "Life is what happens when you're busy making other plans.", "John Lennon", "Life"),
        Quote(11, "The purpose of our lives is to be happy.", "Dalai Lama", "Life"),
        Quote(12, "Life is really simple, but we insist on making it complicated.", "Confucius", "Life"),
        Quote(13, "Get busy living or get busy dying.", "Stephen King", "Life"),
        Quote(14, "You only live once, but if you do it right, once is enough.", "Mae West", "Life"),
        Quote(15, "Life is a journey, not a destination.", "Ralph Waldo Emerson", "Life"),

        // Wisdom
        Quote(16, "The only true wisdom is in knowing you know nothing.", "Socrates", "Wisdom"),
        Quote(17, "In three words I can sum up everything I learned about life: it goes on.", "Robert Frost", "Wisdom"),
        Quote(18, "Turn your wounds into wisdom.", "Oprah Winfrey", "Wisdom"),
        Quote(19, "The mind is everything. What you think you become.", "Buddha", "Wisdom"),
        Quote(20, "Knowledge speaks, but wisdom listens.", "Jimi Hendrix", "Wisdom"),

        // Humor
        Quote(21, "I'm not lazy, I'm on energy saving mode.", "Unknown", "Humor"),
        Quote(22, "I didn't fail the test. I just found 100 ways to do it wrong.", "Benjamin Franklin", "Humor"),
        Quote(23, "Life is short. Smile while you still have teeth.", "Mallory Hopkins", "Humor"),
        Quote(24, "I am so clever that sometimes I don't understand a single word of what I am saying.", "Oscar Wilde", "Humor"),
        Quote(25, "Behind every great man is a woman rolling her eyes.", "Jim Carrey", "Humor"),

        // Love
        Quote(26, "The best thing to hold onto in life is each other.", "Audrey Hepburn", "Love"),
        Quote(27, "Love all, trust a few, do wrong to none.", "William Shakespeare", "Love"),
        Quote(28, "Where there is love there is life.", "Mahatma Gandhi", "Love"),
        Quote(29, "Being deeply loved by someone gives you strength, while loving someone deeply gives you courage.", "Lao Tzu", "Love"),
        Quote(30, "The greatest thing you'll ever learn is just to love and be loved in return.", "Nat King Cole", "Love"),

        // Strength
        Quote(31, "What doesn't kill you makes you stronger.", "Friedrich Nietzsche", "Strength"),
        Quote(32, "Strength does not come from physical capacity. It comes from an indomitable will.", "Mahatma Gandhi", "Strength"),
        Quote(33, "You never know how strong you are until being strong is your only choice.", "Bob Marley", "Strength"),
        Quote(34, "The world breaks everyone, and afterward, some are strong at the broken places.", "Ernest Hemingway", "Strength"),
        Quote(35, "Fall seven times, stand up eight.", "Japanese Proverb", "Strength"),
    )

    fun getQuotes(): List<Quote> = allQuotes.toList()

    fun getRandomQuote(): Quote = allQuotes.random()

    fun getQuotesByCategory(category: String): List<Quote> =
        allQuotes.filter { it.category == category }

    fun getCategories(): List<String> = allQuotes.map { it.category }.distinct()

    fun getFavorites(): List<Quote> = allQuotes.filter { it.isFavorite }

    fun toggleFavorite(quoteId: Int) {
        val index = allQuotes.indexOfFirst { it.id == quoteId }
        if (index != -1) {
            allQuotes[index] = allQuotes[index].copy(isFavorite = !allQuotes[index].isFavorite)
        }
    }

    fun getRandomPair(): Pair<Quote, Quote> {
        val shuffled = allQuotes.shuffled()
        return Pair(shuffled[0], shuffled[1])
    }

    fun recordWin(winnerId: Int, loserId: Int) {
        val winnerIndex = allQuotes.indexOfFirst { it.id == winnerId }
        val loserIndex = allQuotes.indexOfFirst { it.id == loserId }
        if (winnerIndex != -1) {
            allQuotes[winnerIndex] = allQuotes[winnerIndex].copy(wins = allQuotes[winnerIndex].wins + 1)
        }
        if (loserIndex != -1) {
            allQuotes[loserIndex] = allQuotes[loserIndex].copy(losses = allQuotes[loserIndex].losses + 1)
        }
    }

    fun getLeaderboard(): List<Quote> =
        allQuotes.filter { it.wins + it.losses > 0 }.sortedByDescending { it.winRate }
}
