package com.example.suspect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.suspect.model.Player
import com.example.suspect.ui.components.AppCard
import com.example.suspect.ui.components.AppHeading
import com.example.suspect.ui.components.AppInputField
import com.example.suspect.ui.components.PrimaryButton
import com.example.suspect.ui.theme.*
import kotlin.math.max
import kotlin.math.min

@Composable
fun PlayerSetupScreen(
    players: List<Player>,
    roundDuration: Int,
    imposterCount: Int,
    onAddPlayer: (String) -> Unit,
    onRemovePlayer: (String) -> Unit,
    onUpdateDuration: (Int) -> Unit,
    onUpdateImposters: (Int) -> Unit,
    onStart: () -> Unit,
    onOpenInfo: () -> Unit
) {
    var nameInput by remember { mutableStateOf("") }

    val formatDuration = remember(roundDuration) {
        val m = roundDuration / 60
        val s = roundDuration % 60
        "$m:${s.toString().padStart(2, '0')}"
    }

    val maxImposters = max(1, players.size / 2)

    val submitPlayer = {
        if (nameInput.isNotBlank()) {
            onAddPlayer(nameInput)
            nameInput = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppHeading("Players")
            IconButton(
                onClick = onOpenInfo,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                    .testTag("player_setup_info_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = TextMuted
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppInputField(
                value = nameInput,
                onValueChange = { nameInput = it },
                placeholder = "Enter player name",
                onDone = submitPlayer,
                modifier = Modifier.weight(1f),
                testTag = "add_player_input"
            )
            IconButton(
                onClick = submitPlayer,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(AccentPurple)
                    .testTag("add_player_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Player",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopStart
        ) {
            if (players.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Add at least 3 players",
                        color = TextMuted.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    players.chunked(2).forEach { rowPlayers ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowPlayers.forEach { p ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(DarkCard)
                                        .border(1.dp, Color.White.copy(alpha = 0.08f), CircleShape)
                                        .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 8.dp)
                                ) {
                                    Text(
                                        text = p.name,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(
                                        onClick = { onRemovePlayer(p.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove ${p.name}",
                                            tint = TextMuted,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppCard(
                modifier = Modifier.weight(1f),
                testTag = "timer_config_card"
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "TIME",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { onUpdateDuration(max(30, roundDuration - 30)) },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkCardSecondary)
                            .testTag("decrease_time_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease time",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = formatDuration,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(
                        onClick = { onUpdateDuration(min(300, roundDuration + 30)) },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkCardSecondary)
                            .testTag("increase_time_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase time",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            AppCard(
                modifier = Modifier.weight(1f),
                testTag = "imposters_config_card"
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Imposters",
                        tint = TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "IMPOSTERS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { onUpdateImposters(max(1, imposterCount - 1)) },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkCardSecondary)
                            .testTag("decrease_imposters_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease imposters",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = "${min(imposterCount, maxImposters)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(
                        onClick = { onUpdateImposters(min(maxImposters, imposterCount + 1)) },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(DarkCardSecondary)
                            .testTag("increase_imposters_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase imposters",
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = "Start Mission (${players.size})",
            onClick = onStart,
            enabled = players.size >= 3,
            testTag = "start_mission_button"
        )
    }
}
