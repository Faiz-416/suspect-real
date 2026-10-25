package com.example.suspect.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.Player
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.AppSubHeading
import com.example.suspect.ui.components.SecondaryButton
import com.example.suspect.ui.theme.AccentBlue
import com.example.suspect.ui.theme.AccentPurple
import com.example.suspect.ui.theme.DarkCard
import com.example.suspect.ui.theme.TextMuted
import com.example.suspect.ui.theme.TextPrimary
import kotlinx.coroutines.delay

@Composable
fun G2RevealFlowScreen(
    player: Player,
    wordSafe: String,
    wordImpostor: String,
    onDone: () -> Unit
) {
    var isHolding by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var showWord by remember { mutableStateOf(false) }
    var hasRevealed by remember { mutableStateOf(false) }

    LaunchedEffect(isHolding) {
        if (isHolding && !showWord) {
            val startTime = System.currentTimeMillis()
            val duration = 1700L
            while (isHolding && progress < 1f) {
                val elapsed = System.currentTimeMillis() - startTime
                progress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
                if (progress >= 1f) {
                    showWord = true
                    hasRevealed = true
                }
                delay(16L)
            }
        } else if (!isHolding && !hasRevealed) {
            progress = 0f
            showWord = false
        }
    }

    val displayWord = if (player.isImpostor) wordImpostor else wordSafe
    val secondsLeft = ((1f - progress) * 2.5f).toInt().coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            AppHeading(player.name, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            AppSubHeading("Guard the scrolls! The spies are among us!", textAlign = TextAlign.Center)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(DarkCard)
                    .border(4.dp, AccentPurple.copy(alpha = 0.3f), CircleShape)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                isHolding = true
                                tryAwaitRelease()
                                isHolding = false
                            }
                        )
                    }
                    .testTag("hold_to_reveal_button"),
                contentAlignment = Alignment.Center
            ) {
                // Fill progress background
                if (progress > 0f && !showWord) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(progress)
                            .align(Alignment.BottomCenter)
                            .background(AccentPurple.copy(alpha = 0.35f))
                    )
                }

                if (showWord) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "YOUR WORD",
                            color = AccentBlue,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = displayWord,
                            color = TextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                } else if (isHolding) {
                    Text(
                        text = "$secondsLeft",
                        color = TextPrimary,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Black
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Hold to reveal",
                            tint = TextMuted,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Hold to Reveal",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (hasRevealed) {
            AnimatedVisibility(
                visible = hasRevealed,
                enter = fadeIn() + scaleIn(initialScale = 0.9f)
            ) {
                SecondaryButton(
                    text = "Continue",
                    onClick = onDone,
                    testTag = "g2_reveal_continue_button"
                )
            }
        }
    }
}
