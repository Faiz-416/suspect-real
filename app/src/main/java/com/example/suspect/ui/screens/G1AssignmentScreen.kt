package com.example.suspect.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.Player
import com.example.suspect.ui.components.AppCard
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.AppSubHeading
import com.example.suspect.ui.components.PrimaryButton
import com.example.suspect.ui.components.SecondaryButton
import com.example.suspect.ui.theme.AccentPurple
import com.example.suspect.ui.theme.TextPrimary

@Composable
fun G1AssignmentScreen(
    player: Player,
    questionSafe: String,
    questionImpostor: String,
    onContinue: () -> Unit
) {
    var revealed by remember(player.id) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppSubHeading("Pass device to", textAlign = TextAlign.Center)
        AppHeading(player.name, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 32.dp))

        if (!revealed) {
            SecondaryButton(
                text = "I am ${player.name}",
                onClick = { revealed = true },
                testTag = "g1_i_am_player_button"
            )
        } else {
            AnimatedVisibility(
                visible = revealed,
                enter = fadeIn() + scaleIn(initialScale = 0.9f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppCard(
                        borderColor = AccentPurple.copy(alpha = 0.3f),
                        modifier = Modifier.padding(bottom = 24.dp),
                        testTag = "g1_question_card"
                    ) {
                        Text(
                            text = "YOUR QUESTION",
                            color = AccentPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(
                            text = if (player.isImpostor) questionImpostor else questionSafe,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            lineHeight = 30.sp
                        )
                    }

                    PrimaryButton(
                        text = "Answer Now",
                        onClick = onContinue,
                        testTag = "g1_answer_now_button"
                    )
                }
            }
        }
    }
}
