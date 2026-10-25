package com.example.suspect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.GameMode
import com.example.suspect.model.Player
import com.example.suspect.ui.components.AppCard
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.PrimaryButton
import com.example.suspect.ui.components.SecondaryButton
import com.example.suspect.ui.theme.*

@Composable
fun ResultsScreen(
    players: List<Player>,
    votes: Map<String, String>,
    gameMode: GameMode?,
    qImpostor: String,
    qSafe: String,
    wImpostor: String,
    wSafe: String,
    onPlayAgain: () -> Unit,
    onChangePlayers: () -> Unit
) {
    val votedPlayerId = votes["GROUP_DECISION"]
    val votedPlayer = players.find { it.id == votedPlayerId }
    val groupWon = votedPlayer?.isImpostor == true
    val impostors = players.filter { it.isImpostor }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Status Badge
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(if (groupWon) AccentPurple else DangerRed)
                .border(4.dp, if (groupWon) AccentBlue else Color.White, CircleShape)
                .padding(horizontal = 32.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (groupWon) "CAUGHT!" else "ESCAPED!",
                color = TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppHeading(
                text = if (groupWon) "PLAYERS WIN" else "IMPOSTORS WIN",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = "MINORITY GROUP (IMPOSTORS):",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                impostors.forEach { imp ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DangerRed)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = imp.name,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            AppCard(
                borderColor = Color.White.copy(alpha = 0.08f),
                testTag = "results_secrets_card"
            ) {
                Column {
                    Text(
                        text = "IMPOSTOR SECRET",
                        color = DangerRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (gameMode == GameMode.NUMBER_JUSTIFY) qImpostor else wImpostor,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "PLAYER SECRET",
                        color = AccentBlue,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (gameMode == GameMode.NUMBER_JUSTIFY) qSafe else wSafe,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = "Players",
                onClick = onChangePlayers,
                modifier = Modifier.weight(1f),
                testTag = "results_change_players_button"
            )

            PrimaryButton(
                text = "Play Again",
                onClick = onPlayAgain,
                modifier = Modifier.weight(2f),
                testTag = "results_play_again_button",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Play Again",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            )
        }
    }
}
