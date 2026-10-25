package com.example.suspect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
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
fun DiscussionScreen(
    timeLeft: Int,
    isRunning: Boolean,
    onToggleTimer: () -> Unit,
    onVote: () -> Unit,
    question: String,
    players: List<Player>,
    gameMode: GameMode?
) {
    val isLowTime = remember(timeLeft) { timeLeft in 1..10 }

    val formattedTimer = remember(timeLeft) {
        val m = timeLeft / 60
        val s = timeLeft % 60
        "$m:${s.toString().padStart(2, '0')}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        AppHeading("Discussion", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        AppCard(
            borderColor = AccentPurple.copy(alpha = 0.3f),
            backgroundColor = AccentPurple.copy(alpha = 0.1f),
            testTag = "discussion_topic_card"
        ) {
            Text(
                text = "\"$question\"",
                color = AccentPurple,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (gameMode == GameMode.NUMBER_JUSTIFY) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 140.dp)
            ) {
                items(players) { p ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkCard)
                            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = p.name.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = p.answer ?: "-",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentPurple,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formattedTimer,
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = if (isLowTime) DangerRed else TextPrimary,
                modifier = Modifier.testTag("discussion_timer_text")
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SecondaryButton(
                text = "",
                onClick = onToggleTimer,
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp),
                testTag = "toggle_timer_button",
                icon = {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pause" else "Play",
                        tint = TextPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            )

            PrimaryButton(
                text = "Vote Now",
                onClick = onVote,
                modifier = Modifier
                    .weight(2f)
                    .height(60.dp),
                testTag = "vote_now_button"
            )
        }
    }
}
