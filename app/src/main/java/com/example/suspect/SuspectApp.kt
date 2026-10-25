package com.example.suspect

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.suspect.model.AppScreen
import com.example.suspect.ui.components.ConfirmationModal
import com.example.suspect.ui.components.InfoModal
import com.example.suspect.ui.screens.*
import com.example.suspect.ui.theme.DarkBg
import com.example.suspect.ui.theme.TextMuted
import com.example.suspect.viewmodel.GameViewModel

@Composable
fun SuspectApp(
    viewModel: GameViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showExitConfirm by remember { mutableStateOf(false) }
    var showInfoModal by remember { mutableStateOf(false) }

    val handleBackPress = {
        if (uiState.screen == AppScreen.HOME) {
            // Let default back action happen
        } else if (uiState.screen == AppScreen.PLAYER_SETUP) {
            viewModel.navigateBack()
        } else {
            showExitConfirm = true
        }
    }

    if (uiState.screen != AppScreen.HOME) {
        BackHandler {
            handleBackPress()
        }
    }

    ConfirmationModal(
        isOpen = showExitConfirm,
        title = "Abort Mission?",
        message = "Leaving now will cancel the current game and lose all progress.",
        onConfirm = {
            showExitConfirm = false
            viewModel.goHome()
        },
        onCancel = { showExitConfirm = false }
    )

    InfoModal(
        isOpen = showInfoModal,
        onClose = { showInfoModal = false }
    )

    Scaffold(
        containerColor = DarkBg,
        topBar = {
            if (uiState.screen != AppScreen.HOME) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = { handleBackPress() },
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .testTag("app_top_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextMuted,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            AnimatedContent(
                targetState = uiState.screen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    AppScreen.HOME -> HomeScreen(
                        onSelectGameMode = { viewModel.selectGameMode(it) }
                    )
                    AppScreen.PLAYER_SETUP -> PlayerSetupScreen(
                        players = uiState.players,
                        roundDuration = uiState.roundDuration,
                        imposterCount = uiState.imposterCount,
                        onAddPlayer = { viewModel.addPlayer(it) },
                        onRemovePlayer = { viewModel.removePlayer(it) },
                        onUpdateDuration = { viewModel.updateRoundDuration(it) },
                        onUpdateImposters = { viewModel.updateImposterCount(it) },
                        onStart = { viewModel.proceedToGameSetup() },
                        onOpenInfo = { showInfoModal = true }
                    )
                    AppScreen.G1_ASSIGNMENT -> G1AssignmentScreen(
                        player = uiState.players[uiState.currentPlayerIndex],
                        questionSafe = uiState.questionSafe,
                        questionImpostor = uiState.questionImpostor,
                        onContinue = { viewModel.goToG1Input() }
                    )
                    AppScreen.G1_INPUT -> G1InputScreen(
                        player = uiState.players[uiState.currentPlayerIndex],
                        onSubmitAnswer = { viewModel.submitG1Answer(it) }
                    )
                    AppScreen.G1_REVEAL -> G1RevealScreen(
                        players = uiState.players,
                        onStartDiscussion = { viewModel.startDiscussion() }
                    )
                    AppScreen.G2_CATEGORY_SELECT -> G2CategorySelectScreen(
                        onSelectCategory = { viewModel.selectCategory(it) }
                    )
                    AppScreen.G2_REVEAL_FLOW -> G2RevealFlowScreen(
                        player = uiState.players[uiState.currentPlayerIndex],
                        wordSafe = uiState.wordSafe,
                        wordImpostor = uiState.wordImpostor,
                        onDone = { viewModel.finishG2Reveal() }
                    )
                    AppScreen.G2_PASS_PHONE -> G2PassPhoneScreen(
                        nextPlayerName = if (uiState.currentPlayerIndex == uiState.players.size - 1) "Everyone" else uiState.players[uiState.currentPlayerIndex + 1].name,
                        onContinue = { viewModel.passPhoneContinue() }
                    )
                    AppScreen.G1_DISCUSS, AppScreen.G2_GAMEPLAY -> DiscussionScreen(
                        timeLeft = uiState.timeLeft,
                        isRunning = uiState.isTimerRunning,
                        onToggleTimer = { viewModel.toggleTimer() },
                        onVote = { viewModel.goToVote() },
                        question = if (uiState.activeGame == com.example.suspect.model.GameMode.NUMBER_JUSTIFY) uiState.questionSafe else "Category: ${uiState.category}",
                        players = uiState.players,
                        gameMode = uiState.activeGame
                    )
                    AppScreen.G1_VOTE, AppScreen.G2_VOTE -> VoteScreen(
                        players = uiState.players,
                        votes = uiState.votes,
                        onToggleVote = { viewModel.toggleVote(it) },
                        onConfirmVote = { viewModel.confirmVote() }
                    )
                    AppScreen.G1_RESULTS, AppScreen.G2_RESULTS -> ResultsScreen(
                        players = uiState.players,
                        votes = uiState.votes,
                        gameMode = uiState.activeGame,
                        qImpostor = uiState.questionImpostor,
                        qSafe = uiState.questionSafe,
                        wImpostor = uiState.wordImpostor,
                        wSafe = uiState.wordSafe,
                        onPlayAgain = { viewModel.playAgain() },
                        onChangePlayers = { viewModel.changePlayers() }
                    )
                }
            }
        }
    }
}
