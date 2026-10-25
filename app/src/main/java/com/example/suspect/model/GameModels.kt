package com.example.suspect.model

import java.util.UUID

enum class AppScreen {
    HOME,
    PLAYER_SETUP,
    
    // Game 1: Number Justify
    G1_ASSIGNMENT,
    G1_INPUT,
    G1_REVEAL,
    G1_DISCUSS,
    G1_VOTE,
    G1_RESULTS,

    // Game 2: Impostor Word
    G2_CATEGORY_SELECT,
    G2_REVEAL_FLOW,
    G2_PASS_PHONE,
    G2_GAMEPLAY,
    G2_VOTE,
    G2_RESULTS
}

enum class GameMode {
    NUMBER_JUSTIFY,
    IMPOSTOR_WORD
}

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val avatarSeed: Int = 0,
    val isImpostor: Boolean = false,
    val score: Int = 0,
    val answer: String? = null
)

data class QuestionPair(
    val safe: String,
    val impostor: String,
    val type: String // "number" or "text"
)

data class WordPair(
    val safe: String,
    val impostor: String,
    val category: String
)

val CATEGORIES = listOf(
    "Random Mix",
    "Food",
    "Places",
    "Animals",
    "Activities",
    "Objects",
    "Jobs",
    "Nature"
)

data class GameState(
    val screen: AppScreen = AppScreen.HOME,
    val players: List<Player> = emptyList(),
    val activeGame: GameMode? = null,
    val imposterCount: Int = 1,
    val category: String = "Random Mix",
    val currentPlayerIndex: Int = 0,
    val questionSafe: String = "",
    val questionImpostor: String = "",
    val wordSafe: String = "",
    val wordImpostor: String = "",
    val timeLeft: Int = 120,
    val roundDuration: Int = 120,
    val isTimerRunning: Boolean = false,
    val votes: Map<String, String> = emptyMap() // e.g. "GROUP_DECISION" -> playerId
)
