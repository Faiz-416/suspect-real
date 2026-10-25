package com.example.suspect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.suspect.data.GameQuestions
import com.example.suspect.model.AppScreen
import com.example.suspect.model.GameMode
import com.example.suspect.model.GameState
import com.example.suspect.model.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameState())
    val uiState: StateFlow<GameState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun selectGameMode(mode: GameMode) {
        _uiState.update { 
            it.copy(
                activeGame = mode,
                screen = AppScreen.PLAYER_SETUP
            ) 
        }
    }

    fun addPlayer(rawName: String) {
        val trimmed = rawName.trim()
        if (trimmed.isEmpty()) return
        val capName = trimmed.replaceFirstChar { it.uppercase() }
        
        _uiState.update { state ->
            if (state.players.any { it.name.equals(capName, ignoreCase = true) }) {
                state
            } else {
                state.copy(players = state.players + Player(name = capName))
            }
        }
    }

    fun removePlayer(id: String) {
        _uiState.update { state ->
            state.copy(players = state.players.filter { it.id != id })
        }
    }

    fun updateRoundDuration(duration: Int) {
        val clamped = duration.coerceIn(30, 300)
        _uiState.update { it.copy(roundDuration = clamped, timeLeft = clamped) }
    }

    fun updateImposterCount(count: Int) {
        _uiState.update { state ->
            val maxImp = max(1, state.players.size / 2)
            val clamped = count.coerceIn(1, maxImp)
            state.copy(imposterCount = clamped)
        }
    }

    fun proceedToGameSetup() {
        val state = _uiState.value
        if (state.activeGame == GameMode.NUMBER_JUSTIFY) {
            val q = GameQuestions.NUMBER_QUESTIONS.random()
            val cleanPlayers = setupGamePlayers(state.players, state.imposterCount)
            _uiState.update {
                it.copy(
                    players = cleanPlayers,
                    questionSafe = q.safe,
                    questionImpostor = q.impostor,
                    currentPlayerIndex = 0,
                    screen = AppScreen.G1_ASSIGNMENT
                )
            }
        } else {
            _uiState.update { it.copy(screen = AppScreen.G2_CATEGORY_SELECT) }
        }
    }

    fun selectCategory(cat: String) {
        val state = _uiState.value
        val pool = if (cat == "Random Mix") {
            GameQuestions.WORD_PAIRS
        } else {
            GameQuestions.WORD_PAIRS.filter { it.category == cat }
        }
        val pair = pool.ifEmpty { GameQuestions.WORD_PAIRS }.random()
        val cleanPlayers = setupGamePlayers(state.players, state.imposterCount)

        _uiState.update {
            it.copy(
                category = cat,
                players = cleanPlayers,
                wordSafe = pair.safe,
                wordImpostor = pair.impostor,
                currentPlayerIndex = 0,
                screen = AppScreen.G2_REVEAL_FLOW
            )
        }
    }

    private fun setupGamePlayers(players: List<Player>, imposterCount: Int): List<Player> {
        val count = max(1, min(imposterCount, players.size / 2))
        val indices = players.indices.shuffled()
        val impostorIndices = indices.take(count).toSet()

        return players.mapIndexed { index, p ->
            p.copy(
                isImpostor = index in impostorIndices,
                answer = null
            )
        }
    }

    fun goToG1Input() {
        _uiState.update { it.copy(screen = AppScreen.G1_INPUT) }
    }

    fun submitG1Answer(answer: String) {
        val state = _uiState.value
        val updatedPlayers = state.players.toMutableList()
        val currIdx = state.currentPlayerIndex
        updatedPlayers[currIdx] = updatedPlayers[currIdx].copy(answer = answer)

        val isLast = currIdx == state.players.size - 1
        _uiState.update {
            it.copy(
                players = updatedPlayers,
                currentPlayerIndex = if (isLast) 0 else currIdx + 1,
                screen = if (isLast) AppScreen.G1_REVEAL else AppScreen.G1_ASSIGNMENT
            )
        }
    }

    fun finishG2Reveal() {
        _uiState.update { it.copy(screen = AppScreen.G2_PASS_PHONE) }
    }

    fun passPhoneContinue() {
        val state = _uiState.value
        val isLast = state.currentPlayerIndex == state.players.size - 1
        if (isLast) {
            startDiscussion()
        } else {
            _uiState.update {
                it.copy(
                    currentPlayerIndex = it.currentPlayerIndex + 1,
                    screen = AppScreen.G2_REVEAL_FLOW
                )
            }
        }
    }

    fun startDiscussion() {
        stopTimer()
        _uiState.update {
            it.copy(
                timeLeft = it.roundDuration,
                isTimerRunning = true,
                screen = if (it.activeGame == GameMode.NUMBER_JUSTIFY) AppScreen.G1_DISCUSS else AppScreen.G2_GAMEPLAY
            )
        }
        startTimer()
    }

    fun toggleTimer() {
        if (_uiState.value.isTimerRunning) {
            stopTimer()
            _uiState.update { it.copy(isTimerRunning = false) }
        } else {
            _uiState.update { it.copy(isTimerRunning = true) }
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.isTimerRunning && _uiState.value.timeLeft > 0) {
                delay(1000L)
                _uiState.update {
                    val nextTime = it.timeLeft - 1
                    it.copy(
                        timeLeft = nextTime,
                        isTimerRunning = nextTime > 0
                    )
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun goToVote() {
        stopTimer()
        _uiState.update {
            it.copy(
                isTimerRunning = false,
                screen = if (it.activeGame == GameMode.NUMBER_JUSTIFY) AppScreen.G1_VOTE else AppScreen.G2_VOTE
            )
        }
    }

    fun toggleVote(playerId: String) {
        _uiState.update {
            it.copy(votes = mapOf("GROUP_DECISION" to playerId))
        }
    }

    fun confirmVote() {
        _uiState.update {
            it.copy(
                screen = if (it.activeGame == GameMode.NUMBER_JUSTIFY) AppScreen.G1_RESULTS else AppScreen.G2_RESULTS
            )
        }
    }

    fun playAgain() {
        stopTimer()
        _uiState.update { it.copy(votes = emptyMap(), isTimerRunning = false) }
        proceedToGameSetup()
    }

    fun changePlayers() {
        stopTimer()
        _uiState.update {
            it.copy(
                votes = emptyMap(),
                isTimerRunning = false,
                screen = AppScreen.PLAYER_SETUP
            )
        }
    }

    fun goHome() {
        stopTimer()
        _uiState.update {
            GameState(players = it.players) // Keep player list for convenience
        }
    }

    fun navigateBack() {
        val currentScreen = _uiState.value.screen
        when (currentScreen) {
            AppScreen.HOME -> { /* Handled in activity */ }
            AppScreen.PLAYER_SETUP -> {
                _uiState.update { it.copy(screen = AppScreen.HOME) }
            }
            else -> {
                _uiState.update { it.copy(screen = AppScreen.HOME) }
            }
        }
    }
}
